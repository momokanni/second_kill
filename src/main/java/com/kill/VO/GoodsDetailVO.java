package com.kill.VO;

import com.kill.entity.KillUser;
import lombok.Data;

/**
 * 商品详情对象视图
 *
 * @author Administrator
 * @create 2018-08-01 11:13
 */
@Data
public class GoodsDetailVO {

    private GoodsVO goods;
    private KillUser killuser;
    private int killStatus;
    private int remainSeconds;
}
