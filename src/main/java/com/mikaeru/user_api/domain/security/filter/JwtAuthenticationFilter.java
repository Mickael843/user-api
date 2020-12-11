package com.mikaeru.user_api.domain.security.filter;

import com.mikaeru.user_api.domain.service.JwtTokenAuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = new JwtTokenAuthenticationService()
                .getAuthentication((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);

        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
