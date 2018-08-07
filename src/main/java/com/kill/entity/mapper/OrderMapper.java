package com.kill.entity.mapper;

import com.kill.entity.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderMapper {

    @Insert("insert into order_info (user_id,goods_id,delivery_addr_id,goods_name,goods_count,goods_price,order_channel,status,create_date)" +
            "values (#{userId},#{goodsId},#{deliveryAddrId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    long createOrder(OrderInfo orderInfo);

    @Select("select * from order_info where id = #{orderId}")
    OrderInfo getOrderDetailById(long orderId);
}
