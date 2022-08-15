package com.dizhongdi.servicees.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch.core.CreateRequest;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.dizhongdi.model.EsArticleVo;
import com.dizhongdi.servicees.service.ArticleEsService;
import com.dizhongdi.servicees.utils.ESClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:ArticleEsService
 * Package:com.dizhongdi.servicees.service.impl
 * Description:
 *      Article帖子的添加修改查询
 * @Date: 2022/8/10 17:32
 * @Author:dizhongdi
 */
@Service
public class ArticleEsServiceImpl implements ArticleEsService {

    //获取es client
    private ElasticsearchClient client;

    {
        try {
            client = ESClient.initESConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ARTICLE的新增和修改
    @Override
    public void articleSorU(EsArticleVo esArticleVo) throws IOException {
            CreateRequest<EsArticleVo> request = new CreateRequest.Builder<EsArticleVo>().
                    index("article")
                    .document(esArticleVo)
                    .id(esArticleVo.getId())
                    .build();

            //文档插入
            CreateResponse response = client.create(request);
            System.out.println(response.result());
    }

    @Override
    public void deleteArticle(String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest.Builder().index("article").id(id).build();
        System.out.println("数据删除成功：" + deleteRequest);

    }

    @Override
    public List<String> searchArticle(String keyword) throws IOException {
        ArrayList<String> ids = new ArrayList<>();
        HitsMetadata<EsArticleVo> hits = client.search(
                req -> {
                    req.query(
                            q ->
                                    q.match(
                                            m -> m.field("description").query(keyword).operator(Operator.Or).field("title").query(keyword)
                                    )
                    ).index("article");
                    return req;
                }
                , EsArticleVo.class
        ).hits();
        hits.hits().forEach(hit -> ids.add(hit.id()));
        return ids;
    }

    @Override
    public List<String> searchAll() {
        return null;
    }
}
