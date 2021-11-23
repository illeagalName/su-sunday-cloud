package com.haier.bot.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haier.bot.config.properties.QqProperties;
import com.haier.bot.service.BotService;
import com.haier.bot.util.MlyUtil;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.bot.listener
 * @ClassName: EventListener
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/28 11:59
 * @Version: 1.0
 */
@Slf4j
@Component
public class EventListener extends SimpleListenerHost {

    @Resource
    BotService botService;

    @Resource
    QqProperties qqProperties;

    @EventHandler()
    public ListeningStatus onNewFriendRequest(NewFriendRequestEvent event) {
        String fromNick = event.getMessage();
        if (fromNick.equals("1233211234567")) {
            event.accept();
            log.info("添加好友" + event.getFromId());
        } else {
            log.info("添加好友失败,暗号没对上");
        }
        return ListeningStatus.LISTENING;
    }

    @EventHandler()
    public ListeningStatus onBotInvitedJoinGroupRequest(BotInvitedJoinGroupRequestEvent event) {

        return ListeningStatus.LISTENING;
    }

    @EventHandler()
    public ListeningStatus onBotJoinGroup(BotJoinGroupEvent event) {

        return ListeningStatus.LISTENING;
    }

    @EventHandler()
    public ListeningStatus onFriendMessage(FriendMessageEvent event) {
        log.info("收到好友消息" + event.getFriend().getId());
        event.getFriend().sendMessage(new PlainText("只支持群聊"));
        return ListeningStatus.LISTENING;
    }

    @NotNull
    @EventHandler
    public ListeningStatus getGroupMessage(@NotNull GroupMessageEvent event) {
        String message = event.getMessage().contentToString();
        Pair<Boolean, String> atMe = isAtMe(message);
        if (atMe.getLeft()) {
            String content = atMe.getRight();
            long from = event.getSender().getId();
            String fromName = event.getSenderName();
            Group group = event.getGroup();
            long to = group.getId();
            String toName = group.getName();
            String s = MlyUtil.AiChat(content, 2, from, fromName, to, toName);
            JSONObject jsonObject = JSON.parseObject(s);
            String msg;
            if (Objects.equals(jsonObject.getString("code"), "00000")) {
                JSONArray data = jsonObject.getJSONArray("data");
                Object o = data.get(0);
                msg = JSON.parseObject(JSON.toJSONString(o)).getString("content");
            } else {
                msg = "你说的啥，风太大，没听见~";
            }
            botService.sendChatMessage(to, from, msg);
            return ListeningStatus.LISTENING;
        }
        String[] split = message.split("\\s+");
        String command = split.length > 0 ? split[0] : "";
        switch (command) {
            case "菜单":
//                event.getSubject().sendMessage(new PlainText(menu));
                break;
            case "天气":
                if (split.length != 1) {
                    botService.getWeatherInfo(event.getGroup().getId(), split[1]);
                }
                break;
            case "笑话":
                botService.getJokeInfo(event.getGroup().getId());
                break;
            case "风险地区":
                botService.COVID_19(event.getGroup().getId());
                break;
            case "买家秀":
                botService.sendShowToGroupId(event.getGroup().getId());
                break;
        }
        return ListeningStatus.LISTENING;
    }

    @NotNull
    @EventHandler
    public void messagePostSend(@NotNull GroupMessagePostSendEvent event) throws Exception {
        String s = event.getMessage().contentToString();
        if (s.startsWith("看多了伤身体")) {
            MessageReceipt<Group> receipt = event.getReceipt();
            if (Objects.nonNull(receipt)) {
                receipt.recallIn(20000);
            }
        }
    }

    @NotNull
    @EventHandler
    public void getBotOffline(@NotNull BotOfflineEvent event) throws Exception {
        log.error("掉了");
    }


    @NotNull
    @EventHandler
    public void getBotOffline(@NotNull BotReloginEvent event) throws Exception {
        log.error("重新登录");
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        // 处理事件处理时抛出的异常
        log.error("异常" + context + "\n原因:" + exception.toString());
        throw new RuntimeException("在事件处理中发生异常", exception);
    }

    private String menu =
            "1.天气 城市\n" +
                    "2.笑话\n" +
                    "注意：不需要笑话，直接汉字命令\n";


    private Pair<Boolean, String> isAtMe(String message) {
        String atMe = "@" + qqProperties.getAccount() + " ";
        boolean b = message.startsWith(atMe);
        if (b) {
            return Pair.of(true, message.replace(atMe, ""));
        }
        return Pair.of(false, null);
    }
}
