package com.example.final1.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*@ComponentcharlieSQL
@ConfigurationProperties(prefix = "book")*/
//升級為能和ORM映射的java
@Entity
@Table(name = "th_voucher_order")
public class VoucherOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = false)
    private int id;

    private int user_id;

    public VoucherOrder() {
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

}
