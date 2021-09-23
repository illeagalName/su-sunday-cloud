package com.haier.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haier.auth.domain.LoginUser;
import com.haier.core.domain.R;
import com.haier.api.user.RemoteUserService;
import com.haier.api.user.domain.UserVO;
import com.haier.core.util.AssertUtils;
import com.haier.core.util.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public R<UserVO> login(LoginUser loginUser) {
        R<UserVO> userInfo = remoteUserService.getUserInfo(loginUser.getUsername());
        UserVO data = userInfo.getData();
        AssertUtils.notEmpty(data, userInfo.getMsg());
        
        // 判断空值和对比密码
        return userInfo;
    }
}
