package com.dizhongdi.serviceuser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dizhongdi.serviceuser.entity.UcenterMember;
import com.dizhongdi.serviceuser.mapper.UcenterMemberMapper;
import com.dizhongdi.serviceuser.service.UserAdminService;
import org.springframework.stereotype.Service;

/**
 * ClassName:UserAdminServiceImpl
 * Package:com.dizhongdi.serviceuser.service.impl
 * Description:
 *
 * @Date: 2022/8/17 17:18
 * @Author:dizhongdi
 */
@Service
public class UserAdminServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UserAdminService {

    //封禁解封用户
    @Override
    public boolean disabledUser(String id) {
        UcenterMember user = this.getById(id);

        return this.updateById(user.getIsDisabled().equals(0) ? user.setIsDisabled(1) : user.setIsDisabled(0));
    }


}
