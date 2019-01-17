package com.twinkle.common.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * description: 通用接口返回对象
 *
 * @author ：King
 * @date ：2019/1/12 23:00
 */
@Builder
@AllArgsConstructor
@Data
public class ApiResponse<T> implements Serializable {
    private int code;
    private String msg;
    private T result;

    public static ApiResponse ok() {
        return ApiResponse.builder().code(ResultConstant.SUCCEED_CODE).msg(ResultConstant.SUCCEED).build();
    }
    public static <T> ApiResponse ok(T result) {
        return ApiResponse.builder().code(ResultConstant.SUCCEED_CODE).msg(ResultConstant.SUCCEED).result(result).build();
    }

    public static ApiResponse fail() {
        return ApiResponse.builder().code(ResultConstant.FAILED_CODE).msg(ResultConstant.FAILED).build();
    }

    public static ApiResponse fail(int code) {
        return ApiResponse.builder().code(code).msg(ResultConstant.FAILED).build();
    }

    public static ApiResponse fail(String msg) {
        return ApiResponse.builder().code(ResultConstant.FAILED_CODE).msg(msg).build();
    }

    public static ApiResponse fail(int code, String msg) {
        return ApiResponse.builder().code(code).msg(msg).build();
    }
}
