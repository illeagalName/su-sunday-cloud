package com.haier.job;

import com.haier.core.annotation.Enable7CustomConfig;
import com.haier.core.annotation.Enable7FeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.job
 * @ClassName: JobApplication
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/22 11:06
 * @Version: 1.0
 */
@SpringBootApplication
@Enable7FeignClients
@Enable7CustomConfig
public class JobApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }
}
