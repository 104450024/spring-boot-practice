package com.example.final1.demo.utils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CacheClient {
  private final StringRedisTemplate stringRedisTemplate;

  public CacheClient(StringRedisTemplate stringRedisTemplate) {

    this.stringRedisTemplate = stringRedisTemplate;
  }

  public void set(String key, Object value, Long time, TimeUnit unit) {
    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
  }

  public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {

    // 設置邏輯過期
    RedisData redisData = new RedisData();
    redisData.setData(value);
    redisData.setExpirTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
    // 寫入 Redis
    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));

  }

  public <R, ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time,
      TimeUnit unit) {
    String key1 = keyPrefix + id;
    // 從 redis 查詢商鋪緩存
    String json = stringRedisTemplate.opsForValue().get(key1);

    // 判斷是否存在
    if (StrUtil.isNotBlank(json)) {

      return JSONUtil.toBean(json, type);
    }
    // 判斷命中的是否為空值
    if (json != null) {

      return null;
    }
    // 不存在，根據id查詢資料庫
    R r = dbFallback.apply(id);
    // 不存在，返回錯誤
    if (r == null) {
      // 將空值寫入redis
      stringRedisTemplate.opsForValue().set(key1, "", 500, TimeUnit.SECONDS);
      // 返回錯誤訊息

      return null;
    }
    // 存在 寫入redis
    this.set(key1, r, time, unit);

    return r;
  }
}
