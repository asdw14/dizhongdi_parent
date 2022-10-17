package com.dizhongdi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * ClassName:ArticleStarLogByUser
 * Package:com.dizhongdi.model
 * Description:
 *
 * @Date: 2022/10/17 15:08
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ArticleStarLogByUser {
    //发帖用户id
    private String memberId;

    //帖子id
    private String articleId;

    //    帖子标题
    private String title;

    //    文章前几句大概
    private String summary;

    //点赞时间
    private Date gmtModified;
}
