package org.movie.modules.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.movie.modules.web.mapper.MovieMapper;
import org.movie.modules.web.model.Movie;
import org.movie.modules.web.service.FavoritesService;
import org.movie.modules.web.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Page<Movie> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<Movie> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Movie> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Movie> lambda = wrapper.lambda();
        if(StrUtil.isNotEmpty(keyword)){
            lambda.like(Movie::getName,keyword);
        }
        return page(page,wrapper);
    }

    @Override
    public List<Movie> getMovieslist(List<Long> ids) {
        List<Movie> list = this.listByIds(ids);
        return list;
    }
}
