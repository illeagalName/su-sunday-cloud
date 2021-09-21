package com.haier.user.controller;

import com.haier.core.domain.R;
import com.haier.user.service.UserService;
import com.haier.user.api.domain.UserVO;
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
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("info/{username}")
    public R<UserVO> getUserInfo(@PathVariable("username") String username) {
        return R.success(userService.selectUserByUserName(username));
    }
}
