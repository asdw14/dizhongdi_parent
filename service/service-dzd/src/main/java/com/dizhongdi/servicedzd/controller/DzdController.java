package com.dizhongdi.servicedzd.controller;

import com.dizhongdi.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:DzdController
 * Package:com.dizhongdi.servicedzd.controller
 * Description:
 *
 * @Date: 2022/7/26 23:51
 * @Author:dizhongdi
 */
@RestController
@RequestMapping("/admin")
public class DzdController {
    @GetMapping("hello")
    public R hello(){
        return R.ok().data("messge","test");
    }
}
