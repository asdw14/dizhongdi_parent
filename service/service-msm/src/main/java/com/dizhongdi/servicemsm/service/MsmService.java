package com.dizhongdi.servicemsm.service;

/**
 * ClassName:MsmService
 * Package:com.dizhongdi.servicemsm.service.impl
 * Description:
 *
 * @Date: 2022/7/7 23:36
 * @Author:dizhongdi
 */
public interface MsmService {
    //发送手机验证码
    public boolean send(String phone, String templateCode,String code);
    public boolean aliSend(String phone,String code);

    public String getCode();
}
