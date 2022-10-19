package com.dizhongdi.serviceuser.controller.api;

import com.dizhongdi.model.AdminGetUserVo;
import com.dizhongdi.model.ArticleStarLogByUser;
import com.dizhongdi.model.ArticleViewLogByUser;
import com.dizhongdi.model.UserSourceDownLog;
import com.dizhongdi.result.R;
import com.dizhongdi.serviceuser.client.DzdArticleClient;
import com.dizhongdi.serviceuser.client.SourceClient;
import com.dizhongdi.serviceuser.entity.LoginInfo;
import com.dizhongdi.serviceuser.entity.LoginVo;
import com.dizhongdi.serviceuser.entity.RegisterVo;
import com.dizhongdi.serviceuser.entity.UcenterMember;
import com.dizhongdi.serviceuser.service.UcenterMemberService;
import com.dizhongdi.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;
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

    @Autowired
    private DzdArticleClient dzdArticleClient;

    @Autowired
    private SourceClient sourceClient;

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
    @ApiOperation(value = "根据id获取用户信息，返回用户信息对象")
    @Cacheable(value = "userInfoById")
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

    @GetMapping(value = "getArticleView")
    @ApiOperation(value = "前台根据用户id查询帖子浏览记录")
    public R getArticleView(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("请先登录后在进行查看浏览记录！");
        }
//        System.out.println(memberId);
        List<ArticleViewLogByUser> viewByUserId = dzdArticleClient.getArticleViewByUserId(memberId);
        if (viewByUserId==null){
            return R.ok().data("items",null).message("没有浏览记录");
        }
        viewByUserId.forEach(articleView->{
            UcenterMember user = memberService.getById(articleView.getMemberId());
            if (user!=null){
                //发帖人头像
                articleView.setAvatar(user.getAvatar());
                //发帖人昵称
                articleView.setNickname(user.getNickname());
            }
        });
        return R.ok().data("items",viewByUserId);
    }

    @GetMapping(value = "getArticleStar")
    @ApiOperation(value = "前台根据用户id查询帖子点赞记录")
    public R getArticleStar(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("请先登录后在进行查看浏览记录！");
        }
//        System.out.println(memberId);
        List<ArticleStarLogByUser> articleStarByUserId = dzdArticleClient.getArticleStarByUserId(memberId);
        if (articleStarByUserId==null){
            return R.ok().data("items",null).message("没有点赞记录");
        }
        articleStarByUserId.forEach(articleView->{
            UcenterMember user = memberService.getById(articleView.getMemberId());
            if (user!=null){
                //发帖人头像
                articleView.setAvatar(user.getAvatar());
                //发帖人昵称
                articleView.setNickname(user.getNickname());
            }
        });
        return R.ok().data("items",articleStarByUserId);
    }

    @GetMapping(value = "getDownLog")
    @ApiOperation(value = "前台根据用户id查询下载记录")
    public R getDownLog(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("请先登录后在进行查看下载记录！");
        }
//        System.out.println(memberId);
        List<UserSourceDownLog> downLogs = sourceClient.getSourceDownByUserId(memberId);
        if (downLogs == null){
            return R.ok().data("items",null).message("没有记录");
        }

        return R.ok().data("items",downLogs);
    }

    @GetMapping(value = "getUserInfo")
    @ApiOperation(value = "前台查询用户所有信息")
    public R getUserInfo(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("您还没进行登录！");
        }
        AdminGetUserVo member = memberService.getUserInfo(memberId);

        return R.ok().data("item",member);
    }
}
