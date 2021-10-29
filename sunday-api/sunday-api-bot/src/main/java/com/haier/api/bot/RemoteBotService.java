package com.haier.api.bot;

import com.haier.api.bot.factory.BotFallbackFactory;
import com.haier.core.constant.ServiceNameConstants;
import com.haier.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/28 21:32
 */
@FeignClient(contextId = "remoteBotService", value = ServiceNameConstants.BOT_SERVICE, fallbackFactory = BotFallbackFactory.class)
public interface RemoteBotService {

    @GetMapping(value = "/message/send")
    R<String> sendMessage(@RequestParam("message") String message);
}
