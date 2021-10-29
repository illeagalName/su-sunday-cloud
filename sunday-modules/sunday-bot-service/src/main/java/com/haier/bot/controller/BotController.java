package com.haier.bot.controller;

import com.haier.core.domain.R;
import com.haier.core.util.DataUtils;
import com.haier.core.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
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
            CompletableFuture.runAsync(() -> {
                try {
                    Group group = bot.getGroup(groupId);
                    if (Objects.isNull(group)) {
                        log.info("查询群号异常");
                        return;
                    }
                    String imgUrl = "https://api.vvhan.com/api/tao";
                    byte[] urlByByte = HttpUtils.getUrlByByte(imgUrl);
                    log.info("字节长度 {}", Optional.ofNullable(urlByByte).map(s -> s.length).orElse(0));
                    ExternalResource ex = ExternalResource.Companion.create(urlByByte);
                    Image image = group.uploadImage(ex);
                    MessageChain chain = new MessageChainBuilder()
                            .append(image)
                            .build();
                    MessageReceipt<Group> messageReceipt = group.sendMessage(chain);
                    if (messageReceipt.isToGroup()) {
                        log.info("消息已发送至群 {}", groupId);
                    } else {
                        log.warn("消息未发送至群 {}", groupId);
                    }
                    ex.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, taskExecutor);
        }
        log.info("发送消息执行成功");
        return R.success("发送成功");
    }
}
