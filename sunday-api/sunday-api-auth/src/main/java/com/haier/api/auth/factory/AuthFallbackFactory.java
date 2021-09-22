package com.haier.api.auth.factory;

import com.haier.api.auth.RemoteAuthService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 20:48
 */
public class AuthFallbackFactory implements FallbackFactory<RemoteAuthService> {

    @Override
    public RemoteAuthService create(Throwable cause) {
        return null;
    }
}
