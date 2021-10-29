package com.haier.bot.controller;

import com.haier.core.domain.R;
import com.haier.core.util.DataUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.bot.controller
 * @ClassName: BotController
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/28 12:07
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("message")
@RefreshScope
public class BotController {

    @Resource
    Bot bot;

    @Resource
    ThreadPoolTaskExecutor taskExecutor;

    @Value("${qq.config.groupId}")
    Long groupId;

    @GetMapping("send")
    public R<String> sendMessage(@RequestParam("message") String message) {
        if (DataUtils.isNotEmpty(message)) {
            log.info(message);
            CompletableFuture.runAsync(() -> {
                MessageReceipt<Group> messageReceipt = Objects.requireNonNull(bot.getGroup(groupId)).sendMessage(new PlainText(message + "\n \n 喝水时间到了！！！！"));
                if (messageReceipt.isToGroup()) {
                    log.info("消息已发送至群 {}", groupId);
                } else {
                    log.warn("消息未发送至群 {}", groupId);
                }
            }, taskExecutor);
        }
        log.info("发送消息执行成功");
        return R.success("发送成功");
    }
}
