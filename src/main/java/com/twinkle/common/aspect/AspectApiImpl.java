package com.twinkle.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * description: 基本被装饰类,做一些公共处理
 * @author ：King
 * @date ：2019/1/12 16:27
 */
public class AspectApiImpl implements AspectApi{

    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable {
        return null;
    }
}