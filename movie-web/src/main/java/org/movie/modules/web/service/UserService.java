package org.movie.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.movie.modules.web.dto.UserLoginParam;
import org.movie.modules.web.dto.UserParam;
import org.movie.modules.web.model.User;

public interface UserService extends IService<User>
{
    /**
     * 登录功能
     * @param userLoginParam 用户账号和密码
     * @return 生成的JWT的token
     */
    User login(UserLoginParam userLoginParam);

    /**
     * 获取用户信息
     */
    User loadUserByUsername(String username);

    /**
     * 根据用户名获取用户信息
     */
    User getAdminByUsername(String username);

    /**
     * 注册功能
     */
    User register(UserParam userParam);
}
