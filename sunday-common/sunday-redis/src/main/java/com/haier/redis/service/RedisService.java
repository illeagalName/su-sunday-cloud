package com.haier.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: redis 简易封装
 * @Author Ami
 * @Date 2021/9/12 21:01
 */
@Slf4j
public class RedisService {

    @Autowired
    RedissonClient redissonClient;

    /**
     * 缓存对象
     *
     * @param key   键
     * @param value 值
     */

    public <T> void setObject(String key, T value) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * 缓存对象
     *
     * @param key      键
     * @param value    值
     * @param time     超时时间
     * @param timeUnit 时间单位
     */
    public <T> void setObject(String key, T value, Long time, TimeUnit timeUnit) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value, time, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key      键
     * @param time     过期时间
     * @param timeUnit 时间单位
     * @return Boolean
     */
    public Boolean expire(String key, Long time, TimeUnit timeUnit) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.expire(time, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key  键
     * @param time 过期时间
     * @return Boolean
     */
    public Boolean expire(String key, Long time) {
        return expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 获取对象
     *
     * @param key 键
     * @param <T> 值
     * @return T
     */
    public <T> T getObject(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 删除对象
     *
     * @param key 键
     * @return Boolean
     */
    public Boolean delete(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.delete();
    }

    /**
     * 批量删除
     *
     * @param keys 键
     * @return Boolean
     */
    public Boolean delete(Collection<String> keys) {
        keys.forEach(this::delete);
        return true;
    }

    /**
     * 保存List对象
     *
     * @param key    键
     * @param values 列表
     * @return Boolean
     */
    public <T> Boolean setList(String key, List<T> values) {
        RList<T> rList = redissonClient.getList(key);
        return rList.addAll(values);
    }

    /**
     * 获取列表所有的值
     *
     * @param key 键
     * @return List<T>
     */
    public <T> List<T> getList(String key) {
        RList<T> rList = redissonClient.getList(key);
        return rList.readAll();
    }

    /**
     * 获取列表 start后 的值
     *
     * @param key   键
     * @param start 开始索引
     * @return List<T>
     */
    public <T> List<T> getList(String key, Integer start) {
        RList<T> rList = redissonClient.getList(key);
        return rList.range(start);
    }

    /**
     * 获取列表 start后 end前 的值
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return List<T>
     */
    public <T> List<T> getList(String key, Integer start, Integer end) {
        RList<T> rList = redissonClient.getList(key);
        return rList.range(start, end);
    }

    /**
     * 保存set
     *
     * @param key    键
     * @param values 值
     * @return Boolean
     */
    public <T> Boolean setSet(String key, Set<T> values) {
        RSet<T> rSet = redissonClient.getSet(key);
        return rSet.addAll(values);
    }

    /**
     * 获取set
     *
     * @param key 键
     * @return Set<T>
     */
    public <T> Set<T> getSet(String key) {
        RSet<T> rSet = redissonClient.getSet(key);
        return rSet.readAll();
    }

    /**
     * 缓存 map
     *
     * @param key    键
     * @param values 值
     */
    public <K, V> void setMap(String key, Map<K, V> values) {
        RMap<K, V> rMap = redissonClient.getMap(key);
        rMap.putAll(values, 200);
    }

    /**
     * 缓存map 某个值
     *
     * @param key   键
     * @param hKey  map键
     * @param value 值
     * @return 旧值
     */
    public <K, V> V setMapValue(String key, K hKey, V value) {
        RMap<K, V> rMap = redissonClient.getMap(key);
        return rMap.put(hKey, value);
    }

    /**
     * 获取map中子键的值
     *
     * @param key  键
     * @param hKey map键
     * @return T
     */
    public <K, V> V getMapValue(String key, K hKey) {
        RMap<K, V> rMap = redissonClient.getMap(key);
        return rMap.get(hKey);
    }

    /**
     * 获取map
     *
     * @param key 键
     * @return Map<String, T>
     */
    public <K, V> Map<K, V> getMap(String key) {
        RMap<K, V> rMap = redissonClient.getMap(key);
        return rMap.readAllMap();
    }

    public List<String> keys(String pattern) {
        RKeys keys = redissonClient.getKeys();
        Iterable<String> keysByPattern = keys.getKeysByPattern(pattern);
        List<String> list = new ArrayList<>();
        keysByPattern.forEach(list::add);
        return list;
    }

    /**
     * 加锁
     *
     * @param key 键
     * @return RLock
     */
    public <T> RLock tryLock(String key) {
        return redissonClient.getLock(key);
    }

    /**
     * Lock lock = ...;
     *  if (lock.tryLock()) {
     *    try {
     *      // manipulate protected state
     *    } finally {
     *      lock.unlock();
     *    }
     *  } else {
     *    // perform alternative actions
     *  }
     * 此用法可确保在获得锁时解锁，如果未获得锁，则不会尝试解锁。
     * 返回：
     * 如果获得锁则为true ，否则为false
     * @param key 键
     */
    public <T> RLock lock(String key) {
        return redissonClient.getLock(key);
    }

}
