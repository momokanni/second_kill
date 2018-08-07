package com.kill.controller;

import com.kill.VO.ResultVO;
import com.kill.annotation.AccessLimit;
import com.kill.entity.KillUser;
import com.kill.enums.StatusCode;
import com.kill.service.KillService;
import com.kill.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * 验证码控制器
 *
 * @author Administrator
 * @create 2018-08-06 22:09
 */
@Slf4j
@Api(tags ="验证码控制器")
@Controller
@RequestMapping(value = "/Verification")
public class VerifyCodeController {

    @Autowired
    private KillService killService;

    /**
     * @param:
     * describe: 生成数字计算验证码
     * creat_user: sl
     * creat_date: 2018/8/6
     * creat_time: 21:19
     **/
    @ApiOperation(value="验证码接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "user", value = "秒杀用户", required = true, dataType = "KillUser")
    })
    @GetMapping(value = "/code")
    @AccessLimit(seconds = 5,maxCount = 5)
    @ResponseBody
    public ResultVO<String> verifyCode(@RequestParam(value = "goodsId") long goodsId, KillUser user, HttpServletResponse response) {
        try {
            BufferedImage image = killService.createVerifyCode(user,goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image,"JPEG",out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e){
            log.error("【create verifyCode】 exception: {}",e);
            return null;
        }
    }
}
