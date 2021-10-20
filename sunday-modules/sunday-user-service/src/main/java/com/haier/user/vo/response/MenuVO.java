package com.haier.user.vo.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/16 20:01
 */
@Data
public class MenuVO {
    private String redirect;
    private String name;
    private String component;
    private String path;
    private List<MenuVO> children = new ArrayList<>();
    private Meta meta;

    @Data
    public static class Meta {
        private String title;
        private String icon;
    }
}
