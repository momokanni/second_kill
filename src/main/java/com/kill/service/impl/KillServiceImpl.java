package com.kill.service.impl;

import com.kill.VO.GoodsVO;
import com.kill.VO.ResultVO;
import com.kill.entity.KillOrder;
import com.kill.entity.KillUser;
import com.kill.enums.StatusCode;
import com.kill.redis.GoodsKey;
import com.kill.redis.KillKey;
import com.kill.redis.KillOrderKey;
import com.kill.redis.RedisServiceImpl;
import com.kill.service.GoodsService;
import com.kill.service.KillService;
import com.kill.service.OrderService;
import com.kill.util.MD5Util;
import com.kill.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.UUID;

/**
 * 秒杀业务层
 *
 * @author Administrator
 * @create 2018-07-20 16:41
 */
@Service
public class KillServiceImpl implements KillService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisServiceImpl redisService;

    @Transactional
    @Override
    public KillOrder secondKill(KillUser user, GoodsVO goodsVO) {
        //减库存
        boolean reduceStatus = goodsService.reduceStock(goodsVO);
        if (reduceStatus){
            //下订单、写入秒杀订单
            return orderService.createOrder(user,goodsVO);
        } else {
            setGoodsSellOut(goodsVO.getId());
            return null;
        }

    }
    
    /**
     * @param: 
     * describe: 获取秒杀结果
     * creat_user: sl
     * creat_date: 2018/8/6
     * creat_time: 10:53
     **/
    @Override
    public ResultVO<String> getKillResult(Long userId, long goodsId) {
         KillOrder killOrder = orderService.getKillOrderByUserIdGoodsId(goodsId,userId);
         if (killOrder != null){
             return ResultUtil.success(killOrder.getId());
         } else {
            boolean sellOut = getGoodsSellOut(goodsId);
            if (sellOut) {
                return ResultUtil.error(StatusCode.KILL_GOODS_FAILED.getCode(),StatusCode.KILL_GOODS_FAILED.getMsg());
            } else {
                return ResultUtil.error(StatusCode.KILL_ORDER_SUCCESS_WAIT.getCode(),StatusCode.KILL_ORDER_SUCCESS_WAIT.getMsg());
            }
         }
    }


    private boolean getGoodsSellOut(long goodsId) {

        return redisService.exists(KillOrderKey.stockCountKey,"_" + goodsId);
    }

    private void setGoodsSellOut(Long goodsId) {
        redisService.set(KillOrderKey.stockCountKey,"_" + goodsId,true);
    }

    /**
     * @param:
     * describe: 创建秒杀路径
     * creat_user: sl
     * creat_date: 2018/8/6
     * creat_time: 10:54
     **/
    @Override
    public String createKillPath(KillUser user, long goodsId) {
        String path_uuid = MD5Util.backToDB(UUID.randomUUID().toString(),user.getSalt());
        redisService.set(GoodsKey.killPath,"_"+user.getId()+"_"+goodsId,path_uuid);
        return path_uuid;
    }
    
    /**
     * @param: 
     * describe: 验证path_UUID
     * creat_user: sl
     * creat_date: 2018/8/6
     * creat_time: 11:13
     **/
    @Override
    public boolean checkKillPath(KillUser user, long goodsId, String path) {
        String path_uuid = redisService.get(GoodsKey.killPath,"_"+user.getId()+"_"+goodsId,String.class);
        return path_uuid.equals(path);
    }

    /**
     * @param: 
     * describe: 验证verifyCode
     * creat_user: sl
     * creat_date: 2018/8/6
     * creat_time: 22:02
     **/
    @Override
    public boolean checkVerifyCode(KillUser user, long goodsId, Integer verifyCode) {
        Integer codeOld = redisService.get(KillKey.killKey, "_" + user.getId() + "_" + goodsId, Integer.class);
        if(codeOld == null || codeOld - verifyCode != 0 ) {
            return false;
        }
        redisService.delete(KillKey.killKey, "_" + user.getId() + "_" + goodsId);
        return true;
    }


    /**
     * @param: 
     * describe: 生成数字验证码
     * creat_user: sl
     * creat_date: 2018/8/6
     * creat_time: 21:31
     **/
    @Override
    public BufferedImage createVerifyCode(KillUser user, long goodsId) {
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        Integer rnd = calc(verifyCode);
        redisService.set(KillKey.killKey, "_" + user.getId() + "_" + goodsId, rnd);
        //输出图片
        return image;
    }

    /**
     * 生成公式
     */
    private static char[] ops = new char[] {'+', '-', '*'};
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    /**
     * 根据生成公式计算出结果
     * @param exp
     * @return
     */
    private static Integer calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
