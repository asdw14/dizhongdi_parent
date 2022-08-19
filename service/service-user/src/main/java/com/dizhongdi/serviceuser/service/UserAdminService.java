package com.dizhongdi.serviceuser.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dizhongdi.serviceuser.entity.UcenterMember;
import com.dizhongdi.model.AdminGetUserVo;
import com.dizhongdi.serviceuser.entity.vo.UserQuery;

import java.util.List;

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


    //分页查询用户所有信息
    List<AdminGetUserVo> pageQueryList(Page<UcenterMember> userPage, UserQuery userQuery);

    //根据id获取用户所有信息
    AdminGetUserVo queryById(String id);

}
