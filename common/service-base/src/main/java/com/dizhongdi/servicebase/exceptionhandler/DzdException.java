package com.dizhongdi.servicebase.exceptionhandler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName:DzdException
 * Package:com.dizhongdi.com.dizhongdi.servicebase.exceptionhandler
 * Description:
 *
 * @Date: 2022/5/9 22:10
 * @Author:dizhongdi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DzdException extends RuntimeException {

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @Override
    public String toString() {
        return "DzdException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    private String msg;

}
