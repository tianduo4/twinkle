package com.twinkle.config;

import com.twinkle.common.exception.TwinkleException;
import com.twinkle.common.rest.ApiResponse;
import org.apache.shiro.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * description: 全局异常处理
 * @author ：King
 * @date ：2019/1/13 0:26
 */
@RestControllerAdvice
public class ExceptionHandlerConfig {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

    /**
     * description: 捕捉shiro的异常
     * created by King on 2019/1/13 0:30.
     * @Param: ex
     * @return com.twinkle.common.rest.ApiResponse
     */
    @ExceptionHandler(ShiroException.class)
    public ApiResponse handle401() {
        return ApiResponse.fail(401,"您没有权限访问！");
    }

    /**
     * description: 捕获全局异常
     * created by King on 2019/1/13 0:30.
     * @Param: ex
     * @return com.twinkle.common.rest.ApiResponse
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse globalException(Throwable ex) {
        if(ex instanceof TwinkleException){
            return ApiResponse.fail(ex.getMessage());
        }
        logger.error("访问出错",ex);
        return ApiResponse.fail("访问出错，无法访问: " + ex.getMessage());
    }
}
