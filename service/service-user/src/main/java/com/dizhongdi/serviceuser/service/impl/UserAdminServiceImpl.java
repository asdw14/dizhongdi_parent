package com.dizhongdi.serviceuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dizhongdi.serviceuser.entity.DzdCredit;
import com.dizhongdi.serviceuser.entity.DzdDatasize;
import com.dizhongdi.serviceuser.entity.UcenterMember;
import com.dizhongdi.model.AdminGetUserVo;
import com.dizhongdi.serviceuser.entity.vo.UserQuery;
import com.dizhongdi.serviceuser.mapper.UcenterMemberMapper;
import com.dizhongdi.serviceuser.service.DzdCreditService;
import com.dizhongdi.serviceuser.service.DzdDatasizeService;
import com.dizhongdi.serviceuser.service.UserAdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    DzdCreditService creditService;

    @Autowired
    DzdDatasizeService datasizeService;

    //封禁解封用户
    @Override
    public boolean disabledUser(String id) {
        UcenterMember user = this.getById(id);

        return this.updateById(user.getIsDisabled().equals(0) ? user.setIsDisabled(1) : user.setIsDisabled(0));
    }

    //分页获取用户所有的信息
    @Override
    public List<AdminGetUserVo> pageQueryList(Page<UcenterMember> userPage, UserQuery userQuery) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        List<AdminGetUserVo> list = new ArrayList<>();
        String id = userQuery.getId();
        if (userQuery!=null) {
            if (!StringUtils.isEmpty(id)) {
                wrapper.eq("id", id).or().eq("nickname", userQuery.getId());
            }

            //判断查询条件是否有封禁未封禁
            if (!StringUtils.isEmpty(userQuery.getIsDisabled())) {
                //如果为1查询已封禁的
                if (1 == userQuery.getIsDisabled()) {
                    wrapper.eq("is_disabled", 1);
                } else {
                    wrapper.eq("is_disabled", 0);
                }
            }
            //大于开始日期
            if (!StringUtils.isEmpty(userQuery.getBegin())) {
                wrapper.ge("gmt_create", userQuery.getBegin());

            }
        }
        wrapper.orderByDesc("gmt_create");

        IPage<UcenterMember> selectPage = baseMapper.selectPage(userPage, wrapper);
        selectPage.getRecords().stream().map(user -> {
            AdminGetUserVo userInfo = new AdminGetUserVo();
            BeanUtils.copyProperties(user,userInfo);
            //获取用户空间
            DzdDatasize datasize = datasizeService.getById(user.getId());
            if ( datasize != null)
                userInfo.setDatasize(datasize.getDatasize());
            //获取用户积分
            DzdCredit credit = creditService.getById(user.getId());
            return credit == null ? userInfo : userInfo.setCredit(credit.getCredit()).setQuantity(credit.getQuantity());
        }).forEach( user -> list.add(user) );
        return list;
    }


    //根据id获取用户所有信息
    @Override
    public AdminGetUserVo queryById(String id) {
        //获取用户基本信息
        UcenterMember user = this.getById(id);
        AdminGetUserVo userInfo = new AdminGetUserVo();
        BeanUtils.copyProperties(user,userInfo);
        //获取用户空间
        DzdDatasize datasize = datasizeService.getById(user.getId());
        if(datasize != null){
            userInfo.setDatasize(datasize.getDatasize());
        }

        //获取用户积分
        DzdCredit credit = creditService.getById(user.getId());
        if(credit != null){
            userInfo.setCredit(credit.getCredit()).setQuantity(credit.getQuantity());

        }
        //返回用户全部信息
        return userInfo;
    }


}
