package com.kill.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.LinkedList;

/**
 * druid配置类
 *
 * @author Administrator
 * @create 2018-07-04 18:37
 */
@Configuration
public class DruidConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(druidDataSource());
    }

    @ConfigurationProperties(prefix = "spring.datasource.druid")
    @Bean(initMethod = "init",destroyMethod = "close")
    public DruidDataSource druidDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        // 添加druid的监控过滤器，当前只演示监控的功能，因此只有一个过滤器，可以实现多个过滤器
        LinkedList<Filter> filterLinkedList = new LinkedList<>();
        filterLinkedList.add(filter());
        druidDataSource.setProxyFilters(filterLinkedList);

        return druidDataSource;
    }

    @Bean
    public Filter filter(){
        StatFilter statFilter = new StatFilter();
        // SQL执行时间超过2s的被判定为慢日志
        statFilter.setSlowSqlMillis(2000);
        //显示慢日志
        statFilter.setLogSlowSql(true);
        //合并SQL，相同的慢日志过多影响阅读，开启合并功能
        statFilter.setMergeSql(true);
        return statFilter;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        //注册自己的servlet
        return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
    }
}
