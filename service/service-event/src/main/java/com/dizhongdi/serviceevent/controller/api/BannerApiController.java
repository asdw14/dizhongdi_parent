package com.dizhongdi.serviceevent.controller.api;

import com.dizhongdi.result.R;
import com.dizhongdi.serviceevent.entity.CrmBanner;
import com.dizhongdi.serviceevent.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName:BannerApiController
 * Package:com.dizhongdi.servicecms.controller
 * Description:
 *
 * @Date: 2022/7/6 22:17
 * @Author:dizhongdi
 */
@RestController
@RequestMapping("/api/event/banner")
@Api(description = "网站首页Banner列表api")
public class BannerApiController {

    @Autowired
    private CrmBannerService bannerService;

    @ApiOperation(value = "获取首页banner")
    @GetMapping("getAllBanner")
    public R index() {
        List<CrmBanner> list = bannerService.selectIndexList();
        return R.ok().data("bannerList", list);
    }

}
