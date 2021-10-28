package com.haier.job.controller;

import com.haier.api.bot.RemoteBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/28 22:25
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    RemoteBotService remoteBotService;

    @GetMapping("/send")
    public void xxlJobGroup(){
        remoteBotService.sendMessage("4323");
    }
}
