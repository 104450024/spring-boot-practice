
package com.example.final1.demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/** 定義搜尋方法 **/
public interface VoucherOrderRepository extends JpaRepository<VoucherOrder, Integer> {

  // int countByUser_id(int parentUid);

}