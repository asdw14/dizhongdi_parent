package com.dizhongdi.serviceuser.controller.api;


import com.dizhongdi.result.R;
import com.dizhongdi.serviceuser.entity.DzdDatasize;
import com.dizhongdi.serviceuser.entity.LoginVo;
import com.dizhongdi.serviceuser.service.DzdDatasizeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-18
 */
@RestController
@RequestMapping("/api/user/datasize")
public class ApiDatasizeController {

    @Autowired
    DzdDatasizeService datasizeService;

    @ApiOperation(value = "根据用户id获取剩余容量")
    @PostMapping("getDatasizeByMemberId/{memberId}")
    public Double getDatasizeByMemberId(@PathVariable String memberId) {
        DzdDatasize datasize = datasizeService.getById(memberId);
        if (datasize.getStatus()==0){
            //返回剩余容量
            return datasize.getSurplus();
        }
        return 0.0;
    }

    @ApiOperation(value = "增加用户剩余容量")
    @PostMapping("addDatasize/{memberId}/{size}")
    public boolean addDatasize(@PathVariable String memberId, @PathVariable Double size ){

        DzdDatasize dzdDatasize = datasizeService.getById(memberId);
        dzdDatasize.setSurplus( dzdDatasize.getSurplus() + size);
        return datasizeService.updateById(dzdDatasize);
    }


    @ApiOperation(value = "减少用户剩余容量")
    @PostMapping("cutDatasize/{memberId}/{size}")
    public boolean cutDatasize(@PathVariable String memberId, @PathVariable Double size ){

        DzdDatasize dzdDatasize = datasizeService.getById(memberId);
        if (dzdDatasize.getStatus() == 1 ){
            return false;
        }

        dzdDatasize.setSurplus( dzdDatasize.getSurplus() - size);
        return datasizeService.updateById(dzdDatasize);

    }

}

