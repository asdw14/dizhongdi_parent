package com.dizhongdi.serviceuser.controller.api;


import com.dizhongdi.serviceuser.service.DzdCreditService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  用户积分
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-17
 */
@RestController
@RequestMapping("/api/user/credit")
public class ApiCreditController {

    @Autowired
    DzdCreditService creditService;

    @PostMapping("getQuantityById/{memberId}")
    @ApiOperation(value = "根据用户id获取剩余下载次数")
    public Integer getQuantityById(@PathVariable String memberId){
        return creditService.getById(memberId).getQuantity();
    }

}

