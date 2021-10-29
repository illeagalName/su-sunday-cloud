package com.haier.api.bot.factory;

import com.haier.api.bot.RemoteBotService;
import com.haier.core.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/28 21:32
 */
@Slf4j
public class BotFallbackFactory implements FallbackFactory<RemoteBotService> {

    @Override
    public RemoteBotService create(Throwable cause) {
        return message -> R.error("发送异常" + cause.getMessage());
    }
}
