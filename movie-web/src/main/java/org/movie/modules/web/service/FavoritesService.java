package org.movie.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.movie.modules.web.model.Favorites;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface FavoritesService extends IService<Favorites>{
    /**
     * 获取收藏电影列表
     *
     * @return
     */
    List<Long> getMovieIdlist(HttpServletRequest request);

    void add(HttpServletRequest request, Long id);

}
