package com.haier.user.runner;

import com.haier.api.user.domain.ClientVO;
import com.haier.api.user.domain.MenuVO;
import com.haier.api.user.domain.RoleVO;
import com.haier.core.constant.CacheConstants;
import com.haier.redis.service.RedisService;
import com.haier.user.dao.AuthClientMapper;
import com.haier.user.dao.MenuMapper;
import com.haier.user.dao.RoleMapper;
import com.haier.user.domain.AuthClient;
import com.haier.user.domain.Menu;
import com.haier.user.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.user.runner
 * @ClassName: RolePermPreloadRunner
 * @Author: yangwendong
 * @Description: 系统启动就缓存角色和菜单数据，人和角色单独缓存，这样以后更新角色信息时，人只要每次查询角色缓存即可拿到最新的角色菜单数据
 * @Description: 预加载 客户端信息
 * @Date: 2021/9/23 14:33
 * @Version: 1.0
 */
@Component
@Slf4j
public class ResourcePreloadRunner implements ApplicationRunner {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    AuthClientMapper authClientMapper;

    @Autowired
    RedisService redisService;

    @Override
    public void run(ApplicationArguments args) {
        clearCache(CacheConstants.AUTHORIZATION_USER_ROLE + "*");
        log.info("加载角色菜单资源开始 {}", LocalDateTime.now());
        List<Role> roles = roleMapper.listRoles();
        roles.parallelStream().forEach(item -> {
            List<Menu> menus = menuMapper.listMenusByRoleId(item.getRoleId());
            RoleVO role = new RoleVO();
            BeanUtils.copyProperties(item, role);
            role.setMenus(menus.stream().map(Menu::getSymbol).filter(Objects::nonNull).collect(Collectors.toList()));
            // 缓存起来
            redisService.setObject(CacheConstants.AUTHORIZATION_USER_ROLE + role.getRoleId(), role);
        });

        clearCache(CacheConstants.AUTHORIZATION_USER_MENU + "*");
        List<Menu> menus = menuMapper.listMenus();
        menus.forEach(item -> {
            MenuVO menu = new MenuVO();
            BeanUtils.copyProperties(item, menu);
            redisService.setObject(CacheConstants.AUTHORIZATION_USER_MENU + item.getMenuId(), menu);
        });

        clearCache(CacheConstants.AUTHORIZATION_USER_CLIENT + "*");
        log.info("加载client资源开始 {}", LocalDateTime.now());
        List<AuthClient> authClients = authClientMapper.listAuthClients();
        authClients.stream().map(client -> {
            ClientVO clientVO = new ClientVO();
            clientVO.setClientId(client.getClientId());
            clientVO.setTime(client.getAccessTokenValidity());
            clientVO.setRemark(client.getRemark());
            return clientVO;
        }).forEach(m -> {
            redisService.setObject(CacheConstants.AUTHORIZATION_USER_CLIENT + m.getClientId(), m);
        });

        log.info("加载资源结束 {}", LocalDateTime.now());
    }

    private void clearCache(String pattern) {
        List<String> keys = redisService.keys(pattern);
        keys.forEach(key -> {
            redisService.delete(key);
        });
    }
}
