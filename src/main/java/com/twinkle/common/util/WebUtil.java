package com.twinkle.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * description: Web工具类
 *
 * @author ：King
 * @date ：2019/1/13 10:26
 */
public final class WebUtil {

	private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

	// 让工具类彻底不可以实例化
	private WebUtil() {
		throw new Error("工具类不可以实例化！");
	}

	/**
	 * 获取客户端IP地址。
	 * 
	 * @param request
	 *            http servlet request
	 * @return 客户端IP
	 * 
	 * @since 1.0.0
	 * @version 1.0.0
	 */
	public static String getRealIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 将json字符串返回给客户端。
	 * 
	 * @param response
	 *            {@linkplain HttpServletResponse}
	 * @param json
	 *            JSON字符串
	 * @param httpStatusCode
	 *            HTTP状态码
	 * 
	 * @since 1.0.0
	 * @version 1.0.0
	 */
	public static void writeJson(HttpServletResponse response, String json, int httpStatusCode) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		response.setHeader("Pragma", "no-cache"); // HTTP/1.0 caches might not
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setStatus(httpStatusCode);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(json);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	/**
	 * 
	 * getParams:获取请求中的参数. <br/> 
	 * 
	 * @author ZHUANGWEILIANG1 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getParams(ServletRequest request) {
		Map<String,String> params = new HashMap<String,String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		return params;
	}

}
