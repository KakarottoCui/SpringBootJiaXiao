package com.singulee.carschool.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 */
public class Interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        HttpSession session = request.getSession();


//如果是登录页面则放行
        if (request.getRequestURI().equals("/login/login") || request.getRequestURI().equals("/login/getCode")
                || (request.getRequestURI().equals("/login/resetPassword") && session.getAttribute("smscode") != null)
        ) {
            return true;
        }
//如果用户已登录也放行
        if (session.getAttribute("user") != null) {
            return true;
        }
//用户没有登录挑转到登录页面
        response.sendRedirect("/login.html");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
