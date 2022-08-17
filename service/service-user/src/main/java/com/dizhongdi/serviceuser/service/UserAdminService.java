package com.dizhongdi.serviceuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dizhongdi.serviceuser.entity.UcenterMember;

/**
 * ClassName:UserAdminService
 * Package:com.dizhongdi.serviceuser.service
 * Description:
 *
 * @Date: 2022/8/17 17:18
 * @Author:dizhongdi
 */
public interface UserAdminService extends IService<UcenterMember> {
    //封禁解封用户
    boolean disabledUser(String id);


}
