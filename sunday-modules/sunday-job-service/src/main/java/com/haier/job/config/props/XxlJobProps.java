package com.haier.job.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.job.config.props
 * @ClassName: XxlJobProps
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/22 10:57
 * @Version: 1.0
 */
@Data
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProps {

    /**
     * 调度中心配置
     */
    private XxlJobAdminProps admin;

    /**
     * 执行器配置
     */
    private XxlJobExecutorProps executor;

    /**
     * 与调度中心交互的accessToken
     */
    private String accessToken;

    @Data
    public static class XxlJobAdminProps {
        /**
         * 调度中心地址
         */
        private String address;
    }

    @Data
    public static class XxlJobExecutorProps {
        /**
         * 执行器名称
         */
        private String appName;

        /**
         * 执行器 IP
         */
        private String ip;

        /**
         * 执行器端口
         */
        private int port;

        /**
         * 执行器日志
         */
        private String logPath;

        /**
         * 执行器日志保留天数，-1
         */
        private int logRetentionDays;
    }
}
