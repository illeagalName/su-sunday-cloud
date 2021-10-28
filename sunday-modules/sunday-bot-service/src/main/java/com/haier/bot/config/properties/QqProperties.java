package com.haier.bot.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.bot.config.properties
 * @ClassName: QqProperties
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/28 11:08
 * @Version: 1.0
 */
@Data
@ConfigurationProperties(prefix = "qq.config")
public class QqProperties {

    /**
     * 是否启动机器人
     */
    private Integer startStatus;

    /**
     * QQ账号
     */
    private String account;

    /**
     * QQ密码
     */
    private String password;

    /**
     * 代理端口，暂时用不到，不需要代理
     */
    private Integer sockPort;

    /**
     * 百度地图密钥，暂时用不到，还没那么深入
     */
    private String ak;

    private String directoryWindows;

    private String directoryLinux;

    private String directory;

    public String getDirectory() {
        String system = System.getProperty("os.name");
        if (system.toLowerCase().startsWith("win")) {
            return getDirectoryWindows();
        } else {
            return getDirectoryLinux();
        }
    }
}
