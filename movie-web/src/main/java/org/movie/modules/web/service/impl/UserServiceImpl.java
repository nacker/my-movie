package org.movie.modules.web.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.movie.common.exception.ApiException;
import org.movie.common.exception.Asserts;
import org.movie.modules.web.dto.UserLoginParam;
import org.movie.modules.web.dto.UserParam;
import org.movie.modules.web.mapper.UserMapper;
import org.movie.modules.web.model.User;
import org.movie.modules.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public User login(UserLoginParam userLoginParam) {
        // 密码需要客户端加密后传递
        User user = null;
        try {
            user = loadUserByUsername(userLoginParam.getUsername());
            if(!BCrypt.checkpw(userLoginParam.getPassword(),user.getPassword())){
                Asserts.fail("密码不正确");
            }
            if(0 == user.getStatus()){
                Asserts.fail("帐号已被禁用");
            }
//            insertLoginLog(username);
        } catch (Exception e) {
            Asserts.fail("登录异常:"+e.getMessage());
        }
        return user;
    }

    @Override
    public User loadUserByUsername(String username){
        //获取用户信息
        User user = getAdminByUsername(username);
        if (user != null) {
            // 查询用户访问资源，暂留， 后续改动

            return user;
        }
        throw new ApiException("用户不存在");
    }

    /**
     * 根据用户名获取用户信息
     */
    @Override
    public User getAdminByUsername(String username) {
        User user = null;
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername,username);
        List<User> userList = list(wrapper);
        if (userList != null && userList.size() > 0) {
            user = userList.get(0);
            return user;
        }
        return null;
    }


    /**
     * 注册功能
     */
    @Override
    public User register(UserParam userParam) {
        User user = new User();
        BeanUtils.copyProperties(userParam, user);
        user.setCreateTime(new Date());
        user.setStatus(1);
        //查询是否有相同用户名的用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername,user.getUsername());
        List<User> umsAdminList = list(wrapper);
        if (umsAdminList.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = BCrypt.hashpw(user.getPassword());
        user.setPassword(encodePassword);
        baseMapper.insert(user);
        return user;
    }
}
