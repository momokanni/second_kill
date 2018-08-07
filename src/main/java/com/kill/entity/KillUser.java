package com.kill.entity;

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
public class KillUser implements Serializable {

    private static final long serialVersionUID = 824991349123362696L;

    private Long id;

    private String nickname;

    private String password;

    private String salt;

    private String headImage;

    private Date lastLoginDate;

    private Integer loginCount;

    private Date createDate;

    private Date updateDate;
}
