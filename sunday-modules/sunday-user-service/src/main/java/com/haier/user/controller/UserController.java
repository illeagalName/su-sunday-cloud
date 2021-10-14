package com.haier.user.controller;

import com.haier.core.domain.R;
import com.haier.core.util.SecurityUtils;
import com.haier.user.service.UserService;
import com.haier.api.user.domain.UserVO;
import com.haier.user.vo.request.RegisterUserVO;
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

    @PostMapping("register")
    public R<Boolean> registerUser(@RequestBody RegisterUserVO request) {
        return R.success(userService.registerUser(request));
    }

    @GetMapping("routes")
    public R<?> listRoutes() {
        Long userId = SecurityUtils.getUserId();
        log.info("当前用户id {}", userId);
        List<Map<String, Object>> routes = new ArrayList<>();
        Map<String, Object> m1 = new HashMap<>();
        m1.put("path", "/writing-demo");
        m1.put("meta", new HashMap<>() {{
            put("title", "Writing Demo");
            put("icon", "eye-open");
        }});
        m1.put("component", "Layout");
        m1.put("alwaysShow", true);
        routes.add(m1);


        List<Map<String, Object>> children = new ArrayList<>();

        m1.put("children", children);

        Map<String, Object> m2 = new HashMap<>();
        m2.put("path", "hook");
        m2.put("meta", new HashMap<>() {{
            put("title", "Hook-Demo");
        }});
        m2.put("component", "views/example/hook/Hook");
        m2.put("name", "Hook");

        children.add(m2);

        Map<String, Object> m3 = new HashMap<>();
        m3.put("path", "uex-use");
        m3.put("meta", new HashMap<>() {{
            put("title", "Vuex-Demo");
        }});
        m3.put("component", "views/example/vuex-use/VuexUse");
        m3.put("name", "VuexUse");

        children.add(m3);

        return R.success(routes);
    }
}
