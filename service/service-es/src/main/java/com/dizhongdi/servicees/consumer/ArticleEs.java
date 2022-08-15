package com.dizhongdi.servicees.consumer;

import com.alibaba.fastjson.JSON;
import com.dizhongdi.model.EsArticleVo;
import com.dizhongdi.rabbit.config.MqConst;
import com.dizhongdi.servicees.service.ArticleEsService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ClassName:ArticleEs
 * Package:com.dizhongdi.servicees.consumer
 * Description:
 *
 * @Date: 2022/8/10 17:12
 * @Author:dizhongdi
 */

@Component
public class ArticleEs {
    @Autowired
    ArticleEsService articleEsService;

    //ARTICLE的新增和修改
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ARTICLE_SORU),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ARTICLE),
            key = {MqConst.ROUTING_ARTICLE_SORU}
    ))
    public void articleSorU(String message){
        System.out.println(message);
        EsArticleVo esArticleVo = JSON.parseObject(message, EsArticleVo.class);
        try {
            articleEsService.articleSorU(esArticleVo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //ARTICLE的删除
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ARTICLE_DELETE),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ARTICLE),
            key = {MqConst.ROUTING_ARTICLE_DELETE}
    ))
    public void deleteArticle(String id){

        String s = JSON.parseObject(id, String.class);
        System.out.println(s);
        try {
            articleEsService.deleteArticle(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
