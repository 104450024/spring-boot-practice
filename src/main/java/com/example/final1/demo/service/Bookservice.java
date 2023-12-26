package com.example.final1.demo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.final1.demo.domain.Book;
import com.example.final1.demo.domain.BookRepository;

@Service
public class Bookservice {

    private final BookRepository bookRepository;

    @Autowired
    public Bookservice(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // 查詢所有的書單列表
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    // 分頁查詢書單列表
    public Page<Book> findAllByPage(Pageable pageable) {
        Page<Book> page = bookRepository.findAll(PageRequest.of(1, 5, Sort.Direction.DESC, "id"));

        return page;
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book findOne(Long id) {
        return bookRepository.findOne(id);
    }

    public void DeleteOne(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> findByauthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> findBystatus(long status) {
        return bookRepository.findByStatus(status);
    }

    public List<Book> findByAuthorAndStatus(String author, int status) {
        return bookRepository.findByAuthorAndStatus(author, status);
    }

    /*
     * public List<Book> findByEndWithDescription(String des)
     * {
     * return bookRepository.findByDescriptionEndsWith(des);
     * }
     */

    public List<Book> findByDescriptionContains(String des) {
        return bookRepository.findByDescriptionContains(des);
    }

    public List<Book> findBySQL(int len) {
        return bookRepository.findByJPQL(len);
    }

    public int UpdateByJPQL(int status, long id) {
        return bookRepository.UpdateByJPQL(status, id);
    }

    @Transactional
    public int deleteByJPQL(long id) {
        return bookRepository.deleteByJPQL(id);
    }

    @Transactional
    // 兩個自定義方法結合(刪除和更新)
    // Transactional 事務的應用: 有Transactional，當第二句出現錯誤時，第一句不會執行。
    public int deleteAndUpdate(long id, int status, long uid) {
        int dcount = bookRepository.deleteByJPQL(id);

        int ucount = bookRepository.UpdateByJPQL(status, id);

        return dcount + ucount;
    }

}
