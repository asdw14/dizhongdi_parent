package com.dizhongdi.servicedzd.controller.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.result.R;
import com.dizhongdi.servicedzd.entity.DzdArticle;
import com.dizhongdi.servicedzd.entity.vo.article.*;
import com.dizhongdi.servicedzd.service.DzdArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/posting")
    public R posting(@RequestBody CreateArticleVo articleVo){

        //发布未成功报错
        if (!dzdArticleService.posting(articleVo)){
            return R.error().message("文章发布失败");
        }
        return R.ok().message("文章发布成功");
    }

    @ApiOperation(value = "根据id获取帖子")
    @GetMapping("/{id}")
    public R getById(@PathVariable String id){
        GetrAticleVo getrAticleVo = dzdArticleService.queryById(id);
        return R.ok().data("item",getrAticleVo);
    }

    @ApiOperation(value = "根据id删除帖子")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable String id){
        if (dzdArticleService.deleteByid(id)){
            return R.ok().message("删除成功");
        }
        return R.error().message("删除失败");
    }

    @ApiOperation(value = "修改帖子")
    @PutMapping("/{id}")
    public R update(@PathVariable String id, @RequestBody CreateArticleVo articleVo){

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
    @PostMapping("getAticleInfo/{id}")
    public R getAticleInfo(
            @ApiParam(name = "id", value = "id", required = true) @PathVariable String id) {
        ArticleInfoAllVo aticleInfo =  dzdArticleService.getAticleInfo(id);
        return R.ok().data("item" , aticleInfo);
    }
}

