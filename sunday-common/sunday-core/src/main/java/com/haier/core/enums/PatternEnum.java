package com.haier.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.regex.Pattern;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/5 14:17
 */
@AllArgsConstructor
@Getter
@ToString
public enum PatternEnum {

    /**
     * 英文字母 、数字和下划线
     */
    GENERAL(Pattern.compile("^\\w+$")),

    /**
     * 数字
     */
    NUMBERS(Pattern.compile("\\d+")),

    /**
     * 英文单词
     */
    WORD(Pattern.compile("[a-zA-Z]+")),

    /**
     * 单个汉字
     */
    CHINESE(Pattern.compile("[\u4E00-\u9FFF]")),

    /**
     * 汉字
     */
    CHINESE_WORD(Pattern.compile("[\u4E00-\u9FFF]+")),

    /**
     * 电话
     */
    MOBILE(Pattern.compile("(?:0|86|\\+86)?1[3456789]\\d{9}")),

    /**
     * 身份证18位
     */
    CITIZEN_ID(Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)")),

    /**
     * 邮箱
     */
    MAIL(Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")),

    /**
     * emoji表情
     */
    EMOJI(Pattern.compile("[\\x{10000}-\\x{10ffff}\ud800-\udfff]")),

    /**
     * emoji表情编码格式
     */
    EMOJI_DECODE(Pattern.compile("\\[\\[EMOJI:(.*?)\\]\\]")),

    /**
     * 正则分组符号格式
     */
    GROUP_VAR(Pattern.compile("\\$(\\d+)"));;


    /**
     * 正则表达式
     */
    private Pattern pattern;
}
