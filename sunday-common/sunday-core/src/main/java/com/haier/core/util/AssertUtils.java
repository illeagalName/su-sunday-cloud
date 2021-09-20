package com.haier.core.util;

import com.haier.core.exception.CustomException;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 22:09
 */
public class AssertUtils {
    /**
     * 如果为空,会抛出参数错误的异常
     *
     * @param object  需要判断为空的对象
     * @param message 提示的错误信息
     * @see CustomException
     */
    public static void notEmpty(Object object, String message) {
        if (DataUtils.isEmpty(object)) {
            throw new CustomException(message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new CustomException(message);
        }
    }
}
