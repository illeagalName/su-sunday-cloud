package com.haier.bot.controller;

import com.haier.core.util.DataUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
public class BotController {

    @Autowired
    Bot bot;

    @GetMapping("send")
    public Map<String, String> sendMessage(@RequestParam("message") String message) {
        log.info("接收数据");
        Map<String, String> resultMap = new HashMap<>();
        if (DataUtils.isNotEmpty(message)){
            MessageReceipt<Group> messageReceipt = Objects.requireNonNull(bot.getGroup(318195769)).sendMessage(new PlainText(message));
            if (messageReceipt.isToGroup()) {
                log.info("sendMessage,success");
                resultMap.put("status", "success");
                resultMap.put("msg", "success");
                resultMap.put("result", "success");
                return resultMap;
            }
        }
        log.info("sendMessage,没有发到群");
        resultMap.put("status", "fail");
        resultMap.put("msg", "没有发到群");
        return resultMap;
    }
}
