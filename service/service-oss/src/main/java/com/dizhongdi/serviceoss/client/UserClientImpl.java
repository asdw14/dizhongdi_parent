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

    //根据用户id获取剩余容量
    @Override
    public Double getDatasizeByMemberId(String memberId) {
        return 0.0;
    }

    //增加用户剩余容量
    @Override
    public boolean addDatasize(String memberId, Double size) {
        return true;
    }

    //减少用户剩余容量
    @Override
    public boolean cutDatasize(String memberId, Double size) {
        return true;
    }
}
