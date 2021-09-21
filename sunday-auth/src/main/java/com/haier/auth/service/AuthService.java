package com.haier.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haier.auth.domain.LoginUser;
import com.haier.core.domain.R;
import com.haier.user.api.RemoteUserService;
import com.haier.user.api.domain.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public R<UserVO> login(LoginUser loginUser){
        R<UserVO> userInfo = remoteUserService.getUserInfo(loginUser.getUsername());
        ObjectMapper ob = new ObjectMapper();
        try {
            log.info("======= {}", ob.writeValueAsString(userInfo));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
