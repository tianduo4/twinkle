package com.twinkle.common.annotation;

import java.lang.annotation.*;

/**
 * description: 在Controller方法上加入该注解不会验证身份
 *
 * @author ：King
 * @date ：2019/1/15 22:20
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface AuthIgnore {
}
