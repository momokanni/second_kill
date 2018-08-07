package com.kill.controller;

import com.kill.VO.GoodsDetailVO;
import com.kill.VO.GoodsVO;
import com.kill.VO.ResultVO;
import com.kill.annotation.AccessLimit;
import com.kill.constants.Constants;
import com.kill.entity.KillUser;
import com.kill.enums.StatusCode;
import com.kill.redis.GoodsKey;
import com.kill.redis.RedisServiceImpl;
import com.kill.service.GoodsService;
import com.kill.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.kill.util.TemplateUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 商品控制器
 *
 * @author Administrator
 * @create 2018-07-17 22:20
 */
@Slf4j
@Controller
@RequestMapping(value = "/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisServiceImpl redisService;

    @Autowired
    private TemplateUtil templateEngine;



    /**
     * @param: 
     * describe: 刚方法KillUser的注入是通过
     * creat_user: sl
     * creat_date: 2018/7/18
     * creat_time: 10:41
     * 压测结果(QPS)：6997.9,并发：5000 * 10
     *
     * 优化后：
     * QPS：7993
     **/
    @GetMapping(value = "/list",produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response,Model model, KillUser user){
        List<GoodsVO> goodsList = goodsService.getGoodsVoList();
        model.addAttribute("goodsList",goodsList);
        model.addAttribute("user",user);

        String list_html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if (!StringUtils.isEmpty(list_html)){
            return list_html;
        }
        list_html = templateEngine.generateHtml(request,response,model.asMap(),"goods_list",GoodsKey.getGoodsList,"");
        return list_html;
    }

    @GetMapping(value = "/to_detail/{goodsId}")
    @ResponseBody
    public ResultVO<GoodsDetailVO> detail(HttpServletRequest request, HttpServletResponse response,
                                          @PathVariable Long goodsId, Model model,KillUser user){
        GoodsVO goods = null;
        try {
            goods = goodsService.getGoodsDetail(goodsId);
            if(StringUtils.isEmpty(goods)){
                return ResultUtil.error(StatusCode.GOODS_NOT_EXISTS.getCode(),StatusCode.GOODS_NOT_EXISTS.getMsg());
            }
        } catch (Exception e) {
            log.error("查看商品详情异常: {}",e);
            return ResultUtil.error(StatusCode.GOODS_DETAIL_ERROR.getCode(),StatusCode.GOODS_DETAIL_ERROR.getMsg());
        }

        long startDate = goods.getStartDate().getTime();
        long endDate = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int killStatus = 0; //0:未开始，1:进行中，2:已结束
        int remainSeconds = 0;

        if(now < startDate){ //秒杀未开始，倒计时
            killStatus = 0;
            remainSeconds = (int)(startDate - now) / 1000;
        } else if(now > endDate){ //秒杀已结束
            killStatus = 2;
            remainSeconds = -1;
        } else { //秒杀进行中
            killStatus = 1;
            remainSeconds = 0;
        }

        GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
        goodsDetailVO.setGoods(goods);
        goodsDetailVO.setKilluser(user);
        goodsDetailVO.setKillStatus(killStatus);
        goodsDetailVO.setRemainSeconds(remainSeconds);

        return ResultUtil.success(goodsDetailVO);
    }
}
