package com.dizhongdi.serviceuser.controller.api;


import com.dizhongdi.serviceuser.entity.DzdCredit;
import com.dizhongdi.serviceuser.service.DzdCreditService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("cutQuantityById/{memberId}/{num}")
    @ApiOperation(value = "减少下载次数")
    public boolean cutQuantityById(@PathVariable String memberId , @PathVariable Integer num){
        DzdCredit credit = creditService.getById(memberId);
        if (credit.getCredit()>0){
            creditService.updateById(credit.setQuantity(credit.getQuantity() - num));
            return true;
        }
        return false;
    }

    @PutMapping("addQuantityById/{memberId}/{num}")
    @ApiOperation(value = "增加下载次数")
    public boolean addQuantityById(@PathVariable String memberId , @PathVariable Integer num){
        DzdCredit credit = creditService.getById(memberId);
            return creditService.updateById(credit.setQuantity(credit.getQuantity() + num));
    }

}

