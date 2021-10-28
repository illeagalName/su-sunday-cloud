package com.haier.job.service;

import com.haier.api.bot.RemoteBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/28 22:31
 */
@Service
public class CommonService {

    @Autowired
    RemoteBotService remoteBotService;

    public void sendMessage(String key, String content){
        remoteBotService.sendMessage(key.concat(" - ").concat(content));
    }
}
