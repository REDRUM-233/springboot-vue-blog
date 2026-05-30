package com.blog.config;

import com.blog.common.Result;
import com.blog.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 跨域头
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,token");

        // 放行 OPTIONS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 放行登录、用户列表
        String uri = request.getRequestURI();
        if (uri.contains("/api/login") || uri.contains("/api/users")) {
            return true;
        }

        // 获取 token
        String token = request.getHeader("token");
        if (token == null || token.trim().isEmpty()) {
            response.setContentType("application/json;charset=utf-8");
            new ObjectMapper().writeValue(response.getWriter(), Result.fail("未登录或token无效"));
            return false;
        }

        // 校验 token
        try {
            jwtUtil.getUserId(token);
            return true;
        } catch (Exception e) {
            response.setContentType("application/json;charset=utf-8");
            new ObjectMapper().writeValue(response.getWriter(), Result.fail("token无效或已过期"));
            return false;
        }
    }
}
