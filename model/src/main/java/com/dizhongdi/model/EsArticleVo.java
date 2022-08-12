package com.dizhongdi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * ClassName:EsArticleVo
 * Package:com.dizhongdi.model
 * Description:
 *      RabbitMQ传输给ES模块使用的vo类
 * @Date: 2022/8/10 15:50
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EsArticleVo {

    private String id;

    //帖子标题
    private String title;

    //文章内容
    private String description;


}
