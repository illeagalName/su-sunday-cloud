package com.haier.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.bot
 * @ClassName: BotApplication
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/28 10:53
 * @Version: 1.0
 */
@SpringBootApplication
@EnableScheduling
public class BotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
