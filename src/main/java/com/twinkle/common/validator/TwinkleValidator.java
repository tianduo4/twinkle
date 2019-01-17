package com.twinkle.common.validator;

import com.twinkle.common.exception.TwinkleException;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * description: 通用校验类
 *
 * @author ：King
 * @date ：2019/1/12 23:28
 */
public final class TwinkleValidator extends AbstractValidator {

    public static void isTrue(boolean flag, String message) {
        if (!flag) {
            throw new TwinkleException(message);
        }
    }

    public static void isFalse(boolean flag, String message) {
        if (flag) {
            throw new TwinkleException(message);
        }
    }

    public static void notLessThan(int value, int flag, String message) {
        if (value < flag) {
            throw new TwinkleException(message);
        }
    }

    public static void notMessThan(int value, int flag, String message) {
        if (value > flag) {
            throw new TwinkleException(message);
        }
    }

    public static <T> boolean notNull(T value, String message) {
        boolean isValid = notNull(value);
        if (!isValid) {
            throw new TwinkleException(message);
        }
        return isValid;
    }



    public static boolean isNotNumber(String input, String message) {
        if (!isNotNULL(input)) {
            return false;
        }
        boolean isValid = Pattern.matches("^\\d+$", input);
        if (!isValid) {
            throw new TwinkleException(message);
        }
        return isValid;
    }

    public static <T extends CharSequence> boolean notEmpty(T value, String message) {
        boolean isValid = notEmpty(value);
        if (!isValid) {
            throw new TwinkleException(message);
        }
        return isValid;
    }

    public static <T> boolean notEmpty(T[] value, String message) {
        boolean isValid = notEmpty(value);
        if (!isValid) {
            throw new TwinkleException(message);
        }
        return isValid;
    }

    public static <T extends Collection<?>> boolean notEmpty(T value, String message) {
        boolean isValid = notEmpty(value);
        if (!isValid) {
            throw new TwinkleException(message);
        }
        return isValid;
    }

    public static <T extends Map<?, ?>> boolean notEmpty(T value, String message) {
        boolean isValid = notEmpty(value);
        if (!isValid) {
            throw new TwinkleException(message);
        }
        return isValid;
    }

    public static boolean maxLength(String value, int maxLength, String message) {
        boolean isValid = maxLength(value, maxLength);
        if (!isValid) {
            throw new TwinkleException(message);
        }
        return isValid;
    }

    public static boolean range(String value, int minLength, int maxLength, String message) {
        boolean isValid = range(value, minLength, maxLength);
        if (!isValid) {
            throw new TwinkleException(message);
        }
        return isValid;
    }

    public static boolean range(Integer value, int min, int max, String message) {
        boolean isValid = range(value, min, max);
        if (!isValid) {
            throw new TwinkleException(message);
        }
        return isValid;
    }
}
