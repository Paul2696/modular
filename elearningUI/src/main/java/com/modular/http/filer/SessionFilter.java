package com.modular.http.filer;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

@WebFilter("/j_security_check")
public class SessionFilter implements Filter {

    private static int STUDENT = 2;
    private static int TEACHER = 1;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        Principal user = req.getUserPrincipal();
        if(user != null) {
            HttpServletResponse  res = (HttpServletResponse) servletResponse;
            //Cookie
            //res.addCookie();
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
