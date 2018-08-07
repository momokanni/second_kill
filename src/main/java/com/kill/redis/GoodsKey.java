package com.kill.redis;

/**
 * 商品类key
 *
 * @author Administrator
 * @create 2018-07-30 14:03
 */
public class GoodsKey extends BasePrefix {

    public GoodsKey(Integer expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60,"goods_list");

    public static GoodsKey getGoodsDetail = new GoodsKey(1800,"goods_detail");

    public static GoodsKey killStockCount = new GoodsKey(0,"stockCount");

    public static GoodsKey killPath = new GoodsKey(60,"killPath");
}
