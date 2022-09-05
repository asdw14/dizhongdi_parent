package com.dizhongdi.serviceuser.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dizhongdi.result.R;
import com.dizhongdi.serviceuser.entity.LoginInfo;
import com.dizhongdi.serviceuser.entity.LoginVo;
import com.dizhongdi.serviceuser.entity.RegisterVo;
import com.dizhongdi.serviceuser.entity.UcenterMember;
import com.dizhongdi.serviceuser.service.UcenterMemberService;
import com.dizhongdi.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ClassName:MemberApiController
 * Package:com.dizhongdi.serviceuser.controller.api
 * Description:
 *
 * @Date: 2022/7/8 0:23
 * @Author:dizhongdi
 */
@RestController
@RequestMapping("/api/user")
@Api(description="前台用户")
public class MemberApiController {
    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo) {
        String token = memberService.login(loginVo);
        return R.ok().data("token", token);
    }

    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("auth/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request){
        System.out.println(request.getCookies());
        String id = JwtUtils.getMemberIdByJwtToken(request);
        LoginInfo loginInfo = memberService.getLoginInfo(id);
        System.out.println(loginInfo.toString());
        return R.ok().data("item",loginInfo);
    }

    //根据id获取用户信息，返回用户信息对象
    @PostMapping("getInfoUc/{id}")
    public UcenterMember getInfo(@PathVariable String id) {
        UcenterMember ucenterMember = memberService.getById(id);
        return ucenterMember;
    }

//    统计某一天的注册人数
    @GetMapping(value = "countregister/{day}")
    public Integer registerCount(
            @PathVariable String day){
        Integer count = memberService.countRegisterByDay(day);
        return count;
    }
}
