package com.dizhongdi.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName:AdminGetUserVo
 * Package:com.dizhongdi.serviceuser.entity.vo
 * Description:
 *
 * @Date: 2022/8/18 21:33
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminGetUserVo {
    private String id;

    private String openid;

    private String mobile;

    private String nickname;

    private Long credit;

    private BigDecimal datasize;

    private BigDecimal surplus;

    private Integer sex;

    private Integer age;

    private String avatar;

    private Integer isDisabled;


    private Date gmtCreate;

    private Date gmtModified;
}
