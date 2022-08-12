package com.dizhongdi.rabbit.config;

/**
 * ClassName:MqConst
 * Package:com.dizhongdi.rabbit.config
 * Description:
 *
 * @Date: 2022/8/10 16:28
 * @Author:dizhongdi
 */
public class MqConst {
    /**
     * ES帖子新增修改
     */
    public static final String EXCHANGE_DIRECT_ARTICLE = "exchange.direct.article";
    public static final String ROUTING_ARTICLE_SORU = "saveOrUpdate";
    //增修队列
    public static final String QUEUE_ARTICLE_SORU  = "queue.article.sOru";
    /**
     * ES帖子查询
     */
    public static final String ROUTING_ARTICLE_QUERY = "query";

    //查找队列
    public static final String QUEUE_ARTICLE_QUERY  = "queue.article.query";

    /**
     * ES帖子删除
     */
    public static final String ROUTING_ARTICLE_DELETE = "delete";

    //查找队列
    public static final String QUEUE_ARTICLE_DELETE  = "queue.article.delete";

}
