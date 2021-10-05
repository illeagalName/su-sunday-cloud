package com.haier.minio.config;

import com.haier.core.util.JsonUtils;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/5 13:51
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioAutoConfiguration {

    @Resource
    MinioProperties minioProperties;

    @Bean
    @ConditionalOnMissingBean
    public MinioClient getMinioClient() {
        log.info("初始化Minio配置开始 {}", JsonUtils.toString(minioProperties));
        MinioClient client = MinioClient
                .builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        log.info("初始化Minio配置结束");
        return client;
    }
}
