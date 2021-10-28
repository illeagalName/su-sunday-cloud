package com.haier.bot.config;

import com.haier.bot.config.properties.QqProperties;
import com.haier.bot.listener.EventListener;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.bot.config
 * @ClassName: QqBotConfig
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/28 10:54
 * @Version: 1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({QqProperties.class})
public class QqBotConfig {

    private final EventListener eventListener;

    public QqBotConfig(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Bean
    @ConditionalOnProperty(prefix = "qq.config", name = "startStatus", havingValue = "1")
    public Bot bot(QqProperties qqProperties) {
        Bot bot = BotFactory.INSTANCE.newBot(Long.parseLong(qqProperties.getAccount()), qqProperties.getPassword(), new BotConfiguration() {{
            setWorkingDir(new File(qqProperties.getDirectory()));
            fileBasedDeviceInfo(); // 使用 device.json 存储设备信息
            setProtocol(MiraiProtocol.ANDROID_PHONE); // 切换协议
            redirectNetworkLogToFile();
            redirectNetworkLogToDirectory();
        }});
        bot.login();
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");//设置https协议，解决SSL peer shut down incorrectly的异常
        log.info("======注册监听======");
        GlobalEventChannel.INSTANCE.registerListenerHost(eventListener);
        return bot;
    }
}
