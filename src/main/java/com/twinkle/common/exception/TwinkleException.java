package com.twinkle.common.exception;

import lombok.Data;

/**
 * description: 通用异常
 *
 * @author ：King
 * @date ：2019/1/12 23:28
 */
@Data
public class TwinkleException extends RuntimeException {
    private static final long serialVersionUID = 3455708526465670030L;

    public TwinkleException(String msg) {
        super(msg);
    }
}
