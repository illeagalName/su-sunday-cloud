package com.haier.job.config;

import com.haier.job.config.props.XxlJobProps;
import com.haier.job.config.props.XxlJobUser;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.job.config
 * @ClassName: XxlJobConfig
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/22 11:01
 * @Version: 1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({XxlJobProps.class, XxlJobUser.class})
public class XxlJobConfig {

    @Resource
    XxlJobProps props;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(props.getAdmin().getAddress());
        xxlJobSpringExecutor.setAppname(props.getExecutor().getAppName());
        xxlJobSpringExecutor.setIp(props.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(props.getExecutor().getPort());
        xxlJobSpringExecutor.setAccessToken(props.getAccessToken());
        xxlJobSpringExecutor.setLogPath(props.getExecutor().getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(props.getExecutor().getLogRetentionDays());

        return xxlJobSpringExecutor;
    }


}
