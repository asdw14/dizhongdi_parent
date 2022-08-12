package com.dizhongdi.servicees;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ClassName:User
 * Package:com.dizhongdi.es8
 * Description:
 *
 * @Date: 2022/7/29 23:16
 * @Author:dizhongdi
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {
    private Integer id;
    private String name;
    private Integer age;
}
