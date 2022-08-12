package com.dizhongdi.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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

    // 声明 articleExchange
    @Bean("articleExchange")
    public DirectExchange articleExchange(){
        return new DirectExchange(MqConst.EXCHANGE_DIRECT_ARTICLE);
    }

    //ES帖子增改队列
    @Bean("queueArticleSORU")
    public Queue queueArticleSORU(){
        return QueueBuilder.durable(MqConst.QUEUE_ARTICLE_SORU).build();
    }

    //ES帖子查询队列
    @Bean("queueArticleQuery")
    public Queue queueArticleQuery(){
        return QueueBuilder.durable(MqConst.QUEUE_ARTICLE_QUERY).build();
    }

    //ES帖子删除队列
    @Bean("queueArticleDelete")
    public Queue queueArticleDelete(){
        return QueueBuilder.durable(MqConst.QUEUE_ARTICLE_DELETE).build();
    }

    // 声明增改队列 queueArticleSORU 绑定 articleExchange 交换机
    @Bean
    public Binding queueSoruBindingArticle(@Qualifier("queueArticleSORU") Queue queueArticleSORU,
                                             @Qualifier("articleExchange") DirectExchange articleExchange){
        return BindingBuilder.bind(queueArticleSORU).to(articleExchange).with(MqConst.ROUTING_ARTICLE_SORU);
    }

    // 声明增改队列 queueArticleQuery 绑定 articleExchange 交换机
    @Bean
    public Binding queueQueryBindingArticle(@Qualifier("queueArticleQuery") Queue queueArticleQuery,
                                    @Qualifier("articleExchange") DirectExchange articleExchange){
        return BindingBuilder.bind(queueArticleQuery).to(articleExchange).with(MqConst.ROUTING_ARTICLE_QUERY);
    }

    // 声明增改队列 queueArticleDelete 绑定 articleExchange 交换机
    @Bean
    public Binding queueDeleteBindingArticle(@Qualifier("queueArticleDelete") Queue queueArticleDelete,
                                           @Qualifier("articleExchange") DirectExchange articleExchange){
        return BindingBuilder.bind(queueArticleDelete).to(articleExchange).with(MqConst.ROUTING_ARTICLE_DELETE);
    }


}
