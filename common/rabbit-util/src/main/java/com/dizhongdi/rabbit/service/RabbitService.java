package com.dizhongdi.rabbit.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName:RabbitService
 * Package:com.dizhongdi.rabbit.service
 * Description:
 *
 * @Date: 2022/8/4 17:32
 * @Author:dizhongdi
 */
@Service
public class RabbitService {
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     *  发送消息
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param message 消息
     */
    public boolean sendMessage(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return true;
    }
}
