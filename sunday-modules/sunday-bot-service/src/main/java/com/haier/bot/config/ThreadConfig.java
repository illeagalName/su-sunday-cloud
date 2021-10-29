package com.haier.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ProjectName: xunk
 * @Package: com.xunk.equipment.config
 * @ClassName: ThreadConfig
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/3/29 11:34
 * @Version: 1.0
 */
@Configuration
public class ThreadConfig {

    /**
     * 线程数后面再定，Runtime.getRuntime().availableProcessors()
     *
     * @return
     */
    @Bean(name = "asyncServiceTaskExecutor")
    public ThreadPoolTaskExecutor asyncServiceTaskExecutor() {
        return createThreadPool(5, 20, 1024, "async-bot-service-task-");
    }

    private ThreadPoolTaskExecutor createThreadPool(Integer core, Integer max, Integer capacity, String namePrefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(core); // 50
        // 设置最大线程数
        executor.setMaxPoolSize(max); // 100
        // 设置队列容量
        executor.setQueueCapacity(capacity); // 1024
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix(namePrefix);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
//        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

}
