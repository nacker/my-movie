package org.movie.modules.web.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.movie.common.api.CommonResult;
import org.movie.common.service.RedisService;
import org.movie.common.util.ComConstants;
import org.movie.modules.web.dto.UserLoginParam;
import org.movie.modules.web.dto.UserParam;
import org.movie.modules.web.model.User;
import org.movie.modules.web.service.UserService;
import org.movie.common.util.JwtTokenUtil;
import org.movie.modules.web.service.impl.UserServiceImpl;
import org.movie.modules.web.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Api(tags = "UserController", description = "用户模块")
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Autowired
    private RedisService redisService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UserVo> login(@Validated @RequestBody UserLoginParam userLoginParam) {
        User login = userService.login(userLoginParam);
        if (login == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }

        UserVo vo = new UserVo();
        String userName = login.getUsername();
        String token = tokenHead + " " + jwtTokenUtil.generateUserNameStr(userName);
        vo.setToken(token);

        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + userName;
        redisService.set(key, token, REDIS_EXPIRE);

        return CommonResult.success(vo);
    }

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<User> register(@Validated @RequestBody UserParam userParam) {
        User user = userService.register(userParam);
        if (user == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(null);
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult logout(HttpServletRequest request) {
        // 拿到jwt令牌
        String jwt = request.getHeader(tokenHeader);
        //判断是否存在 判断开头是否加了tokenHead Bearer
        if(!StrUtil.isBlank(jwt) && jwt.startsWith(tokenHead)) {
            // 解密
            jwt = jwt.substring(tokenHead.length());
            String userName = jwtTokenUtil.getUserNameFromToken(jwt);
            if (!StrUtil.isBlank(userName)) {
                String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + userName;
                redisService.del(key);
                return CommonResult.success(null);
            }
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "获取jwt令牌(临时设置)")
    @RequestMapping(value = "/jwt", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult session(HttpServletRequest request) {
        // 拿到jwt令牌
        String jwt = request.getHeader(tokenHeader);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return CommonResult.success(map);
    }
}


