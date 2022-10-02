package org.movie.modules.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.movie.modules.web.model.Favorites;
import org.movie.modules.web.model.Movie;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface MovieService extends IService<Movie> {
    /**
     * 分页获取电影列表
     */
    Page<Movie> list(String keyword, Integer pageSize, Integer pageNum);


    /*
    *  根据ids获取电影列表
    * */
    List<Movie> getMovieslist(List<Long> ids);

}
