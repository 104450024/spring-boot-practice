package com.example.final1.demo.web;


import com.example.final1.demo.domain.Book;
import com.example.final1.demo.service.Bookservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class BookApp {

/**  postman 測試類 **/

    private final Bookservice bookservice;

    @Autowired
    public BookApp(Bookservice bookservice) {
        this.bookservice = bookservice;
    }


    //獲取讀書清單列表
    @GetMapping("/bookss")
    public List<Book> getAll()
    {
        return bookservice.findAll();
    }

    @PostMapping("/booksave")
    public Book save(Book book)
    {
        /*Book book=new Book();
        book.setName(name);
        book.setAuthor(author);
        book.setDescription(description);
        book.setStatus(status);*/

        return bookservice.save(book);
    }

    @GetMapping("/booksave/{id}")
    public Book getOne(@PathVariable Long id)
    {
        return bookservice.findOne(id);
    }


    @PutMapping("/bookss")
    public Book Update(@RequestParam int id,
                       @RequestParam String name,
                       @RequestParam String author,
                       @RequestParam String description,
                       @RequestParam int status)
    {
        Book book=new Book();
        book.setId(id);
        book.setName(name);
        book.setAuthor(author);
        book.setDescription(description);
        book.setStatus(status);

        return bookservice.save(book);
    }


    @DeleteMapping("/bookss/{id}")
    public void delete(@PathVariable long id)
    {

        bookservice.DeleteOne(id);

    }


    @PostMapping("/bookss/by")     //利用author找書單
    public List<Book> findByAuthor(@RequestParam String author)
    {

        return bookservice.findByauthor(author);
    }



    @PostMapping("/bookss/by1")  //利用 author 和 status 找書單
    public List<Book> findByAuthorAndStatus(@RequestParam String author,@RequestParam int status)
    {
        return bookservice.findByAuthorAndStatus(author,status);
    }


 /*   @PostMapping("/b")
    public List<Book> findByEndsDescription(@RequestParam String des)
    {
        return bookservice.findByEndWithDescription(des);
    }*/


    @PostMapping("/bookss/des")
    public List<Book> findByDescriptionContains(@RequestParam String des)
    {
        return bookservice.findByDescriptionContains(des);
    }

    @PostMapping("/bookss/sql")
    public List<Book> findBySQL(@RequestParam int len)
    {
        return bookservice.findBySQL(len);
    }

    @PostMapping("/bookss/update")
    public int UpdateByJPQL(@RequestParam int status,@RequestParam long id)
    {
        return bookservice.UpdateByJPQL(status,id);
    }

    @PostMapping("/bookss/delete")
    public int deleteByJPQL(@RequestParam long id)
    {
        return bookservice.deleteByJPQL(id);
    }

    @PostMapping("/bookss/updateanddelete")
    public int UpdateByJPQL(@RequestParam long id,@RequestParam int status,@RequestParam long uid)
    {
        return bookservice.deleteAndUpdate(id,status,uid);
    }

    @GetMapping("/booksPage")
    public Page<Book> getAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size)
    {
        Pageable pageable= PageRequest.of(page,size, Sort.by("id").descending());
        return bookservice.findAllByPage(pageable);

    }

}
