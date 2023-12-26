package com.example.final1.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.final1.demo.utils.RedisIdWorker;
import com.example.final1.demo.web.BookController;

@SpringBootTest
public class ShopExpireTest {

  @Resource
  private BookController bookController;

  @Resource
  private RedisIdWorker redisIdWorker;

  // 線城池為異步調用，需要加 CountDownLatch 計時
  private ExecutorService es = Executors.newFixedThreadPool(500);

  @Test
  void testIdWorker() throws InterruptedException {

    CountDownLatch latch = new CountDownLatch(300);

    Runnable task = () -> {
      for (int i = 0; i < 100; i++) {

        long id = redisIdWorker.nextId("order");
        System.out.println("id = " + id);

      }
      latch.countDown();
    };
    long begin = System.currentTimeMillis();
    for (int i = 0; i < 300; i++) {
      es.submit(task);
    }
    latch.await();
    long end = System.currentTimeMillis();

    System.out.println("time = " + (end - begin));
  }

  @Test
  void testSaveShop() {

    bookController.saveShop2Redis(8L, 10L);
  }

}
