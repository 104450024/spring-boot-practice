package com.example.final1.demo.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.final1.demo.domain.Book;
import com.example.final1.demo.domain.BookRepository;
import com.example.final1.demo.service.Bookservice;
import com.example.final1.demo.utils.CacheClient;
import com.example.final1.demo.utils.RedisData;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@RestController
public class BookController {

    private final Bookservice bookservice;
    private final BookRepository bookRepository;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    @Autowired
    public BookController(Bookservice bookservice, BookRepository bookRepository) {
        this.bookservice = bookservice;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/book")
    public String list() {
        return "books";
    }

    @GetMapping("/books")
    public String list(Model model) {
        List<Book> books = bookservice.findAll();

        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/booksid/{status}")
    public String listStatus(@PathVariable long status, Model model) {
        List<Book> books = bookservice.findBystatus(status);
        model.addAttribute("books", books);
        return "book";
    }

    // 跳轉至書單詳情(book)
    // @GetMapping("books/{id}")
    // public String detail(@PathVariable long id) {

    // // 緩存穿透
    // // String Throw = queryWithPassThrough(id);

    // // 緩存擊穿
    // // String shop = queryWithPassMutex(id);

    // // if (shop == "商品不存在") {
    // // return "商品不存在";
    // // }

    // // return "商品" + id + "查詢成功";

    // }

    // 跳轉至書單詳情(book)
    @GetMapping("books/{id}")
    public String detail(@PathVariable long id) {
        // Book shop = queryWithLogicalExpire(id);
        // Book shop1 = cacheClient.queryWithPassThrough("cache:book:", id, Book.class,
        // bookRepository.findOne(id),
        // 600,
        // TimeUnit.MINUTES);

        // return shop;
        // 緩存擊穿(邏輯過期)
        String shop = queryWithPassMutex(id);

        return shop;

    }

    private Boolean tryLock(String key) {

        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);

        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key) {
        stringRedisTemplate.delete(key);

    }

    // 重建一個線程
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    public Book queryWithLogicalExpire(Long id) {
        String key1 = "cache:book:" + id;
        // 從 redis 查詢商鋪緩存
        String shopJson = stringRedisTemplate.opsForValue().get(key1);

        // 判斷是否存在
        if (StrUtil.isBlank(shopJson)) {

            // 不存在，直接返回空值
            return null;
        }
        // 命中，需要先把 json 反序列化為對象
        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
        Book book1 = JSONUtil.toBean((JSONObject) redisData.getData(), Book.class);
        LocalDateTime expirTime = redisData.getExpirTime();
        // 判斷是否過期
        if (expirTime.isAfter(LocalDateTime.now())) {
            // 未過期，直接返回店鋪信息
            return book1;
        }

        // 已過期，需要緩存重建

        // 緩存重建
        // 獲取互斥鎖(判斷是否獲取成功)
        String lockKey = "lock:shop:" + id;
        Boolean isLock = tryLock(lockKey);
        if (isLock) {
            // 成功 : 開啟獨立線程，實施緩存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                // 重建緩存
                try {
                    this.saveShop2Redis(id, 30L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unlock(lockKey);
                }
            });
        }

        // 返回過期的商鋪信息

        // 不存在，根據id查詢資料庫
        // Book book = bookRepository.findOne(id);

        // stringRedisTemplate.opsForValue().set(key1, JSONUtil.toJsonStr(book), 600,
        // TimeUnit.SECONDS);

        return book1;
    }

    public String queryWithPassThrough(Long id) {
        String key1 = "cache:book:" + id;
        // 從 redis 查詢商鋪緩存
        String shopJson = stringRedisTemplate.opsForValue().get(key1);

        // 判斷是否存在
        if (StrUtil.isNotBlank(shopJson)) {

            return "商品存在";
        }
        // 判斷命中的是否為空值
        if (shopJson != null) {
            return "商品不存在";
        }
        // 不存在，根據id查詢資料庫
        Book book = bookRepository.findOne(id);
        // 不存在，返回錯誤
        if (book == null) {
            // 將空值寫入redis
            stringRedisTemplate.opsForValue().set(key1, "", 500, TimeUnit.SECONDS);
            // 返回錯誤訊息

            return "商品不存在";
        }
        stringRedisTemplate.opsForValue().set(key1, JSONUtil.toJsonStr(book), 600, TimeUnit.SECONDS);

        return "book存入";
    }

    public String queryWithPassMutex(Long id) {
        String key1 = "cache:book:" + id;
        // 從 redis 查詢商鋪緩存
        String shopJson = stringRedisTemplate.opsForValue().get(key1);

        // 判斷是否存在
        if (StrUtil.isNotBlank(shopJson)) {

            return "商品存在";
        }
        // 判斷命中的是否為空值
        if (shopJson != null) {
            return "商品不存在";
        }
        // 實現緩存重建
        // 獲取互斥鎖
        String lockKey = "lock:shop:" + id;
        try {
            Boolean isLock = tryLock(lockKey);
            // 判斷是否獲取成功
            if (!isLock) {
                // 失敗，則休眠並重試
                Thread.sleep(50);
                return queryWithPassMutex(id);
            }

            String shopJson1 = stringRedisTemplate.opsForValue().get(key1);

            if (StrUtil.isNotBlank(shopJson1)) {

                return "商品存在";
            }

            // 成功，根據id查詢數據庫
            Book book = bookRepository.findOne(id);
            // 模擬重建延遲
            Thread.sleep(200);
            // 不存在，返回錯誤
            if (book == null) {
                // 將空值寫入redis
                stringRedisTemplate.opsForValue().set(key1, "", 500, TimeUnit.SECONDS);
                // 返回錯誤訊息

                return "商品不存在";
            }
            // 存在，寫入redis
            stringRedisTemplate.opsForValue().set(key1, JSONUtil.toJsonStr(book), 600, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 釋放互斥鎖
            unlock(lockKey);
        }

        return "book存入";
    }

    public void saveShop2Redis(Long id, Long expireSecnods) {
        // 1.查詢數據
        Book book = bookRepository.findOne(id);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 2.封裝邏輯過期時間
        RedisData redisData = new RedisData();
        redisData.setData(book);
        redisData.setExpirTime(LocalDateTime.now().plusSeconds(expireSecnods));
        // 3.寫入Redis
        stringRedisTemplate.opsForValue().set("cache:book:" + id, JSONUtil.toJsonStr(redisData));

    }

    // 跳轉input提交頁面(新增書單)
    @GetMapping("/books/input")
    public String inputPage(Model model) {
        model.addAttribute("book", new Book());
        return "input";
    }

    // 提交一個書單信息
    @PostMapping("/books")
    public String post(Book book, final RedirectAttributes attributes) {
        Book book1 = bookservice.save(book);
        if (book1 != null) {
            attributes.addAttribute("message", " <" + book1.getName() + " 信息提交成功");
        }
        return "redirect:/books";

    }

    // 跳轉至更新頁面
    @GetMapping("/books/{id}/input")
    public String inputEditPage(@PathVariable long id, Model model) {
        Book book = bookservice.findOne(id);
        model.addAttribute("book", book);
        return "input";
    }

    /**
     * POST--REDIRECT--GET
     * 這是 redirect
     *
     **/

}
