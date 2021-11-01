package com.haier.bot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haier.core.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.bot.service
 * @ClassName: BotService
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/11/1 14:39
 * @Version: 1.0
 */
@Service
@RefreshScope
@Slf4j
public class BotService {

    @Value("${qq.config.weather}")
    String weatherUrl;

    @Value("${qq.config.joke}")
    String jokeUrl;

    @Resource
    Bot bot;

    @Resource
    ThreadPoolTaskExecutor taskExecutor;

    public void getWeatherInfo(Long groupId, String city) {
        CompletableFuture.runAsync(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("city", city);
            String s = HttpUtils.doGet(weatherUrl, params);
            JSONObject jsonObject = JSON.parseObject(s);
            log.info(jsonObject.toJSONString());
            if (jsonObject.getBooleanValue("success")) {
                JSONObject info = jsonObject.getJSONObject("info");
                StringBuilder sb = new StringBuilder();
                sb.append("城市：").append(city).append("\n")
                        .append("时间：").append(info.getString("date")).append("\n")
                        .append("天气：").append(info.getString("type")).append("\n")
                        .append("温度：").append(info.getString("high")).append(" ，").append(info.getString("low")).append("\n")
                        .append("风力：").append(info.getString("fengxiang")).append(" ，").append(info.getString("fengli")).append("\n")
                        .append("提示：").append(info.getString("tip"));
                bot.getGroup(groupId).sendMessage(new PlainText(sb.toString()));
            }
        }, taskExecutor);
    }

    public void getJokeInfo(Long groupId) {
        CompletableFuture.runAsync(() -> {
            String s = HttpUtils.doGet(jokeUrl);
            bot.getGroup(groupId).sendMessage(new PlainText(s));
        }, taskExecutor);
    }
}
