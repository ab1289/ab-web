package com.abhishekbhalla.abweb.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class HeaderFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            LOGGER.info("Origin: " + httpServletRequest.getHeader("origin") + ", Referer: " + httpServletRequest.getHeader("referer"));
        }
    }

    @Override
    public void destroy() {

    }
}
