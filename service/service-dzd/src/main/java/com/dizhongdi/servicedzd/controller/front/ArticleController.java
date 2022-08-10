package com.dizhongdi.servicedzd.controller.front;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.rabbit.service.RabbitService;
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

import java.util.List;
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
@RequestMapping("/api/article/")
@Api(description="帖子前台")
public class ArticleController {

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

    @ApiOperation(value = "分页获取帖子")
    @PostMapping("{page}/{limit}")
    public R getList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "AticleQuery", value = "查询对象", required = false) AticleQuery query){
        Page<DzdArticle> pageParam = new Page<>();

        Map<String,Object> pageList =  dzdArticleService.pageList(pageParam,query);
        return R.ok().data("items",pageList);
    }
}

