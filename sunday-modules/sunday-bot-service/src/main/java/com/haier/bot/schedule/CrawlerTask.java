package com.haier.bot.schedule;

import com.haier.bot.service.BotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description:  为了方便bot服务单独部署(主要是服务器不行，只能部署一个)，定时任务就用自带的，从job服务移至到bot服务里
 * @Author Ami
 * @Date 2021/10/28 17:16
 */
@Component
@Slf4j
@RefreshScope
public class CrawlerTask {

    @Value("${qq.config.privateGroupId}")
    Long privateGroupId;

    @Resource
    BotService botService;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void jokeTask() {
        botService.getJokeInfo(privateGroupId);
    }

}
