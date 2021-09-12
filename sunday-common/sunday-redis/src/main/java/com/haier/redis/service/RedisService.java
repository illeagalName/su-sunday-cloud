package com.haier.redis.service;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/12 21:01
 */
public class RedisService {
    @Autowired
    RedissonClient redissonClient;

    public <T> T get(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    public <T> void put(String key, T obj) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(obj);
    }
}
