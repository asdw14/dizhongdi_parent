package com.dizhongdi.serviceuser.client;

import com.dizhongdi.model.ArticleStarLogByUser;
import com.dizhongdi.model.ArticleViewLogByUser;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName:DzdArticleClientImpl
 * Package:com.dizhongdi.serviceuser.client
 * Description:
 *
 * @Date: 2022/10/17 15:23
 * @Author:dizhongdi
 */
@Component
public class DzdArticleClientImpl implements DzdArticleClient {
    @Override
    public List<ArticleViewLogByUser> getArticleViewByUserId(String memberId) {
        return null;
    }

    @Override
    public List<ArticleStarLogByUser> getArticleStarByUserId(String memberId) {
        return null;
    }
}
