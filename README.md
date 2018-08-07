# second_kill
### 概要说明：
>1、前端：Thymeleaf...</br>
>2、后端：springBoot2.0、mybatis、lombok...</br>
>3、MQ：RabbitMQ</br>
>4、缓存：Redis</br>
>5、连接池：Druid</br>
>6、压测：JMeter</br>
>7、接口文档: swagger

### 技术点：
>1、重写Jedis方法，对key值统一管理</br>
>2、响应格式统一管理。</br>
>3、异常统一处理（认证授权异常、其他运行时异常）</br>
>4、Jsr303参数校验</br>
>5、明文密码两次加密加盐</br>
>6、页面静态化</br>
>7、热点数据对象加入缓存</br>
>8、redis分布式锁及预减库存</br>
>9、rabbitmq异步下单</br>
>10、logback对日志统一管理</br>
>11、对秒杀地址进行隐藏</br>
>12、接口防刷，本例共采用两种方式（1、nginx,2、拦截器+缓存）</br>
>13、JMeter压测,结果：2核/4GB/2M带宽，2143个不同用户，QPS: 3196/s(优化前), 6210(优化后)</br>
>14、ThreadLocal对请求秒杀对象进行统一管理


## 秒杀涉及到的知识点真的很多！！！！
