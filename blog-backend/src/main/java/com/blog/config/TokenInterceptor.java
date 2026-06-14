package com.blog.config;

import com.blog.common.Result;
import com.blog.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 放行 OPTIONS（CORS 预检由 WebConfig 统一处理）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 放行登录、注册
        String uri = request.getRequestURI();
        if (uri.contains("/api/login") || uri.contains("/api/register")) {
            return true;
        }

        // 公开 GET（文章、分类、标签、评论）
        if ("GET".equalsIgnoreCase(request.getMethod()) &&
            (uri.contains("/api/article") || uri.contains("/api/category") ||
             uri.contains("/api/tag") || uri.contains("/api/comment"))) {
            return true;
        }

        // 获取 token
        String token = request.getHeader("token");
        if (token == null || token.trim().isEmpty()) {
            response.setContentType("application/json;charset=utf-8");
            new ObjectMapper().writeValue(response.getWriter(), Result.fail("未登录或token无效"));
            return false;
        }

        // 校验 token，通过后将用户信息写入 request
        try {
            Long userId = jwtUtil.getUserId(token);
            String role = jwtUtil.getRole(token);
            String username = jwtUtil.getUsername(token);
            request.setAttribute("userId", userId);
            request.setAttribute("role", role);
            request.setAttribute("username", username);
            return true;
        } catch (Exception e) {
            response.setContentType("application/json;charset=utf-8");
            new ObjectMapper().writeValue(response.getWriter(), Result.fail("token无效或已过期"));
            return false;
        }
    }
}
