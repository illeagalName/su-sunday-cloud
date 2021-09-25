package com.haier.core.handler;

import com.haier.core.domain.R;
import com.haier.core.exception.BaseException;
import com.haier.core.exception.CustomException;
import com.haier.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 21:58
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    /**
     * 基础异常
     */
    @ExceptionHandler(BaseException.class)
    public R<String> baseException(BaseException e) {
        return R.error(e.getDefaultMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public R<String> businessException(CustomException e) {
        if (StringUtils.isNull(e.getCode())) {
            return R.error(e.getMessage());
        }
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<String> handleException(Exception e) {
        String s = Optional.ofNullable(e).map(Exception::getMessage).orElse("系统异常");
        log.error(s);
        return R.error(s);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public R<String> validatedBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return R.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return R.error(message);
    }

}
