package com.dizhongdi.servicedzd.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.result.R;
import com.dizhongdi.servicedzd.entity.DzdArticle;
import com.dizhongdi.servicedzd.entity.vo.article.AticleQuery;
import com.dizhongdi.servicedzd.entity.vo.article.CreateArticleVo;
import com.dizhongdi.servicedzd.entity.vo.article.GetrAticleVo;
import com.dizhongdi.servicedzd.service.DzdArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-09
 */
@RestController
@RequestMapping("/admin/dzd/article/")
@Api(description="帖子后台管理")
public class DzdArticleController {

    @Autowired
    DzdArticleService dzdArticleService;
    @ApiOperation(value = "发布帖子")
    @PostMapping("/saveOrPush")
    public R posting(@RequestBody CreateArticleVo articleVo){

        //发布未成功报错
        if (!dzdArticleService.posting(articleVo)){
            return R.error().message("文章发布失败");
        }
        return R.ok().message("文章发布成功");
    }

    @ApiOperation(value = "更新帖子")
    @PutMapping("update/{id}")
    public R getById(@PathVariable String id,@RequestBody CreateArticleVo articleVo){
        boolean falg = dzdArticleService.updateInfo(id,articleVo);
        return R.ok();
    }

    @ApiOperation(value = "根据id获取帖子")
    @GetMapping("articleInfo/{id}")
    public R getById(@PathVariable String id){
        GetrAticleVo getrAticleVo = dzdArticleService.queryById(id);
        return R.ok().data("item",getrAticleVo);
    }


    @ApiOperation(value = "修改发布状态")
    @PutMapping("statusById/{id}")
    public R statusById(@PathVariable String id){
        if (dzdArticleService.updateStatus(id)){
            return R.ok();
        }
        return R.error();
    }
    @ApiOperation(value = "根据id删除帖子")
    @DeleteMapping("removeById/{id}")
    public R deleteById(@PathVariable String id){
        if (dzdArticleService.deleteByid(id)){
            return R.ok().message("删除成功");
        }
        return R.error().message("删除失败");
    }

    @ApiOperation(value = "分页获取帖子")
    @PostMapping("getPageList/{page}/{limit}")
    public R getList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "AticleQuery", value = "查询对象", required = false) AticleQuery articleQuery){
        Page<DzdArticle> articlePage = new Page<>(page,limit);
        IPage<DzdArticle> articleIPage = dzdArticleService.pageQuery(articlePage,articleQuery);
        return R.ok().data("items" , articleIPage.getRecords()).data("total",articleIPage.getTotal());
    }
}

