package com.haier.bot.util;

import cn.hutool.json.JSONObject;
import com.haier.core.util.HttpUtils;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.bot.util
 * @ClassName: MlyUtil
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/11/20 13:01
 * @Version: 1.0
 */
public class MlyUtil {


    /**
     * @param content  消息主体，跟机器人交互的文本（长度超过64个字符将被自动截取，只保留前64个字符）
     * @param type     对话场景，1：私聊，2：群聊（对话场景不一样，from和to参数的含义也不一样）
     * @param from     消息发送者标识符（ID）：群消息时，此值表示群成员；好友消息时，此值表示好友。（长度超过32个字符将被自动截取）
     * @param fromName 消息发送者名字或昵称：群消息时，此值表示群成员；好友消息时，此值表示好友。（长度超过32个字符将被自动截取）
     * @param to       消息接收者标识符（ID）：群消息时此值表示群标识；好友消息时此值不用传，并且无效。（长度超过32个字符将被自动截取）
     * @param toName   消息接收者名字或昵称：群消息时此值表示群名；好友消息时此值不用传，并且无效。（长度超过32个字符将被自动截取）
     * @return
     */
    public static String AiChat(String content, Integer type, Long from, String fromName, Long to, String toName) {
        JSONObject body = new JSONObject();
        // 发送的内容
        body.set("content", content);
        // 消息类型，1：私聊，2：群聊
        body.set("type", type);
        body.set("from", from);
        body.set("fromName", fromName);
        body.set("to", to);
        body.set("toName", toName);
        RequestBody requestBody = RequestBody.create(body.toString(), MediaType.parse("application/json; charset=utf-8"));
        // 构建请求头
        Headers headers = new Headers.Builder().set("Content-Type", "application/json; charset=utf-8").add("Api-Key", "wz1ixrwl9g4fnf6v").add("Api-Secret", "n0njzobk").build();
        Request request = new Request.Builder()
                .url("https://i.mly.app/reply").headers(headers)
                .post(requestBody).build();
        try {
            ResponseBody responseBody = HttpUtils.COMMON_CLIENT.newCall(request).execute().body();
            if (Objects.nonNull(responseBody)) {
                return responseBody.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
