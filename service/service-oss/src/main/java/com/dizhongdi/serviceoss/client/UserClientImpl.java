package com.dizhongdi.serviceoss.client;

import com.dizhongdi.model.AdminGetUserVo;
import org.springframework.stereotype.Component;

/**
 * ClassName:UserClientImpl
 * Package:com.dizhongdi.servicedzd.client
 * Description:
 *
 * @Date: 2022/8/19 17:00
 * @Author:dizhongdi
 */
@Component
public class UserClientImpl implements UserClient {
    @Override
    public AdminGetUserVo getAllInfoId(String id) {
        return new AdminGetUserVo().setNickname("加载失败");
    }
}
