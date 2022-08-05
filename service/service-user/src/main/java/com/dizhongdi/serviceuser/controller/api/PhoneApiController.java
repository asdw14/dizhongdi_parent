package com.dizhongdi.serviceuser.controller.api;

import com.dizhongdi.rabbit.config.MyRabbitConfig;
import com.dizhongdi.rabbit.service.RabbitService;
import com.dizhongdi.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName:PhoneApiController
 * Package:com.dizhongdi.serviceuser.controller.api
 * Description:
 *
 * @Date: 2022/8/5 21:52
 * @Author:dizhongdi
 */
@RestController
@RequestMapping("/api/user/phone")
@Api(description="前台用户手机号")
public class PhoneApiController {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RabbitService rabbitService;

    @ApiOperation(value = "获取手机验证码")
    @GetMapping("send/{phone}")
    public R send(@PathVariable String phone) {
        rabbitService.sendMessage(MyRabbitConfig.MSM_EXCHANGE,"MSMA",phone);
        return R.ok();
    }

}
