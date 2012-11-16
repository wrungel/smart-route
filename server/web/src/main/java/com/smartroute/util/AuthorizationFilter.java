package com.smartroute.util;


import com.smartroute.controller.Authorization;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;

@WebFilter("/user/*")
public class AuthorizationFilter implements Filter {

    @Inject Logger logger;
    @Inject Authorization auth;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {    
        HttpServletRequest req = (HttpServletRequest) request;
        if (auth != null && auth.isLoggedIn()) {
            chain.doFilter(request, response);
        } else {
            String requestURI = req.getRequestURI();
            HttpServletResponse res = (HttpServletResponse) response;
            //res.sendRedirect(req.getContextPath() + "/login.jsf");
            res.sendRedirect(req.getContextPath() + "/login.jsf?from=" + URLEncoder.encode(requestURI, "UTF-8"));
        }
    }

    // override init() and destroy() as well, but they can be kept empty.
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    
}

