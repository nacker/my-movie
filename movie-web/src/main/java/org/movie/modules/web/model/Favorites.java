package org.movie.modules.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/*
 * Favorites Tablename
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("favorites")
@ApiModel(value ="Favorites对象", description="用户搜藏表")
public class Favorites implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "电影ID")
    private Long movieId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
