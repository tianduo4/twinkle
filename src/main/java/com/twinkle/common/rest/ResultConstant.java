package com.twinkle.common.rest;

/**
 * description: 结果常量
 *
 * @author ：King
 * @date ：2019/1/12 23:32
 */
public interface ResultConstant {

    int  SUCCEED_CODE= 0;

    int  FAILED_CODE= -1;

    int  PARAM_ERROR_CODE= 1;

    String SUCCEED = "成功";

    String FAILED  = "失败";

    String ERROR  = "系统错误";

    String UNAUTHORIZED  = "未授权";

    String PARAM_ERROR  = "参数错误";
}
