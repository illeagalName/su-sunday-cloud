package com.haier.core.util;

import java.util.Collection;
import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 22:08
 */
public class DataUtils {
    public static boolean isEmpty(Object pObj) {
        if (pObj == null) {
            return true;
        }
        if (pObj == "") {
            return true;
        }
        if (pObj instanceof String) {
            return ((String) pObj).trim().length() == 0;
        } else if (pObj instanceof Collection<?>) {
            return ((Collection<?>) pObj).size() == 0;
        } else if (pObj instanceof Map<?, ?>) {
            return ((Map<?, ?>) pObj).size() == 0;
        } else if (pObj instanceof Object[]) {
            return ((Object[]) pObj).length == 0;
        } else if (pObj instanceof boolean[]) {
            return ((boolean[]) pObj).length == 0;
        } else if (pObj instanceof byte[]) {
            return ((byte[]) pObj).length == 0;
        } else if (pObj instanceof char[]) {
            return ((char[]) pObj).length == 0;
        } else if (pObj instanceof short[]) {
            return ((short[]) pObj).length == 0;
        } else if (pObj instanceof int[]) {
            return ((int[]) pObj).length == 0;
        } else if (pObj instanceof long[]) {
            return ((long[]) pObj).length == 0;
        } else if (pObj instanceof float[]) {
            return ((float[]) pObj).length == 0;
        } else if (pObj instanceof double[]) {
            return ((double[]) pObj).length == 0;
        }
        return false;
    }

    public static boolean isNotEmpty(Object pObj) {
        return !isEmpty(pObj);
    }
}
