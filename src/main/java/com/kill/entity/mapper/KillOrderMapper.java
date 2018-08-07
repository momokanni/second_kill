package com.kill.entity.mapper;

import com.kill.entity.KillOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface KillOrderMapper {

    @Select("select * from kill_order where user_id = #{userId} and goods_id = #{goodsId}")
    KillOrder getKillOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into kill_order (user_id,order_id,goods_id) values (#{userId},#{orderId},#{goodsId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int createKillOrder(KillOrder killOrder);
}
