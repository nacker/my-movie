package org.movie.modules.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.movie.common.api.CommonResult;
import org.movie.common.service.RedisService;
import org.movie.common.util.ComConstants;
import org.movie.common.util.JwtTokenUtil;
import org.movie.modules.web.model.Favorites;
import org.movie.modules.web.model.Movie;
import org.movie.modules.web.model.User;
import org.movie.modules.web.service.FavoritesService;
import org.movie.modules.web.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Api(tags = "FavoritesController", description = "收藏模块")
@RequestMapping("/favorites")
@Slf4j
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private MovieService movieService;


    @ApiOperation(value = "收藏电影列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult list(HttpServletRequest request){
        List<Long> list = favoritesService.getMovieIdlist(request);
        List<Movie> movieslist = movieService.getMovieslist(list);
        return CommonResult.success(movieslist);
    }


    @ApiOperation("收藏电影")
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult add(HttpServletRequest request,@PathVariable Long id) {
        favoritesService.add(request,id);
        return CommonResult.success(null);
    }

}
