package com.haier.bot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haier.bot.util.TextToImage;
import com.haier.core.util.DateUtils;
import com.haier.core.util.HttpUtils;
import com.haier.core.util.SecurityUtils;
import com.haier.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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

    @Value("${qq.config.covid}")
    String covidUrl;

    @Value("${qq.config.url}")
    String taoUrl;

    @Resource
    Bot bot;

    @Resource
    ThreadPoolTaskExecutor taskExecutor;

    @Resource
    RestTemplate restTemplate;

    @Resource
    RedisService redisService;

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
                sb.append("?????????").append(city).append("\n")
                        .append("?????????").append(info.getString("date")).append("\n")
                        .append("?????????").append(info.getString("type")).append("\n")
                        .append("?????????").append(info.getString("high")).append(" ???").append(info.getString("low")).append("\n")
                        .append("?????????").append(info.getString("fengxiang")).append(" ???").append(info.getString("fengli")).append("\n")
                        .append("?????????").append(info.getString("tip"));
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

    public void everyDayToReadWorld(Long groupId) {
        CompletableFuture.runAsync(() -> {
            Group group = bot.getGroup(groupId);
            try {
                URL url = new URL("https://api.vvhan.com/api/60s");
                URLConnection urlConnection = url.openConnection();
                byte[] bytes = urlConnection.getInputStream().readAllBytes();
                ExternalResource externalResource = ExternalResource.Companion.create(bytes);
                Image image = group.uploadImage(externalResource);
                group.sendMessage(image);
                externalResource.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, taskExecutor);
    }

    private String key = "sunday:bot:show";

    public void sendShowToGroupId(Long groupId) {
        Integer value = redisService.getObject(key);
        if (Objects.nonNull(value)) {
            bot.getGroup(groupId).sendMessage(new PlainText("??????????????????~"));
        } else {
            CompletableFuture.runAsync(() -> {
                Group group = bot.getGroup(groupId);
                try {
                    URL url = new URL(taoUrl);
                    URLConnection urlConnection = url.openConnection();
                    byte[] bytes = urlConnection.getInputStream().readAllBytes();
                    ExternalResource externalResource = ExternalResource.Companion.create(bytes);
                    Image image = group.uploadImage(externalResource);
                    MessageChain chain = new MessageChainBuilder().append(image).build();
                    group.sendMessage(chain);
                    externalResource.close();
                } catch (Exception e) {
                    group.sendMessage(new PlainText("oh~ ????????????~"));
                }
            }, taskExecutor);
            redisService.setObject(key, 1, 3L, TimeUnit.SECONDS);
        }
    }

    public void sendChatMessage(Long groupId, Long to, String message) {
        CompletableFuture.runAsync(() -> {
            bot.getGroup(groupId).sendMessage(new At(to).plus(new PlainText("\n")).plus(new PlainText(message)));
        }, taskExecutor);
    }

    public void COVID_19(Long groupId) {
        CompletableFuture.runAsync(() -> {
            String key = "sunday:covid19:area";
            JSONObject result = redisService.getObject(key);
            LocalDateTime now = LocalDateTime.now();
            if (Objects.isNull(result)) {
                log.info("????????????");
                //???????????????
                long timestamp = now.toEpochSecond(ZoneOffset.of("+8"));
                //??????????????????????????????????????????????????????------start
                final String STATE_COUNCIL_SIGNATURE_KEY = "fTN2pfuisxTavbTuYVSsNJHetwq5bJvCQkjjtiLM2dCratiA";
                final String STATE_COUNCIL_X_WIF_NONCE = "QkjjtiLM2dCratiA";
                final String STATE_COUNCIL_X_WIF_PAASID = "smt-application";
                //??????????????????????????? appId
                final String STATE_COUNCIL_APP_ID = "NcApplication";
                //??????????????????????????? PASSID
                final String STATE_COUNCIL_PASSID = "zdww";
                //??????????????????????????? ??????
                final String STATE_COUNCIL_NONCE = "123456789abcdefg";
                //??????????????????????????? token
                final String STATE_COUNCIL_TOEKN = "23y0ufFl5YxIyGrI8hWRUZmKkvtSjLQA";
                //??????????????????????????? key
                final String STATE_COUNCIL_KEY = "3C502C97ABDA40D0A60FBEE50FAAD1DA";

                HttpHeaders requestHeaders = new HttpHeaders();
                Map<String, Object> body = new HashMap<>(10);
                //??????????????????????????????
                String signatureStr = String.format("%d%s%d", timestamp, STATE_COUNCIL_SIGNATURE_KEY, timestamp);
                //????????????
                String signature = SecurityUtils.generateSHA256Str(signatureStr).toUpperCase();
                //?????????????????????
                requestHeaders.add("x-wif-nonce", STATE_COUNCIL_X_WIF_NONCE);
                requestHeaders.add("x-wif-paasid", STATE_COUNCIL_X_WIF_PAASID);
                requestHeaders.add("x-wif-signature", signature);
                requestHeaders.add("x-wif-timestamp", String.valueOf(timestamp));
                //body????????????
                body.put("appId", STATE_COUNCIL_APP_ID);
                body.put("paasHeader", STATE_COUNCIL_PASSID);
                body.put("timestampHeader", timestamp);
                body.put("nonceHeader", STATE_COUNCIL_NONCE);
                //????????????
                signatureStr = String.format("%d%s%s%d", timestamp, STATE_COUNCIL_TOEKN, STATE_COUNCIL_NONCE, timestamp);
                String signatureHeader = SecurityUtils.generateSHA256Str(signatureStr).toUpperCase();
                body.put("signatureHeader", signatureHeader);
                body.put("key", STATE_COUNCIL_KEY);
                HttpEntity<String> httpEntity = new HttpEntity(body, requestHeaders);
                ResponseEntity<String> response = restTemplate.exchange(covidUrl, HttpMethod.POST, httpEntity, String.class);
                JSONObject jsonObject = JSONObject.parseObject(response.getBody());
                if (jsonObject.getInteger("code") == 0) {
                    result = jsonObject.getJSONObject("data");
                }
            }
            if (Objects.nonNull(result)) {
                StringBuilder sb = new StringBuilder();

                sb.append("?????????").append(DateUtils.toString(now, "yyyy-MM-dd HH???")).append(":")
                        .append("\n\n").append("??????????????????").append(result.getInteger("hcount")).append("??????").append("\n");
                JSONArray highlist = result.getJSONArray("highlist");
                int i = 1;
                for (Object o : highlist) {
                    JSONObject obj = (JSONObject) o;
                    sb.append(i).append(". ").append(obj.getString("area_name")).append(" [");
                    JSONArray communitys = obj.getJSONArray("communitys");
                    int size = communitys.size();
                    for (int j = 0; j < size; j++) {
                        sb.append(communitys.get(j));
                        if (j != size - 1) {
                            sb.append("???");
                        }
                    }
                    sb.append("]");
                    sb.append("\n");
                    i++;
                }

                i = 1;
                sb.append("\n\n");
                sb.append("??????????????????").append(result.getInteger("mcount")).append("??????").append("\n");
                JSONArray middlelist = result.getJSONArray("middlelist");
                for (Object o : middlelist) {
                    JSONObject obj = (JSONObject) o;
                    sb.append(i).append(". ").append(obj.getString("area_name")).append(" [");
                    JSONArray communitys = obj.getJSONArray("communitys");
                    int size = communitys.size();
                    for (int j = 0; j < size; j++) {
                        sb.append(communitys.get(j));
                        if (j != size - 1) {
                            sb.append("???");
                        }
                    }
                    sb.append("]");
                    sb.append("\n");
                    i++;
                }
                redisService.setObject(key, result, 1L, TimeUnit.HOURS);
                // ????????????
                String path = "./a.jpg";
                if (TextToImage.createImage(sb.toString(), path)) {
                    Group group = bot.getGroup(groupId);
                    ExternalResource externalResource = ExternalResource.Companion.create(new File(path));
                    Image image = group.uploadImage(externalResource);
                    MessageReceipt<Group> groupMessageReceipt = group.sendMessage(image);
                    try {
                        externalResource.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                bot.getGroup(groupId).sendMessage(new PlainText("????????????????????????"));
            }
        }, taskExecutor);
    }

    public void takePills(Long to, String message) {
        CompletableFuture.runAsync(() -> {
            bot.getFriend(to).sendMessage(message);
        }, taskExecutor);
    }
}
