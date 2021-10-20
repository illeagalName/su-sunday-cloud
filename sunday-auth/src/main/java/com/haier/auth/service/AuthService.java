package com.haier.auth.service;

import com.haier.api.user.domain.ClientVO;
import com.haier.auth.domain.LoginUser;
import com.haier.core.constant.CacheConstants;
import com.haier.core.domain.R;
import com.haier.api.user.RemoteUserService;
import com.haier.api.user.domain.UserVO;
import com.haier.core.util.*;
import com.haier.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.haier.core.constant.CacheConstants.DETAILS_CLIENT_ID;
import static com.haier.core.constant.CacheConstants.AUTHORIZATION_USER_TOKEN;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/21 21:23
 */
@Service
@Slf4j
public class AuthService {

    @Autowired
    RemoteUserService remoteUserService;

    @Autowired
    RedisService redisService;

    public R<Map<String, Object>> login(LoginUser loginUser) {
        // 判断loginUser的username和password的空值
        AssertUtils.anyNotEmpty("用户名/密码不能为空", loginUser.getUsername(), loginUser.getPassword());
        R<UserVO> userInfo = remoteUserService.getUserInfo(loginUser.getUsername(), loginUser.getPassword());
        UserVO data = userInfo.getData();
        AssertUtils.notEmpty(data, userInfo.getMsg());
        AssertUtils.isTrue(Objects.equals(data.getStatus(), 0), "账号已被停用");
        // 密码正确,创建token
        return R.success(createToken(data));
    }

    public R<Boolean> logout() {
        Long userId = SecurityUtils.getUserId();
        String clientId = SecurityUtils.getClientId();
        Boolean delete = redisService.delete(AUTHORIZATION_USER_TOKEN + clientId + ":" + userId);
        return R.success(delete);
    }


    private Map<String, Object> createToken(UserVO user) {
        // 获取加密盐
        String secret = SecurityUtils.generateSecurityCode();
        // 从header中获取clientId
        String clientId = ServletUtils.getHeader(DETAILS_CLIENT_ID);
        AssertUtils.notEmpty(clientId, "客户端标识不正确");
        ClientVO clientMap = redisService.getObject(CacheConstants.AUTHORIZATION_USER_CLIENT + clientId);
        AssertUtils.notEmpty(clientMap, "客户端标识不正确");
        // 生成token
        Date date = new Date(System.currentTimeMillis() + clientMap.getTime() * 1000);
        String token = SecurityUtils.createToken(new HashMap<>() {{
            put("uniqueId", user.getUserId() + "");
            put("clientId", clientId);
        }}, secret, date);

        user.setClientId(clientId);
        user.setSecret(secret);
        user.setToken(token);
        user.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        user.setLoginTime(LocalDateTime.now());
        user.setExpireTime(DateUtils.toLocalDateTime(date));
        // 保存或更新用户token
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        redisService.setObject(AUTHORIZATION_USER_TOKEN + clientId + ":" + user.getUserId(), user, clientMap.getTime() - 1, TimeUnit.SECONDS);
        return map;
    }
}
