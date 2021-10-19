package com.haier.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.core.util.AssertUtils;
import com.haier.core.util.HttpUtils;
import com.haier.core.util.SecurityUtils;
import com.haier.redis.service.RedisService;
import com.haier.user.dao.MenuMapper;
import com.haier.user.dao.RoleMapper;
import com.haier.user.dao.UserMapper;
import com.haier.user.domain.Menu;
import com.haier.user.domain.Role;
import com.haier.user.domain.User;
import com.haier.user.service.UserService;
import com.haier.api.user.domain.UserVO;
import com.haier.user.vo.request.RegisterUserVO;
import com.haier.user.vo.response.PersonalInfoVO;
import com.haier.user.vo.response.MenuVO;
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

import static com.haier.core.constant.CacheConstants.AUTHORIZATION_USER_TOKEN;


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

    @Autowired
    RedisService redisService;

    @Value("${remote.service}")
    String remoteServiceUrl;

    @Override
    public UserVO selectUserByUserName(String username, String password) {
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.select("user_id,password,user_name,nick_name,email,phone,sex,avatar,status").eq("user_name", username);
        User user = userMapper.selectOne(userWrapper);
        AssertUtils.notEmpty(user, "不存在用户：" + username);
        AssertUtils.isTrue(SecurityUtils.matchesPassword(password, user.getPassword()), "密码不正确");
        // 先用BeanUtil 后面改为MapStruct
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        List<Role> roles = roleMapper.listRolesByUserId(user.getUserId());
        vo.setRoles(roles.stream().map(Role::getSymbol).collect(Collectors.toList()));
        return vo;
    }

    @Override
    public PersonalInfoVO getPersonalInfo() {
        Long userId = SecurityUtils.getUserId();
        String clientId = SecurityUtils.getClientId();
        UserVO userVO = redisService.getObject(AUTHORIZATION_USER_TOKEN + clientId + ":" + userId);
        PersonalInfoVO result = new PersonalInfoVO();
        BeanUtils.copyProperties(userVO, result);
        return result;
    }

    @Override
    public Boolean registerUser(RegisterUserVO request) {
        // 根据用户名先查询是否存在
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("username", request.getUserName());
        User u = userMapper.selectOne(userWrapper);
        AssertUtils.isTrue(Objects.isNull(u), "用户名已存在");

        User user = new User();
        BeanUtils.copyProperties(request, user);
        // 密码加密
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return userMapper.insert(user) > 0;
    }

    @Override
    public List<MenuVO> listMenus() {
        // 严格排序
        QueryWrapper<Menu> menuWrapper = new QueryWrapper<>();
        menuWrapper.orderByAsc("parent_id", "menu_sort");
        List<Menu> menus = menuMapper.selectList(menuWrapper);

        Map<Long, MenuVO> temp = new HashMap<>();

        List<MenuVO> result = new ArrayList<>();
        menus.forEach(item -> {
            MenuVO vo = new MenuVO();
            vo.setPath(item.getPath());
            vo.setComponent(item.getComponent());
            vo.setRedirect(item.getRedirect());
            vo.setName(item.getMenuName());
            MenuVO.Meta meta = new MenuVO.Meta();
            meta.setIcon(item.getIcon());
            meta.setTitle(Objects.isNull(item.getMenuTitle()) ? item.getMenuName() : item.getMenuTitle());
            vo.setMeta(meta);

            Long parentId = item.getParentId();
            if (Objects.equals(parentId, 0L)) {
                result.add(vo);
            } else {
                MenuVO routeVO = temp.get(parentId);
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
