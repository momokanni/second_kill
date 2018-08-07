package com.kill.service.impl;

import com.kill.VO.GoodsVO;
import com.kill.entity.Goods;
import com.kill.entity.KillGoods;
import com.kill.entity.mapper.GoodsMapper;
import com.kill.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品业务层
 *
 * @author Administrator
 * @create 2018-07-19 14:19
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVO> getGoodsVoList() {

        return goodsMapper.listGoodsVo();
    }

    @Override
    public GoodsVO getGoodsDetail(long goodsId) {

        return goodsMapper.getGoodsDetail(goodsId);
    }

    @Override
    public boolean reduceStock(GoodsVO goodsVO) {
        KillGoods goods = new KillGoods();
        goods.setGoodsId(goodsVO.getId());
        int reduce = goodsMapper.reduceStock(goods);
        return reduce > 0;
    }
}
