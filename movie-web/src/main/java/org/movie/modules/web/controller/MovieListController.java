package org.movie.modules.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.movie.common.api.CommonPage;
import org.movie.common.api.CommonResult;
import org.movie.modules.web.model.Movie;
import org.movie.modules.web.service.MovieService;
import org.movie.modules.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Api(tags = "MovieListController", description = "电影模块")
@RequestMapping("/movies")
@Slf4j
public class MovieListController {

    @Autowired
    private MovieService movieService;


    @ApiOperation("根据电影名分页获电影列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<Movie>> list(@RequestParam(value = "search", required = false) String search,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<Movie> movieList = movieService.list(search, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(movieList));
    }

    @ApiOperation(value = "电影列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult allList(){
        return CommonResult.success(movieService.list());
    }


    @ApiOperation("电影详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult update(@PathVariable Long id) {
        Movie movie = movieService.getById(id);
        return CommonResult.success(movie);
    }
}