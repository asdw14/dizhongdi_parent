package com.dizhongdi.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:MyRabbitConfig
 * Package:com.dizhongdi.rabbit.config
 * Description:
 *
 * @Date: 2022/8/4 17:21
 * @Author:dizhongdi
 */
@Configuration
public class MyRabbitConfig {
    //普通消费机
    public static final String MSM_EXCHANGE = "MSM";
    //普通队列
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";


    // 声明 MSMExchange
    @Bean("msmExchange")
    public DirectExchange msmExchange(){
        return new DirectExchange(MSM_EXCHANGE);
    }

    @Bean("queueA")
    public Queue queueA(){
        return QueueBuilder.durable(QUEUE_A).build();
    }

    @Bean("queueB")
    public Queue queueB(){
        return QueueBuilder.durable(QUEUE_B).build();
    }

    // 声明队列 A 绑定 X 交换机
    @Bean
    public Binding queueABindingMSM(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("msmExchange") DirectExchange MsmExchange){
        return BindingBuilder.bind(queueA).to(MsmExchange).with("MSMA");
    }

    // 声明队列 B 绑定 X 交换机
    @Bean
    public Binding queueBBindingMSM(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("msmExchange") DirectExchange MsmExchange){
        return BindingBuilder.bind(queueB).to(MsmExchange).with("MSMB");
    }

}
