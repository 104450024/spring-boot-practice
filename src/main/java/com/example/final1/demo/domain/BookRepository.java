package com.example.final1.demo.domain;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/** 定義搜尋方法 **/
public interface BookRepository extends JpaRepository<Book, Long> {

  /* Page<Book> findAll(Pageable pageable); */

  Page<Book> findAll(Pageable pageage);

  @Query("select b from Book b where b.id= ?1")
  Book findOne(Long id);

  List<Book> findByAuthor(String author);

  List<Book> findByAuthorAndStatus(String author, int status);

  /* List<Book> findByDescriptionEndsWith(String des); */

  List<Book> findByStatus(long status);

  List<Book> findByDescriptionContains(String des);

  /* @Query("select b from Book b where length(b.name)> ?1 ") */
  /*
   * @Query(value = "select * from Book where LENGTH(b.name)> ?1 ", nativeQuery =
   * true)
   */
  @Query("select b from Book b where length(b.name)> ?1 ")
  List<Book> findByJPQL(int len);

  /*
   * @Transactional
   * 
   * @Modifying //增強 Query 注釋 update delete insert
   * 
   * @Query("update Book b set b.status= ?1 where b.id= ?2")
   * int UpdateByJPQL(int status,long id);
   */

  @Transactional
  @Modifying
  @Query("update Book b set b.status= ?1 where b.id= ?2")
  int UpdateByJPQL(int status, long id);

  @Transactional
  @Modifying
  @Query("delete from Book b where b.id= ?1")
  int deleteByJPQL(long id);

}
