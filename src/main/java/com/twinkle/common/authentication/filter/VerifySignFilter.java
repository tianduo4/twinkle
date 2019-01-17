package com.twinkle.common.authentication.filter;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.twinkle.common.constant.ApiBaseConstant;
import com.twinkle.common.rest.ApiResponse;
import com.twinkle.common.util.SigntureUtil;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * description: 接口签名过滤器
 *
 * @author ：King
 * @date ：2019/1/13 9:04
 */
public class VerifySignFilter extends PathMatchingFilter {
    private static final Logger logger = LoggerFactory.getLogger(VerifySignFilter.class);
    /**
     * 是否开启签名验证
     */
    private boolean signtureVerificationEnable = true;
    /**
     * 关闭签名验证时需要传入debugToken参数
     */
    private String debugToken = "";

    public VerifySignFilter(boolean signtureVerificationEnable,String debugToken ){
        this.signtureVerificationEnable=signtureVerificationEnable;
        this.debugToken=debugToken;
    }

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        if (!verifySign(request)) {
            sendResponse(response, JSONObject.toJSONString(ApiResponse.fail("签名有误")));
            return false;
        }
        return super.onPreHandle(request, response, mappedValue);
    }

    private boolean verifySign(ServletRequest request) {
        String sign = request.getParameter(ApiBaseConstant.SIGN);
        String signPassToken = request.getParameter(ApiBaseConstant.DEBUG_TOKEN);
        Map<String, String> params = getParams(request);
        if (!signtureVerificationEnable && debugToken.equals(signPassToken == null ? "" : signPassToken)) {
            return true;
        }

        if (SigntureUtil.verify(params, sign)) {
            return true;
        } else {
            return false;
        }
    }

    private Map<String, String> getParams(ServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Set<Map.Entry<String, String[]>> keSet = map.entrySet();
        Map<String, String> params = Maps.newHashMap();
        for (Iterator<Map.Entry<String, String[]>> itr = keSet.iterator(); itr.hasNext(); ) {
            Map.Entry<String, String[]> me = (Map.Entry<String, String[]>) itr.next();
            Object ok = me.getKey();
            Object ov = me.getValue();
            String[] value = new String[1];
            if (ov instanceof String[]) {
                value = (String[]) ov;
            } else {
                value[0] = ov.toString();
            }
            for (int k = 0; k < value.length; k++) {
                params.put(ok.toString(), value[k]);
            }
        }
        return params;
    }


    private void sendResponse(ServletResponse response, String str) throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("Rest result=" + str);
        }
        response.setCharacterEncoding("gbk");
        PrintWriter out = response.getWriter();
        out.write(str);
        out.flush();
    }
}
