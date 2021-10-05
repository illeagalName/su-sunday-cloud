package com.haier.user.controller;

import com.haier.core.domain.R;
import com.haier.core.util.SecurityUtils;
import com.haier.user.service.UserService;
import com.haier.api.user.domain.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/12 21:04
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("info/{username}")
    public R<UserVO> getUserInfo(@PathVariable("username") String username) {
        String clientId = SecurityUtils.getClientId();
        Long userId = SecurityUtils.getUserId();
        String username1 = SecurityUtils.getUsername();
        log.info("clientId = {} , userId = {} , username = {}", clientId, userId, username1);
        return R.success(userService.selectUserByUserName(username));
    }

}
