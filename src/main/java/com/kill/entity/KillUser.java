package com.kill.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 秒杀用户
 *
 * @author Administrator
 * @create 2018-07-14 20:32
 */
@Data
@ApiModel(description = "秒杀用户")
public class KillUser implements Serializable {

    private static final long serialVersionUID = 824991349123362696L;
    @ApiModelProperty(value = "秒杀用户ID")
    private Long id;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "密码盐")
    private String salt;
    @ApiModelProperty(value = "头像")
    private String headImage;
    @ApiModelProperty(value = "上一次登录时间")
    private Date lastLoginDate;
    @ApiModelProperty(value = "登录次数")
    private Integer loginCount;
    @ApiModelProperty(value = "注册时间")
    private Date createDate;
    @ApiModelProperty(value = "修改时间")
    private Date updateDate;
}
