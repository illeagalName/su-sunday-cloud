package com.haier.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/25 10:32
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "white")
public class WhiteListProperties {
    /**
     * 放行白名单配置，网关不校验此处的白名单
     */
    private List<String> lists;

    public List<String> getLists() {
        return lists;
    }

    public void setLists(List<String> lists) {
        this.lists = lists;
    }
}
