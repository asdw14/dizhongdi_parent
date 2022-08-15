package com.dizhongdi.servicees.controller;

import com.dizhongdi.result.R;
import com.dizhongdi.servicees.service.ArticleEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * ClassName:ArticleController
 * Package:com.dizhongdi.servicees.controller
 * Description:
 *
 * @Date: 2022/8/15 18:45
 * @Author:dizhongdi
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleEsService articleEsService;
    @RequestMapping("/search/{keyword}")
    public R searchArticle(@PathVariable String keyword) throws IOException {
        List<String> idList = articleEsService.searchArticle(keyword);
        return R.ok().data("ids",idList);
    }

    @RequestMapping("/searchAll")
    public R searchAll() throws IOException {
        List<String> idList = articleEsService.searchAll();
        return R.ok().data("ids",idList);
    }
}
