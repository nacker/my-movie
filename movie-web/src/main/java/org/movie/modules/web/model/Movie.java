package org.movie.modules.web.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/*
 * Movie Tablename
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("movie")
@ApiModel(value="movie对象", description="电影")
public class Movie {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "电影标题")
    private String title;

    @ApiModelProperty(value = "电影内容")
    private String content;

    @ApiModelProperty(value = "电影热度")
    private String hot;

    @ApiModelProperty(value = "电影图片名")
    private String imageName;

    @ApiModelProperty(value = "电影名")
    private String name;

    @ApiModelProperty(value = "电影发布日期")
    private Date publishDate;
}
