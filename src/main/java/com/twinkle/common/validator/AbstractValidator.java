package com.twinkle.common.validator;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * description: 通用校验抽象类,放校验辅助方法
 *
 * @author ：King
 * @date ：2019/1/12 23:28
 */
public abstract class AbstractValidator {

    public static <T> boolean notNull(T value) {
        return value != null;
    }

    public static <T> boolean notEmpty(T[] value) {
        return ArrayUtils.isNotEmpty(value);
    }

    public static <T extends Collection<?>> boolean notEmpty(T value) {
        return !(value == null || value.isEmpty());
    }

    public static <T extends Map<?, ?>> boolean notEmpty(T value) {
        return !(value == null || value.isEmpty());
    }

    public static <T extends CharSequence> boolean notEmpty(T value) {
        return StringUtils.isNotEmpty(value);
    }

    public static boolean maxLength(String value, int maxLength) {
        int len = StringUtils.length(value);
        return len <= maxLength;
    }

    public static boolean range(String value, int minLength, int maxLength) {
        int len = StringUtils.length(value);
        return len >= minLength && len <= maxLength;
    }

    public static boolean range(Integer value, int min, int max) {
        if (value == null) {
            return true;
        }
        return value >= min && value <= max;
    }

    public static Boolean isNotNULL(Object... value) {
        if (null == value || value.length < 1) {
            return false;
        } else {
            for (int i = 0; i < value.length; i++) {
                if (null == value[i] || "".equals(value[i].toString()) || "null".equals(value[i].toString().toLowerCase())) {
                    return false;
                }
            }
        }
        return true;
    }
}