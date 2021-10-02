package com.haier.elasticsearch.exception;

import com.haier.core.exception.CustomException;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/1 18:19
 */
public class EsException extends CustomException {
    public EsException(String message) {
        super(message);
    }

    public EsException(Integer code, String message) {
        super(code, message);
    }

    public EsException(String message, Throwable e) {
        super(message, e);
    }
}
