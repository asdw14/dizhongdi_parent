package com.dizhongdi.servicedzd.controller.api;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.model.ArticleStarLogByUser;
import com.dizhongdi.model.ArticleViewLogByUser;
import com.dizhongdi.result.R;
import com.dizhongdi.servicebase.utils.IpUtils;
import com.dizhongdi.servicedzd.entity.ArticleViewLog;
import com.dizhongdi.servicedzd.entity.DzdArticle;
import com.dizhongdi.servicedzd.entity.vo.article.*;
import com.dizhongdi.servicedzd.entity.vo.comment.PushCommentVo;
import com.dizhongdi.servicedzd.service.ArticleViewLogService;
import com.dizhongdi.servicedzd.service.DzdArticleService;
import com.dizhongdi.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-09
 */
@RestController
@RequestMapping("/api/dzd/article")
@Api(description="帖子前台")
public class DzdArticleController {

    @Autowired
    DzdArticleService dzdArticleService;


    @ApiOperation(value = "发布帖子")
    @PostMapping("/saveOrPush")
    public R posting(@RequestBody CreateArticleVo articleVo,HttpServletRequest request){
        System.out.println(articleVo.getMarkdown());
        //验证用户是否登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println(memberId);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("请先登录后在进行发帖！");
        }
        //放入用户id
        articleVo.setMemberId(memberId);
        //发布未成功报错
        if (!dzdArticleService.posting(articleVo)){
            return R.error();
        }
        return R.ok();
    }

    @ApiOperation(value = "根据id删除帖子")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable String id,HttpServletRequest request){
        //验证用户是否登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println(memberId);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("请先进行登录再进行操作！");
        }
        if (dzdArticleService.deleteByid(id)){
            return R.ok().message("删除成功");
        }
        return R.error().message("删除失败");
    }

    @ApiOperation(value = "修改帖子")
    @PutMapping("/{id}")
    public R update(@PathVariable String id, @RequestBody CreateArticleVo articleVo,HttpServletRequest request){

        //修改未成功报错
        if (!dzdArticleService.updateArticle(articleVo)){
            return R.error().message("文章修改失败");
        }
        return R.ok().message("文章修改成功");
    }

    @ApiOperation(value = "前台分页获取帖子")
    @PostMapping("getPageList/{page}/{limit}")
    public R getUserPageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "AticleQueryVo", value = "查询对象", required = false)
            @RequestBody AticleQueryVo articleQuery){

        Page<DzdArticle> articlePage = new Page<>(page,limit);
        List<GetAllAticleVo> allAticleList = dzdArticleService.pageAllArticleQuery(articlePage,articleQuery);
        return R.ok().data("items" , allAticleList).data("total",articlePage.getTotal());
    }

    @ApiOperation(value = "前台获取帖子所有信息")
    @PostMapping("getArticleInfo/{id}")
    public R getArticleInfo(
            @ApiParam(name = "id", value = "id", required = true) @PathVariable String id, HttpServletRequest request) {
        ArticleInfoAllVo articleInfo =  dzdArticleService.getAticleInfo(id);
        String ipAddr = IpUtils.getIpAddress(request);
        System.out.println("===================================================="+ipAddr);
        //验证用户是否登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询用户是否点赞过该帖子
        if (!StringUtils.isEmpty(memberId)){
            boolean isStar = dzdArticleService.getIsStar(id,memberId);
            if (isStar)
                articleInfo.setStarIs(isStar);
        }

        //根据用户id增加浏览次数，一个用户增加一次
        dzdArticleService.addArticleViewCountByMemberId(id,memberId);
        return R.ok().data("item" , articleInfo);
    }

    @PutMapping("articleStar/{articleId}")
    @ApiOperation(value = "点赞和撤销通用")
    public R articleStar( @PathVariable String articleId , HttpServletRequest request){

        //验证用户是否登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println(memberId);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("请先登录后在进行点赞！");
        }

        //返回true是点赞，false是没点赞
        boolean flag = dzdArticleService.articleStar(articleId, memberId);

        return R.ok().data("starIs",flag);

    }

    @ApiOperation(value = "前台根据用户id查询帖子浏览记录")
    @GetMapping("getArticleViewByUserId/{memberId}")
    public List<ArticleViewLogByUser> getArticleViewByUserId(@PathVariable String memberId){
        return dzdArticleService.getArticleViewByUserId(memberId);
    }

    @ApiOperation(value = "前台根据用户id查询帖子点赞记录")
    @GetMapping("getArticleStarByUserId/{memberId}")
    public List<ArticleStarLogByUser> getArticleStarByUserId(@PathVariable String memberId){
        return dzdArticleService.getArticleStarByUserId(memberId);
    }

}

