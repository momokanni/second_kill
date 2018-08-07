package com.kill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kill.VO.LoginVO;
import com.kill.constants.CookieConstant;
import com.kill.entity.KillUser;
import com.kill.entity.mapper.KillUserMapper;
import com.kill.enums.StatusCode;
import com.kill.exception.KillException;
import com.kill.redis.KillUserKey;
import com.kill.redis.LoginTokenKey;
import com.kill.redis.RedisServiceImpl;
import com.kill.service.KillUserService;
import com.kill.util.CookieUtil;
import com.kill.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * 秒杀用户业务层实现类
 *
 * @author Administrator
 * @create 2018-07-16 15:32
 */
@Service
public class KillUserServiceImpl implements KillUserService {

    @Autowired
    private KillUserMapper killUserMapper;

    @Autowired
    private RedisServiceImpl redisService;
    
    /**
     * @param: id
     * describe: 获取用户信息
     * creat_user: sl
     * creat_date: 2018/7/16
     * creat_time: 18:58
     **/
    @Override
    public KillUser getKillUserById(Long id) {
        KillUser user = redisService.get(KillUserKey.getById,"_"+id,KillUser.class);
        if(user !=null){
            return user;
        }
        user = killUserMapper.getKillUserById(id);
        if (user !=null){
            redisService.set(KillUserKey.getById,"_"+id,user);
        }
        return user;
    }

    @Override
    public boolean login(HttpServletResponse response,LoginVO loginVO) {
        if(loginVO == null){
            throw new KillException(StatusCode.PARAM_ERROR);
        }
        String mobile = loginVO.getMobile();
        String inputToBack_Pwd = loginVO.getPassword();
        //判断手机号是否真实存在
        KillUser user = getKillUserById(Long.parseLong(mobile));
        if(user == null){
            throw new KillException(StatusCode.KILL_USER_NOT_EXISTS);
        }
        //验证密码
        String backToDB_Pwd = user.getPassword();
        String salt = user.getSalt();
        String clacPwd = MD5Util.backToDB(inputToBack_Pwd,salt);
        if(!clacPwd.equals(backToDB_Pwd)){
            throw new KillException(StatusCode.LOGIN_PWD_ERROR);
        }
        //生成token,并插入到cookie和redis中
        String token = UUID.randomUUID().toString();
        addCookie(response,token,user);
        return true;
    }

    /**
     * @param: 
     * describe: 通过token获取保存到缓存中的用户信息
     * creat_user: sl
     * creat_date: 2018/7/17
     * creat_time: 21:30
     **/
    @Override
    public KillUser getByToken(HttpServletResponse response, String token) {

        if(StringUtils.isEmpty(token)){
            return null;
        }
        KillUser user = redisService.get(LoginTokenKey.cookieKey,token,KillUser.class);
        if (user != null){
            /**
             * 这句的作用是，登录成功后更新缓存有效时间
             */
            addCookie(response,token,user);
        }

        return user;
    }

    /**
     * @param: 
     * describe: 将cookie存入缓存和浏览器中
     * creat_user: sl
     * creat_date: 2018/7/17
     * creat_time: 21:40
     **/
    private void addCookie(HttpServletResponse response,String token,KillUser user){
        redisService.set(LoginTokenKey.cookieKey,token,user);
        CookieUtil.set(response, CookieConstant.TOKEN,token,CookieConstant.EXPIRE);
    }

    @Override
    public void getAllUsers(){
        List<KillUser> userList = killUserMapper.getAllUsers();
        String urlString = "http://localhost:8080/secondKill/login/do_login";
        File file = new File("D:/tokens.txt");
        if(file.exists()){
            file.delete();
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file,"rw");
            file.createNewFile();
            randomAccessFile.seek(0);
            for (int i=0;i < userList.size();i++){
                KillUser killUser = userList.get(i);
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream out = conn.getOutputStream();
                String param = "mobile="+killUser.getId()+"&password="+MD5Util.inputPassToFormPass("123456");
                out.write(param.getBytes());
                out.flush();
                InputStream inputStream = conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte buff[] = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buff)) >= 0){
                    baos.write(buff,0,len);
                }
                inputStream.close();
                baos.close();
                String response = new String(baos.toByteArray());
                JSONObject jsonObject = JSONObject.parseObject(response);
                String token = jsonObject.getString("data");
                System.out.println("~~~~~~~~~~~~~~~~~~ClassName = KillUserServiceImpl ,METHOD_NAME = getAllUsers , create token = "+ killUser.getId());

                String row = killUser.getId() + "," + token;
                randomAccessFile.seek(randomAccessFile.length());
                randomAccessFile.write(row.getBytes());
                randomAccessFile.write("\r\n".getBytes());
                System.out.println("~~~~~~~~~~~~~~~~~~ClassName = KillUserServiceImpl ,METHOD_NAME = getAllUsers , write to file = "+killUser.getId());
            }
            randomAccessFile.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("over");

    }
}
