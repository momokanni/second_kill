package com.kill.service;

import com.kill.VO.GoodsVO;
import com.kill.entity.Goods;

import java.util.List;

public interface GoodsService {

    List<GoodsVO> getGoodsVoList();

    GoodsVO getGoodsDetail(long goodsId);

    boolean reduceStock(GoodsVO goodsVO);
}
