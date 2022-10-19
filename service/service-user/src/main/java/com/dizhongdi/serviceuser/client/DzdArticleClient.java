package com.dizhongdi.serviceuser.client;

import com.dizhongdi.model.ArticleStarLogByUser;
import com.dizhongdi.model.ArticleViewLogByUser;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * ClassName:DzdArticleClient
 * Package:com.dizhongdi.serviceuser.client
 * Description:
 *
 * @Date: 2022/10/17 15:21
 * @Author:dizhongdi
 */
@FeignClient(value = "service-dzd" ,fallback = DzdArticleClientImpl.class)
@Component
public interface DzdArticleClient {

    //前台根据用户id查询帖子浏览记录
    @GetMapping("/api/dzd/article/getArticleViewByUserId/{memberId}")
    public List<ArticleViewLogByUser> getArticleViewByUserId(@PathVariable String memberId);

    //前台根据用户id查询帖子点赞记录
    @GetMapping("/api/dzd/article/getArticleStarByUserId/{memberId}")
    public List<ArticleStarLogByUser> getArticleStarByUserId(@PathVariable String memberId);
}
