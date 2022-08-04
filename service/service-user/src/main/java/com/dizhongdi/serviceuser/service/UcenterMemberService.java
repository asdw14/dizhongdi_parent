package com.dizhongdi.serviceuser.service;

import com.dizhongdi.serviceuser.entity.LoginInfo;
import com.dizhongdi.serviceuser.entity.LoginVo;
import com.dizhongdi.serviceuser.entity.RegisterVo;
import com.dizhongdi.serviceuser.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-07-08
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    //    会员登录
    String login(LoginVo loginVo);
    //    会员注册
    void register(RegisterVo registerVo);
//    根据token获取用户信息
    LoginInfo getLoginInfo(String id);

    UcenterMember getByOpenid(String openid);

    //统计某一天的注册人数
    Integer countRegisterByDay(String day);
}
