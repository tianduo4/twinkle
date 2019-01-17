package com.twinkle.config;

//import com.liugh.annotation.AccessLimit;
//import com.liugh.annotation.Log;
//import com.liugh.annotation.ParamXssPass;
//import com.liugh.annotation.ValidationParam;
//import com.liugh.util.ComUtil;
//import com.liugh.util.StringUtil;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;

import com.twinkle.common.annotation.AccessLimit;
import com.twinkle.common.aspect.AccessLimitAspect;
import com.twinkle.common.aspect.AspectApiImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * description: 切面:防止xss攻击 记录log  参数验证
 *
 * @author ：King
 * @date ：2019/1/12 16:24
 */
@Aspect
@Configuration
public class ControllerAspect {

    @Pointcut("execution(* com.twinkle.**.controller..*(..))  ")
    public void aspect() {
    }

    @Around(value = "aspect()")
    public Object validationPoint(ProceedingJoinPoint pjp)throws Throwable{
        Method method = currentMethod(pjp,pjp.getSignature().getName());
        //创建被装饰者
        AspectApiImpl aspectApi = new AspectApiImpl();

        //是否需要限流
        if (method.isAnnotationPresent(AccessLimit.class)) {
            new AccessLimitAspect(aspectApi).doHandlerAspect(pjp,method);
        }
        return  pjp.proceed(pjp.getArgs());
    }

    /**
     * 获取目标类的所有方法，找到当前要执行的方法
     */
    private Method currentMethod ( ProceedingJoinPoint joinPoint , String methodName ) {
        Method[] methods      = joinPoint.getTarget().getClass().getMethods();
        Method   resultMethod = null;
        for ( Method method : methods ) {
            if ( method.getName().equals( methodName ) ) {
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
    }
}
