package com.haier.security.config;

import com.haier.core.constant.CacheConstants;
import com.haier.core.util.IpUtils;
import com.haier.core.util.ServletUtils;
import com.haier.core.util.StringUtils;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 21:27
 */
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            HttpServletRequest httpServletRequest = ServletUtils.getRequest();
            if (StringUtils.isNotNull(httpServletRequest)) {
                Map<String, String> headers = ServletUtils.getHeaders(httpServletRequest);
                // 传递用户信息请求头，防止丢失
                String userId = headers.get(CacheConstants.DETAILS_USER_ID);
                if (StringUtils.isNotEmpty(userId)) {
                    requestTemplate.header(CacheConstants.DETAILS_USER_ID, userId);
                }
                String userName = headers.get(CacheConstants.DETAILS_USERNAME);
                if (StringUtils.isNotEmpty(userName)) {
                    requestTemplate.header(CacheConstants.DETAILS_USERNAME, userName);
                }
                String authentication = headers.get(CacheConstants.AUTHORIZATION_HEADER);
                if (StringUtils.isNotEmpty(authentication)) {
                    requestTemplate.header(CacheConstants.AUTHORIZATION_HEADER, authentication);
                }

                // 配置客户端IP
                requestTemplate.header("X-Forwarded-For", IpUtils.getIpAddr(ServletUtils.getRequest()));
            }
        };
    }
}