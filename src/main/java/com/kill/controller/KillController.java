package com.kill.controller;

import com.kill.VO.GoodsVO;
import com.kill.VO.ResultVO;
import com.kill.annotation.AccessLimit;
import com.kill.entity.KillOrder;
import com.kill.entity.KillUser;
import com.kill.entity.OrderInfo;
import com.kill.enums.StatusCode;
import com.kill.exception.KillException;
import com.kill.rabbitmq.KillMessage;
import com.kill.rabbitmq.MQSender;
import com.kill.redis.*;
import com.kill.service.GoodsService;
import com.kill.service.KillService;
import com.kill.service.OrderService;
import com.kill.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;

/**
 * 秒杀控制器
 *
 * @author Administrator
 * @create 2018-07-20 10:50
 *
 * InitializingBean : 类初始化
 */
@Slf4j
@Controller
@RequestMapping(value = "/kill")
public class KillController implements InitializingBean {

    @Autowired
    private RedisServiceImpl redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private KillService killService;

    @Autowired
    private MQSender sender;

    @Autowired
    private RedisLock redisLock;

    private static final Integer TIMEOUT = 10 * 1000;

    @Override
    public void afterPropertiesSet() {
        List<GoodsVO> goodsList = goodsService.getGoodsVoList();
        if (goodsList != null){
            for (GoodsVO killGoods: goodsList) {
                if (killGoods.getStockCount() > 0){
                    redisService.set(GoodsKey.killStockCount,"_"+killGoods.getId(),killGoods.getStockCount());
                }
            }
        } else {
            return;
        }
    }

    /**
     * @param: 
     * describe: 秒杀
     * creat_user: sl
     * creat_date: 2018/7/26
     * creat_time: 11:54
     * QPS: 3196/s  2143个用户
     * 并发: 5000 * 10
     **/
    @PostMapping(value = "/{path}/do_kill")
    @ResponseBody
    public ResultVO<OrderInfo> doKill(@RequestParam(value = "goodsId") long goodsId, KillUser user, Model model,
                                       @PathVariable String path){
        if (user == null){
            return ResultUtil.error(StatusCode.KILL_USER_NOT_EXISTS.getCode(),StatusCode.KILL_USER_NOT_EXISTS.getMsg());
        }
        model.addAttribute("user",user);

        boolean checkPath = killService.checkKillPath(user,goodsId,path);
        if(!checkPath){
            return ResultUtil.error(StatusCode.KILL_PATH_FAILED.getCode(),StatusCode.KILL_PATH_FAILED.getMsg());
        }

        // lock
        long time = System.currentTimeMillis() + TIMEOUT;
        boolean lockStatus = redisLock.lock(LockKey.lock_kill,"_"+goodsId,String.valueOf(time));
        if (!lockStatus){
            throw new KillException(StatusCode.LOCK_STATUS_FAILED.getCode(),StatusCode.LOCK_STATUS_FAILED.getMsg());
        }

        //1、预减库存
        long count = redisService.decr(GoodsKey.killStockCount,"_"+goodsId);
        if (count < 0){
            return ResultUtil.error(StatusCode.GOODS_SELL_OUT.getCode(),StatusCode.GOODS_SELL_OUT.getMsg());
        }
        //判断重复秒杀
        KillOrder killOrder = orderService.getKillOrderByUserIdGoodsId(goodsId,user.getId());
        if (killOrder != null){
            return ResultUtil.error(StatusCode.KILL_ORDER_ALREADY_EXISTED.getCode(),StatusCode.KILL_ORDER_ALREADY_EXISTED.getMsg());
        }
        //入队
        KillMessage killMessage = new KillMessage();
        killMessage.setGoodsId(goodsId);
        killMessage.setUser(user);
        sender.sendKillMsg(killMessage);

        //unlock
        redisLock.unLock(LockKey.lock_kill,"_"+goodsId,String.valueOf(time));

        return ResultUtil.waitResponse(StatusCode.KILL_ORDER_SUCCESS_WAIT.getCode(),StatusCode.KILL_ORDER_SUCCESS_WAIT.getMsg());

        /*//查询秒杀商品
        GoodsVO goodsVO = goodsService.getGoodsDetail(goodsId);
        //判断秒杀时间
        long startDate = goodsVO.getStartDate().getTime();
        long endDate = goodsVO.getEndDate().getTime();
        long now = System.currentTimeMillis();
        if(now > startDate && now < endDate){ //秒杀进行中
            //判断库存
            int stockCount = goodsVO.getStockCount();
            if (stockCount <= 0){
                return ResultUtil.error(StatusCode.GOODS_SELL_OUT.getCode(),StatusCode.GOODS_SELL_OUT.getMsg());
            }
            //避免同一个用户重复秒杀
            OrderInfo order = orderService.getKillOrderByUserIdGoodsId(user.getId(),goodsId);
            if (!StringUtils.isEmpty(order)){
                return ResultUtil.error(StatusCode.KILL_ORDER_ALREADY_EXISTED.getCode(),StatusCode.KILL_ORDER_ALREADY_EXISTED.getMsg());
            }
            OrderInfo orderInfo = killService.secondKill(user,goodsVO);
            return ResultUtil.success(orderInfo);

        } else {
            log.info("秒杀无效请求,goodsId: " + goodsId);
            return ResultUtil.error(StatusCode.GOODS_NO_SECOND_KILLING.getCode(),StatusCode.GOODS_NO_SECOND_KILLING.getMsg());
        }*/
    }

    /**
     * 秒杀轮询结果
     * @param goodsId
     * @param user
     * @param model
     * @return
     */
    @GetMapping(value = "/result")
    @ResponseBody
    public ResultVO<String> killResult(@RequestParam(value = "goodsId") long goodsId, KillUser user, Model model){
        if (user == null){
            return ResultUtil.error(StatusCode.KILL_USER_NOT_EXISTS.getCode(),StatusCode.KILL_USER_NOT_EXISTS.getMsg());
        }
        model.addAttribute("user",user);

        return killService.getKillResult(user.getId(),goodsId);
    }
    
    /**
     * @param: 
     * describe: 生成随机秒杀路径
     * creat_user: sl
     * creat_date: 2018/8/6
     * creat_time: 10:59
     **/
    @AccessLimit(seconds = 5,maxCount = 5)
    @GetMapping(value = "/path")
    @ResponseBody
    public ResultVO<String> path(HttpServletRequest request,
                                 @RequestParam(value = "goodsId") long goodsId, KillUser user,
                                 @RequestParam(value = "verifyCode",defaultValue = "0") Integer verifyCode) {
        if (user == null) {
            return ResultUtil.error(StatusCode.KILL_USER_NOT_EXISTS.getCode(), StatusCode.KILL_USER_NOT_EXISTS.getMsg());
        }
        boolean check = killService.checkVerifyCode(user, goodsId, verifyCode);
        if(!check) {
            return ResultUtil.error(StatusCode.VERIFYCODE_INPUT_VALUE_ERROR.getCode(),StatusCode.VERIFYCODE_INPUT_VALUE_ERROR.getMsg());
        }
        String path_uuid = killService.createKillPath(user,goodsId);
        return ResultUtil.success(path_uuid);
    }


}
