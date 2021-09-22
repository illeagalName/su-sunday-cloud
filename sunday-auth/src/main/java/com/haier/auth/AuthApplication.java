package com.haier.auth;

import com.haier.core.annotation.Enable7FeignClients;
import com.haier.core.annotation.Enable7CustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/21 21:01
 */
@SpringBootApplication
@Enable7FeignClients
@Enable7CustomConfig
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
