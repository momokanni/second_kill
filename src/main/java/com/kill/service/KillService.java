package com.kill.service;

import com.kill.VO.GoodsVO;
import com.kill.VO.ResultVO;
import com.kill.entity.KillOrder;
import com.kill.entity.KillUser;
import java.awt.image.BufferedImage;

public interface KillService {

    KillOrder secondKill(KillUser user, GoodsVO goodsVO);

    ResultVO<String> getKillResult(Long userId, long goodsId);

    String createKillPath(KillUser user, long goodsId);

    boolean checkKillPath(KillUser user, long goodsId, String path);

    BufferedImage createVerifyCode(KillUser user, long goodsId);

    boolean checkVerifyCode(KillUser user, long goodsId, Integer verifyCode);
}
