package com.haier.core.exception;

/**
 * 自定义异常
 *
 * @author
 */
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code;

    private final String message;

    public CustomException(String message) {
        this.message = message;
    }

    public CustomException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public CustomException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
