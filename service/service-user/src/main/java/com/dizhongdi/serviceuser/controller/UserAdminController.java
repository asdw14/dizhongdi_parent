package com.dizhongdi.serviceuser.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.result.R;
import com.dizhongdi.serviceuser.entity.UcenterMember;
import com.dizhongdi.model.AdminGetUserVo;
import com.dizhongdi.serviceuser.entity.vo.UserQuery;
import com.dizhongdi.serviceuser.service.DzdCreditService;
import com.dizhongdi.serviceuser.service.UserAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName:UserAdminController
 * Package:com.dizhongdi.serviceuser.controller
 * Description:
 *
 * @Date: 2022/8/17 17:16
 * @Author:dizhongdi
 */
@RequestMapping("/admin/user")
@Api(description="用户后台管理")
@RestController
public class UserAdminController {
    @Autowired
    UserAdminService userAdminService;
    @Autowired
    DzdCreditService creditService;

    @ApiOperation(value = "根据id注销用户")
    @DeleteMapping("removeById/{id}")
    public R deleteById(@PathVariable String id){
        userAdminService.removeById(id);
        creditService.removeById(id);
        return R.ok();
    }


    @ApiOperation(value = "分页获取用户所有的信息")
    @PostMapping("getPageList/{page}/{limit}")
    public R getPageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "UserQuery", value = "查询对象", required = false)
            @RequestBody UserQuery userQuery){
        Page<UcenterMember> articlePage = new Page<>(page,limit);
        List<AdminGetUserVo> queryList = userAdminService.pageQueryList(articlePage,userQuery);
        return R.ok().data("items" , queryList).data("total",articlePage.getTotal());
    }

    @ApiOperation(value = "根据id获取用户所有的信息")
    @GetMapping("userAllInfo/{id}")
    public AdminGetUserVo getAllInfoId(@PathVariable String id){
        return userAdminService.queryById(id);
    }


    //获取所有用户信息
    @PostMapping("getAllMember")
    public List<UcenterMember> getAllMember(){
        return userAdminService.list(new QueryWrapper<UcenterMember>());
    }
}
