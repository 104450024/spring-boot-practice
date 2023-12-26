package com.example.final1.demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;

public interface KillVoucherRepository extends JpaRepository<KillVoucher, Integer> {

  @Query("select b from Book b where b.id= ?1")
  Book findOne(Long id);

  @Query(value = "SELECT * FROM tb_seckill_voucher t WHERE t.id = :id", nativeQuery = true)
  KillVoucher findVKillVoucherID(@Param("id") Integer id);

  @Modifying
  @Query(value = "update tb_seckill_voucher t set t.stock = t.stock - 1 where t.id = :id1 and t.stock > 0", nativeQuery = true)
  int updateKillVoucherMinus(@Param("id1") Integer id1);

}
