package com.dizhongdi.serviceoss.client;

import com.dizhongdi.model.AdminGetUserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * ClassName:UserClient
 * Package:com.dizhongdi.serviceuser
 * Description:
 *      远程调用用户服务
 * @Date: 2022/8/19 16:10
 * @Author:dizhongdi
 */
@Component
@FeignClient(name="service-user",fallback = UserClientImpl.class)
public interface UserClient {
    @GetMapping("/admin/user/userAllInfo/{id}")
    public AdminGetUserVo getAllInfoId(@PathVariable String id);
}
