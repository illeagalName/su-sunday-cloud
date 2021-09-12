package com.haier.user.controller;

import com.haier.redis.service.RedisService;
import com.haier.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/12 21:04
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    RedisService redisService;

    @GetMapping("get")
    public User get(@RequestParam String key) {
        return redisService.get(key);
    }

    @GetMapping("put")
    public String put(@RequestParam String key) {
        User c = new User();
        c.setAge(12);
        c.setName("张三");
        redisService.put(key, c);
        return "";
    }
}
