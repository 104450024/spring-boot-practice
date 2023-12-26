package com.example.final1.demo.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

/*@ComponentcharlieSQL
@ConfigurationProperties(prefix = "book")*/
//升級為能和ORM映射的java
@Entity
@Table(name = "tb_seckill_voucher")
public class KillVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = false)
    private int id;

    private int stock;

    @CreationTimestamp
    private LocalDateTime create_time;

    @CreationTimestamp
    private LocalDateTime begin_time;

    @CreationTimestamp
    private LocalDateTime end_time;

    @CreationTimestamp
    private LocalDateTime update_time;

    public KillVoucher() {
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDateTime getBeginTime() {
        return begin_time;
    }

    public void setBeginTime(LocalDateTime begin_time) {
        this.begin_time = begin_time;
    }

    public LocalDateTime getEndTime() {
        return end_time;
    }

    public void setEndTime(LocalDateTime end_time) {
        this.end_time = end_time;
    }

}
