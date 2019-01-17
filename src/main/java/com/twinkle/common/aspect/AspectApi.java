package com.twinkle.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * description: 切面类
 *
 * @author ：King
 * @date ：2019/1/12 16:27
 */
public interface AspectApi {
    Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable;
}
