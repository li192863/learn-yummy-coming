package com.chestnut.haoweilai.filter;

import com.alibaba.fastjson.JSON;
import com.chestnut.haoweilai.common.BaseContext;
import com.chestnut.haoweilai.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 检查用户是否完成登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 获取本次请求uri
        String requestURI = req.getRequestURI();
        // 判断本次请求是否需要处理
        String[] urls = new String[] {
                "/employee/login",  // 后台员工登录
                "/employee/logout",  // 后台员工退出
                "/backend/**",  // 后台资源
                "/front/**",  // 前台资源
                "/common/**",  // 公共资源（上传/下载图片）
                "/user/sendMsg",  // 前台发送短信
                "/user/login"  // 前台登录
        };
        // 若资源无需拦截则直接放行
        if (isNotBlocked(urls, requestURI)) {
            log.info("请求{}无需拦截", requestURI);
            chain.doFilter(request, response);
            return;
        }
        // 若后台已登录则直接放行
        if (redisTemplate.opsForValue().get("employee") != null) {
            // 当前用户id
            Long employeeId = (Long) redisTemplate.opsForValue().get("employee");
            BaseContext.setCurrentId(employeeId);

            log.info("请求{}无需拦截，员工（id为{}）已登录", requestURI, employeeId);
            chain.doFilter(request, response);
            return;
        }
        // 若用户已登录则直接放行
        if (redisTemplate.opsForValue().get("user") != null) {
            // 当前用户id
            Long userId = (Long) redisTemplate.opsForValue().get("user");
            BaseContext.setCurrentId(userId);

            log.info("请求{}无需拦截，用户（id为{}）已登录", requestURI, userId);
            chain.doFilter(request, response);
            return;
        }
        // 若未登录则跳转至登录页面
        log.info("请求{}已拦截，用户未登录", requestURI);
        resp.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        resp.getWriter().flush();
    }

    /**
     * 路径匹配，检查本次路径是否放行
     * @param urls
     * @param requestURI
     * @return
     */
    private boolean isNotBlocked(String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
