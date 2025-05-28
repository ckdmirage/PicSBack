package com.example.demo.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.model.entity.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


//用於維護權限:未登入/user/admin可以訪問的頁面
@Component
public class LoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = req.getRequestURI();

        // 白名單
        if (path.startsWith("/user/login") || path.startsWith("/user/register") || path.startsWith("/public")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = req.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;

        if (userObj == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().write("{\"message\":\"請先登入\"}");
            return;
        }

        if (path.startsWith("/admin")) {
            User user = (User) userObj;
            if (!"ADMIN".equals(user.getRole())) {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write("{\"message\":\"你沒有權限訪問此頁面\"}");
                return;
            }
        }

        chain.doFilter(req, res); // 驗證通過，放行
    }
}