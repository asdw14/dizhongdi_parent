package com.dizhongdi.servicees.service;

import com.dizhongdi.model.EsArticleVo;

import java.io.IOException;
import java.util.List;

/**
 * ClassName:ArticleEs
 * Package:com.dizhongdi.servicees.consumer
 * Description:
 *
 * @Date: 2022/8/10 17:12
 * @Author:dizhongdi
 */


public interface ArticleEsService {
    //ARTICLE的新增和修改
    public void articleSorU(EsArticleVo esArticleVo) throws IOException;

    //ARTICLE的删除
    public void deleteArticle(String id) throws IOException;

    //ARTICLE的搜索
    public List<String> searchArticle(String keyword) throws IOException;

    List<String> searchAll();
}
