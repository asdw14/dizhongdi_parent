package com.dizhongdi.servicedzd.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.result.R;
import com.dizhongdi.servicedzd.entity.CommentStar;
import com.dizhongdi.servicedzd.entity.DzdArticle;
import com.dizhongdi.servicedzd.entity.DzdComment;
import com.dizhongdi.servicedzd.entity.vo.comment.CommentInfoVo;
import com.dizhongdi.servicedzd.service.CommentStarService;
import com.dizhongdi.servicedzd.service.DzdCommentService;
import com.dizhongdi.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-24
 */
@RestController
@RequestMapping("/admin/dzd/comment")
@Api(description="评论后台管理")
public class AdminCommentController {

    @Autowired
    DzdCommentService commentService;

    @Autowired
    CommentStarService commentStarService;

    @DeleteMapping("/deleteCommentById/{id}")
    @ApiOperation(value = "删除评论")
    public R deleteCommentById(@ApiParam(name = "id", value = "帖子id", required = true)
                           @PathVariable String id){
        if (commentService.deleteComment(id)){
            commentService.removeByParentId(id);
            Wrapper wrapper = new QueryWrapper<CommentStar>().eq("comment_id", id);
            commentStarService.remove(wrapper);
            return  R.ok().message("删除成功");

        }
        return R.error().message("删除失败");
    }

    //根据帖子id获取评论
    @GetMapping("getAllByArticle/{articleId}")
    @ApiOperation(value = "根据帖子id获取所有评论")
    public R getAllByArticle(@PathVariable String articleId) {
        List<CommentInfoVo> comments = commentService.getAllByArticle(articleId);
        return R.ok().data("items",comments);
    }

    //分页获取所有评论
    @GetMapping("getAllPageList/{page}/{limit}")
    @ApiOperation(value = "根据帖子id获取所有评论")
    public R getAllPageList(@ApiParam(name = "page", value = "当前页码", required = true)
                                @PathVariable Long page,

                            @ApiParam(name = "limit", value = "每页记录数", required = true)
                                @PathVariable Long limit
                            ) {
        Page<DzdComment> iPage = new Page<>(page,limit);
        Wrapper wrapper = new QueryWrapper<DzdComment>().orderByDesc("gmt_create");
        IPage page1 = commentService.page(iPage, wrapper);
        List<DzdComment> records = page1.getRecords();
        return R.ok().data("items",records).data("total",page1.getTotal());
    }

}

