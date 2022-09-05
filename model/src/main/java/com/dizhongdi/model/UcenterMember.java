package com.dizhongdi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author dizhongdi
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UcenterMember implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String openid;

    private String mobile;

    private String password;
    private String nickname;

    private Integer sex;

    private Integer age;

    private String avatar;

    private String sign;

    private Integer isDisabled;

    private Integer isDeleted;

    private Date gmtCreate;

    private Date gmtModified;


}
