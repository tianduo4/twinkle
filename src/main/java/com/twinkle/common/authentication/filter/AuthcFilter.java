package com.twinkle.common.authentication.filter;

import com.alibaba.fastjson.JSON;
import com.twinkle.common.rest.ApiResponse;
import com.twinkle.common.util.WebUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


/**
 * 
 * ClassName: 用户过滤器 <br/> 
 * date: 2016年7月7日 下午5:51:14 <br/> 
 * 
 * @author ZHUANGWEILIANG1 
 * @version
 */
public class AuthcFilter extends UserFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
//         result = new ResponseResult(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "未经授权",new ResultData(""));
        ApiResponse result=ApiResponse.fail(401,"您没有权限访问！");
        // 401 Unauthorized
        WebUtils.writeJson(httpResponse, JSON.toJSONString(result), HttpStatus.OK.value());

        return false;
    }

}
