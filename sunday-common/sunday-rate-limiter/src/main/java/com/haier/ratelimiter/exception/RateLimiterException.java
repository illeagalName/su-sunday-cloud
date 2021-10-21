package com.haier.ratelimiter.exception;

import com.haier.core.exception.CustomException;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/21 22:04
 */
public class RateLimiterException extends CustomException {
    public RateLimiterException(String message) {
        super(message);
    }

    public RateLimiterException(Integer code, String message) {
        super(code, message);
    }

    public RateLimiterException(String message, Throwable e) {
        super(message, e);
    }
}
