package com.example.final1.demo.utils;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RedisData {

  private LocalDateTime expirTime;
  private Object Data;
}
