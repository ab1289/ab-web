package com.abhishekbhalla.abweb.filter;


import com.abhishekbhalla.abweb.service.DynoRefreshService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
public class HeaderFilter implements Filter {
    @Autowired DynoRefreshService dynoRefreshService;

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        dynoRefreshService.setLastRequestTime();
        if(servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            LOGGER.info("Origin: " + httpServletRequest.getHeader("origin") + ", Referer: " + httpServletRequest.getHeader("referer"));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
