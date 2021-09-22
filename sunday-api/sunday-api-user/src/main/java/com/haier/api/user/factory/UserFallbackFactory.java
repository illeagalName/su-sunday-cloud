package com.haier.api.user.factory;

import com.haier.core.domain.R;
import com.haier.api.user.RemoteUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 20:48
 */
@Slf4j
public class UserFallbackFactory implements FallbackFactory<RemoteUserService> {

    @Override
    public RemoteUserService create(Throwable cause) {
        log.error("获取用户信息失败: {}", cause.getMessage());
        return username -> R.error("获取用户信息失败");
    }
}
