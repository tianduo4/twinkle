package com.twinkle.config;

import com.twinkle.common.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * description: 全局过滤器
 *
 * @author ：King
 * @date ：2019/1/13 9:04
 */
@Slf4j
@WebFilter(filterName = "globalFilter", urlPatterns = "/*")
public class GlobalFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long ts = System.currentTimeMillis();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (log.isInfoEnabled()) {
            log.info("####### Begin Request[" + httpRequest.getRequestURI() + "] Param:" + LogUtil.convert(request));
        }
        try {
            chain.doFilter(request, response);
        } finally {
            long te = System.currentTimeMillis();
            if (log.isInfoEnabled()) {
                log.info("####### Finish Request[" + httpRequest.getRequestURI() + "] Cost[" + (te - ts) + "ms]");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
