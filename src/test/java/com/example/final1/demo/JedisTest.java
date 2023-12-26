package com.example.final1.demo;

import org.hibernate.mapping.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

public class JedisTest {

  private Jedis jedis;

  @BeforeEach
  void setUp() {

    // 1.建立連接
    jedis = new Jedis("127.0.0.1", 6379);
    // 2.設置密碼
    jedis.select(0);

  }

  @Test
  void testString() {
    String result = jedis.set("name", "虎哥");
    System.out.println("reslut = " + result);
    // 獲取數據
    String name = jedis.get("name");
    System.out.println("name = " + name);
  }

  @Test
  void testHash() {
    // insert hash data
    jedis.hset("user:3", "name", "Jack");
    jedis.hset("user:4", "age", "21");
    java.util.Map<String, String> hgetAll = jedis.hgetAll("user:1");

    System.out.println(hgetAll);

  }

  @Test
  void testPrint() {
    System.out.println("test");
  }

  @AfterEach
  void tearDown() {
    if (jedis != null) {
      jedis.close();
    }
  }
}
