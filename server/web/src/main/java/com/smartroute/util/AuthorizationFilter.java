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
import java.util.Enumeration;

@WebFilter("/user/*")
public class AuthorizationFilter implements Filter {

    @Inject Logger logger;
    @Inject Authorization auth;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {    
        HttpServletRequest req = (HttpServletRequest) request;
        Enumeration attributeNames = req.getSession().getAttributeNames();
       while (attributeNames.hasMoreElements()) {
           Object nextElement = attributeNames.nextElement();
           logger.info(nextElement.toString());
       }
//        Authorization auth = (Authorization) req.getSession().getAttribute("auth");

        if (auth != null && auth.isLoggedIn()) {
            // User is logged in, so just continue request.
            chain.doFilter(request, response);
        } else {
            // User is not logged in, so redirect to index.
            HttpServletResponse res = (HttpServletResponse) response;
            res.sendRedirect(req.getContextPath() + "/login.html");
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

