package com.haier.user.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.haier.core.domain.R;
import com.haier.core.util.JsonUtils;
import com.haier.core.util.SecurityUtils;
import com.haier.user.service.UserService;
import com.haier.api.user.domain.UserVO;
import com.haier.user.vo.request.RegisterUserVO;
import com.haier.user.vo.response.RouteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("info")
    public R<?> getUserInfo1() {
        String username = SecurityUtils.getUsername();
        String data = "{\"roles\":[\"admin\"],\"introduction\":\"I am a super administrator\",\"avatar\":\"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif\",\"name\":\"Super Admin\"}";
        Map<String, Object> stringObjectMap = JsonUtils.toObject(data, new TypeReference<Map<String, Object>>() {
        });
        return R.success(stringObjectMap);
//        return R.success(userService.selectUserByUserName(username));
    }

    @PostMapping("register")
    public R<Boolean> registerUser(@RequestBody RegisterUserVO request) {
        return R.success(userService.registerUser(request));
    }

    @GetMapping("roles")
    public R<?> listRoles() {
        List<String> roles = new ArrayList<>();
        roles.add("consumer");
        return R.success(roles);
    }

    @GetMapping("menus")
    public R<?> listPermissions() {
        List<RouteVO> routes = userService.listRoutes();
        return R.success(routes);
    }
}
