package com.kill.entity.mapper;

import com.kill.VO.GoodsVO;
import com.kill.entity.KillGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

/**
 * class_name: GoodsMapper
 * package: com.kill.entity.mapper
 * describe: 商品映射
 * creat_user: sl
 * creat_date: 2018/7/19
 * creat_time: 13:57
 **/
@Mapper
public interface GoodsMapper {

    /**
     * 秒杀商品列表
     * @return
     */
    @Select("select g.*,kg.stock_count,kg.start_date,kg.end_date,kg.kill_price from kill_goods kg" +
            " left join goods g on g.id = kg.goods_id")
    List<GoodsVO> listGoodsVo();

    /**
     * 商品详情
     * @param goodsId
     * @return
     */
    @Select("select g.*,kg.stock_count,kg.start_date,kg.end_date,kg.kill_price from kill_goods kg" +
            " left join goods g on g.id = kg.goods_id where g.id = #{goodsId}")
    GoodsVO getGoodsDetail(long goodsId);

    /**
     * 减库存
     * @param goods
     */
    @Update("update kill_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStock(KillGoods goods);
}
