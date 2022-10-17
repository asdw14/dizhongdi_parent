package com.dizhongdi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * ClassName:ArticleViewLogByUser
 * Package:com.dizhongdi.model
 * Description:
 *
 * @Date: 2022/10/17 14:20
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ArticleViewLogByUser {


    //发帖用户id
    private String memberId;

    //    昵称
    private String nickname;

    //头像
    private String avatar;

    //帖子id
    private String articleId;

//    帖子标题
    private String title;

//    文章前几句大概
    private String summary;

    //浏览时间
    private Date gmtModified;
}
