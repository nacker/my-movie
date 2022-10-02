package org.movie.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.movie.common.service.RedisService;
import org.movie.common.util.JwtTokenUtil;
import org.movie.modules.web.mapper.FavoritesMapper;
import org.movie.modules.web.model.Favorites;
import org.movie.modules.web.model.User;
import org.movie.modules.web.service.FavoritesService;
import org.movie.modules.web.service.MovieService;
import org.movie.modules.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FavoritesServiceImpl extends ServiceImpl<FavoritesMapper, Favorites> implements FavoritesService {

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


    @Autowired
    private UserService userService;

    @Override
    public List<Long> getMovieIdlist(HttpServletRequest request) {
        // 拿到jwt令牌
        String jwt = request.getHeader(tokenHeader);
        jwt = jwt.substring(tokenHead.length());
        String userName = jwtTokenUtil.getUserNameFromToken(jwt);
        // user
        User user = userService.getAdminByUsername(userName);

        QueryWrapper<Favorites> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Favorites> lambda = wrapper.lambda();
        lambda.eq(Favorites::getUserId,user.getId());
        List<Favorites> list = this.list(lambda);

        ArrayList<Long> ids = new ArrayList<Long>();
        for (Favorites item : list) {
            ids.add(item.getMovieId());
        }
        return ids;
    }

    @Override
    public void add(HttpServletRequest request, Long id) {
        // 拿到jwt令牌
        String jwt = request.getHeader(tokenHeader);
        jwt = jwt.substring(tokenHead.length());
        String userName = jwtTokenUtil.getUserNameFromToken(jwt);
        // user
        User user = userService.getAdminByUsername(userName);
        // favorites
        Favorites favorites = new Favorites();
        favorites.setMovieId(id);
        favorites.setUserId(user.getId());
        favorites.setCreateTime(new Date());

        QueryWrapper<Favorites> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Favorites> lambda = wrapper.lambda();
        lambda.eq(Favorites::getUserId,user.getId());
        lambda.eq(Favorites::getMovieId,id);
        List<Favorites> flist = this.list(wrapper);
        if (flist.size() > 0) {
            Favorites f = flist.get(0);
            baseMapper.deleteById(f.getId());
            return;
        }
        baseMapper.insert(favorites);
    }
}
