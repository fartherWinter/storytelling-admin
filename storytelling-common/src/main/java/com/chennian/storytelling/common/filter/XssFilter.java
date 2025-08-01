package com.chennian.storytelling.common.filter;

import com.chennian.storytelling.common.xss.XssWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 安全过滤
 *
 * @author by chennian
 * @date 2023/3/14 15:46
 */
@Component
public class XssFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;


        logger.info("uri:{}", req.getRequestURI());
        // xss 过滤
        chain.doFilter(new XssWrapper(req), resp);
    }

    @Override
    public void destroy() {

    }
}
