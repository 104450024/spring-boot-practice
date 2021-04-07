package com.example.final1.demo.web;


import com.example.final1.demo.domain.Book;
import com.example.final1.demo.service.Bookservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class BookController {


    private final Bookservice bookservice;

    @Autowired
    public BookController(Bookservice bookservice) {
        this.bookservice = bookservice;
    }

    @GetMapping("/book")
    public String list()
    {
        return "books";
    }

    @GetMapping("/books")
    public String list(Model model)
    {
        List<Book> books= bookservice.findAll();

        model.addAttribute("books",books);
        return "books";
    }

    @GetMapping("/booksid/{status}")
    public String listStatus(@PathVariable long status,Model model)
    {
        List<Book> books = bookservice.findBystatus(status);
        model.addAttribute("books",books);
        return "book";
    }


    //跳轉至書單詳情(book)
    @GetMapping("books/{id}")
    public String detail(@PathVariable long id,Model model)
    {
        Book book=bookservice.findOne(id);
        model.addAttribute("book",book);
        return "book";
    }

    //跳轉input提交頁面(新增書單)
    @GetMapping("/books/input")
    public String inputPage(Model model)
    {
        model.addAttribute("book",new Book());
        return "input";
    }

    //提交一個書單信息
    @PostMapping("/books")
    public String post(Book book, final RedirectAttributes attributes)
    {
       Book book1= bookservice.save(book);
       if (book1!=null)
       {
           attributes.addAttribute("message"," <"+book1.getName()+" 信息提交成功");
       }
        return "redirect:/books";

    }

    //跳轉至更新頁面
    @GetMapping("/books/{id}/input")
    public String inputEditPage(@PathVariable long id,Model model)
    {
        Book book= bookservice.findOne(id);
        model.addAttribute("book",book);
        return "input";
    }



    /** POST--REDIRECT--GET
     * 這是 redirect
     *
     * **/

}
