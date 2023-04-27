package com.imooc;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * 1，定义交换机
     * 2，定义队列
     * 3，创建交换机
     * 4，创建队列
     * 5，队列和交换机的绑定
     */

//    ctrl+shift+u: 转为大写
    // 1，定义交换机
    public  static  final  String  EXCHANGE_MST="exchange_mst";

    // 2，定义队列
    public  static  final  String QUEUE_SYS_MSG="queue_sys_msg";

    // 3，创建交换机, 要将交换机放置到springboot中，需要用到bean这个注解
    @Bean(EXCHANGE_MST)
    public Exchange exchange(){
        // 构建交换机，使用topic类型,设置持久化durable(true)，重启后依然存在
        return ExchangeBuilder.topicExchange(EXCHANGE_MST).durable(true).build();
    }

    //4，创建队列
    @Bean(QUEUE_SYS_MSG)
    public Queue queue(){
        return new Queue(QUEUE_SYS_MSG,true);
    }

    //5，队列和交换机的绑定
    @Bean
    public Binding binding(@Qualifier(EXCHANGE_MST) Exchange  exchange, @Qualifier(QUEUE_SYS_MSG) Queue queue){
        // 使用在容器中的这两个对象,with代表routingkey(定义路由规则，类似于requestMapping)
        return BindingBuilder.bind(queue).to(exchange).with("sys.msg.*").noargs();
    }
}
