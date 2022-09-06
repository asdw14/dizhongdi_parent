package com.dizhongdi.servicemsm.consumer;

import com.dizhongdi.result.R;
import com.dizhongdi.servicemsm.service.MsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:SendController
 * Package:com.dizhongdi.servicemsm.consumer
 * Description:
 *
 * @Date: 2022/9/6 23:06
 * @Author:dizhongdi
 */
//@RestController
//@RequestMapping
public class SendController {
//
//    @Autowired
//    RedisTemplate redisTemplate;
//
//    @Autowired
//    MsmService msmService;


//    @GetMapping("send/{phone}")
//    public R sendMsg(@PathVariable String phone){
//
//        System.out.println(phone);
//        //从redis获取验证码，如果获取获取到，返回ok
//        // key 手机号  value 验证码
//        String code = (String) redisTemplate.opsForValue().get(phone);
//        if (!StringUtils.isEmpty(code)){
//            return R.ok().message("之前就发过了");
//        }
//        //如果从redis获取不到，
//        String messge = msmService.aliSend(phone);
//        return R.ok().message(messge);
//    }
}
