package com.dizhongdi.servicemsm.consumer;

import com.dizhongdi.rabbit.config.MyRabbitConfig;
import com.dizhongdi.result.R;
import com.dizhongdi.servicemsm.service.MsmService;
import com.dizhongdi.servicemsm.service.impl.MsmServiceImpl;
import lombok.experimental.Accessors;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:MsmConsumer
 * Package:com.dizhongdi.servicemsm.service
 * Description:
 *
 * @Date: 2022/8/4 17:44
 * @Author:dizhongdi
 */
@Component
public class MsmConsumer {
    @Autowired
    MsmService msmService;
    @Autowired
    RedisTemplate redisTemplate;

    //阿里云云市场的api
    @RabbitListener(queues = MyRabbitConfig.QUEUE_A)
    public void sendMsg(Message message){
        String phone = new String(message.getBody());
        System.out.println(phone);
        //从redis获取验证码，如果获取获取到，返回ok
        // key 手机号  value 验证码
        String code = (String) redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)){
            return;
        }
        //如果从redis获取不到，
        // 生成验证码，
        code = msmService.getCode();
        if (msmService.aliSend(phone,code)){
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            System.out.println(phone + "发送验证码成功");
        }else {
            //失败重发一次
            msmService.aliSend(phone,code);
        }
    }
//
//    @RabbitListener(queues = MyRabbitConfig.QUEUE_A)
//    public void sendMsg(Message message){
//        String phone = new String(message.getBody());
//        System.out.println(phone);
//        //从redis获取验证码，如果获取获取到，返回ok
//        // key 手机号  value 验证码
//        String code = (String) redisTemplate.opsForValue().get(phone);
//        if (!StringUtils.isEmpty(code)){
//            return;
//        }
//        //如果从redis获取不到，
//        // 生成验证码，
//        code = msmService.getCode();
//        if (msmService.send(phone,"1",code)){
//            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
//            System.out.println(phone + "发送验证码成功");
//        }
//    }


}
