package com.twinkle.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

//import com.alibaba.fastjson.serializer.StringSerializer;
//import com.pingan.mamc.security.SecurityUtil;
//import org.apache.log4j.NDC;

/**
 * description: 日志辅助类
 *
 * @author ：King
 * @date ：2019/1/13 9:23
 */
public class LogUtil {
    private static final Log logger = LogFactory.getLog(LogUtil.class);

    /**
     * 将对象转换成JSON字符串
     *
     * @param obj 待转换对象
     *            是否脱敏(不支持DTO)
     * @return JSON字符串
     */
    public static String convert(Object obj) {
        if (obj == null) {
            return "NULL";
        } else if (obj instanceof ServletRequest) {
            ServletRequest request = (ServletRequest) obj;
            return convert(request.getParameterMap());
        } else {
            return JSONObject.toJSONString(obj);
        }
    }

//    public static void setLogHeader(HttpServletRequest httpRequest){
//        try {
//            String appName = httpRequest.getParameter("appName");
//            if (StringUtils.isEmpty(appName)) {
//                appName = httpRequest.getParameter("mamcAppId");
//            }
//            if (StringUtils.isEmpty(appName)) {
//                appName = httpRequest.getParameter("appId");
//            }
//            if (StringUtils.isEmpty(appName)) {
//                appName = "UNKNOWN";
//            }
//            appName = SecurityUtil.fixHttpRS(appName);
//            String ipAddr = getIpAddress(httpRequest);
//            NDC.push("IP=" + ipAddr + ">");
//        } catch (Exception e) {
//            if(logger.isInfoEnabled()) {
//                logger.info("setLogHeader fail", e);
//            }
//        }
//    }

    public static String getIpAddress(HttpServletRequest httpRequest) {
        String ipAddr = httpRequest.getHeader("X-Real-IP");
        if (StringUtils.isEmpty(ipAddr)) {
            ipAddr = httpRequest.getRemoteAddr();
        }
        return ipAddr;
    }
}
