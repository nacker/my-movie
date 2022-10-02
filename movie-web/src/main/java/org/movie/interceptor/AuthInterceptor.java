package org.movie.interceptor;

import cn.hutool.core.util.StrUtil;
import org.movie.common.api.ResultCode;
import org.movie.common.exception.ApiException;
import org.movie.common.service.RedisService;
import org.movie.common.util.ComConstants;
import org.movie.common.util.JwtTokenUtil;
import org.movie.modules.web.model.User;
import org.movie.modules.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 作用： 验证 用户是否登录
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    // 配置文件中的白名单secure.ignored.urls
    private List<String> urls;

//    @Autowired
//    private UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Autowired
    RedisService redisService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1、不需要登录就可以访问的路径——白名单
        // 获取当前请求   /user/login
        String requestURI = request.getRequestURI();
        // Ant方式路径匹配 /**  ？  _
        PathMatcher matcher = new AntPathMatcher();
        // 因为session基于cookie,解决cookie的跨站不能共享的新特性问题
        response.setHeader("SET-COOKIE", "JSESSIONID=" + request.getSession().getId() + ";Path=/;secure;HttpOnly;SameSite=None");
        for (String ignoredUrl : urls) {
            if(matcher.match(ignoredUrl,requestURI)){
                return  true;
            }
        }

        // 拿到jwt令牌
        String jwt = request.getHeader(tokenHeader);
        String tempjwt = jwt;
        //判断是否存在 判断开头是否加了tokenHead Bearer
        if(!StrUtil.isBlank(jwt) && jwt.startsWith(tokenHead)) {
            // 解密
            jwt = jwt.substring(tokenHead.length());
            String userName = jwtTokenUtil.getUserNameFromToken(jwt);
            if (!StrUtil.isBlank(userName)) {
                String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + userName;
                String token = (String)redisService.get(key);
                if (!StrUtil.isBlank(token) && StrUtil.equals(tempjwt,token,false)) {
                    return true;
                }
            }
        }
        throw new ApiException(ResultCode.UNAUTHORIZED);
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
