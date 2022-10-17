package com.dizhongdi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * ClassName:UserSourceDownLog
 * Package:com.dizhongdi.model
 * Description:
 *
 * @Date: 2022/10/17 16:27
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//用户下载资源记录
public class UserSourceDownLog {
    //用户id
    private String memberId;

    //资源id
    private String sourceId;

    //简短的资源描述
    private String sourceName;

    //文件大小
    private Double fileSize;

    //OSS云端保存资源URL
    private String sourceOssUrl;

    //下载时间
    private Date gmtModified;

}
