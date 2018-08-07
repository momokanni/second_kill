package com.kill.util;

import com.kill.redis.GoodsKey;
import com.kill.redis.KeyPrefix;
import com.kill.redis.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 模板引擎类
 *
 * @author Administrator
 * @create 2018-07-30 17:23
 */
@Component
public class TemplateUtil {

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private RedisServiceImpl redisService;

    public String generateHtml(HttpServletRequest request, HttpServletResponse response, Map<String,Object> map,
                               String templateName, KeyPrefix prefix,String key){
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),map);
        String html = thymeleafViewResolver.getTemplateEngine().process(templateName,webContext);

        if(prefix != null){
            if(!StringUtils.isEmpty(html)){
                redisService.set(prefix,key,html);
            }
        }
        return html;
    }
}
