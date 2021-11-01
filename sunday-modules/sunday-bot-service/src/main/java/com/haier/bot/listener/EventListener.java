package com.haier.bot.listener;

import com.haier.bot.service.BotService;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

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
        }
        return ListeningStatus.LISTENING;
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

}
