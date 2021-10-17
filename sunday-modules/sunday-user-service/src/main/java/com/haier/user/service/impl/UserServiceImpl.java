package com.haier.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.haier.core.util.AssertUtils;
import com.haier.core.util.HttpUtils;
import com.haier.core.util.SecurityUtils;
import com.haier.user.dao.MenuMapper;
import com.haier.user.dao.RoleMapper;
import com.haier.user.dao.UserMapper;
import com.haier.user.domain.Menu;
import com.haier.user.domain.Role;
import com.haier.user.domain.User;
import com.haier.user.service.UserService;
import com.haier.api.user.domain.UserVO;
import com.haier.user.vo.request.RegisterUserVO;
import com.haier.user.vo.response.RouteVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 20:55
 */
@Service
@RefreshScope
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    MenuMapper menuMapper;

    @Value("${remote.service}")
    String remoteServiceUrl;

    @Override
    public UserVO selectUserByUserName(String username) {
        User user = userMapper.selectUserByUserName(username);
        AssertUtils.notEmpty(user, "不存在用户：" + username);
        // 先用BeanUtil 后面改为MapStruct
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        List<Role> roles = roleMapper.listRolesByUserId(user.getUserId());
        vo.setRoleIds(roles.stream().map(Role::getRoleId).collect(Collectors.toList()));
        return vo;
    }

    @Override
    public Boolean registerUser(RegisterUserVO request) {
        // 根据用户名先查询是否存在
        User u = userMapper.selectUserByUserName(request.getUserName());
        AssertUtils.isTrue(Objects.isNull(u), "用户名已存在");

        User user = new User();
        BeanUtils.copyProperties(request, user);
        // 密码加密
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return userMapper.insert(user) > 0;
    }

    @Override
    public List<RouteVO> listRoutes() {
        // 严格排序
        List<Menu> menus = menuMapper.listMenus();

        Map<Long, RouteVO> temp = new HashMap<>();

        List<RouteVO> result = new ArrayList<>();
        menus.forEach(item -> {
            RouteVO vo = new RouteVO();
            vo.setPath(item.getPath());
            vo.setComponent(item.getComponent());
            vo.setRedirect(item.getRedirect());
            vo.setName(item.getMenuName());
            RouteVO.Meta meta = new RouteVO.Meta();
            meta.setIcon(item.getIcon());
            meta.setTitle(Objects.isNull(item.getMenuTitle()) ? item.getMenuName() : item.getMenuTitle());
            vo.setMeta(meta);

            Long parentId = item.getParentId();
            if (Objects.equals(parentId, 0L)) {
                result.add(vo);
            } else {
                RouteVO routeVO = temp.get(parentId);
                routeVO.getChildren().add(vo);
            }
            temp.put(item.getMenuId(), vo);
        });
        return result;
    }

    @Override
    public Object todayElectricity() {
        String url = remoteServiceUrl + "/system/use/groupByPeriod";
        Map<String, Object> params = new HashMap<>();
        params.put("buildingId", 1);
        params.put("period", 1);
        params.put("energyType", 1);
        String s = HttpUtils.doGet(url, params);
        JSONObject jsonObject = JSONObject.parseObject(s);
        return jsonObject.getJSONArray("data");
    }

    @Data
    public static class UsedVO {
        private String subscript;
        private BigDecimal used;
    }
}
