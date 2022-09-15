package com.dizhongdi.serviceoss.client;

import com.dizhongdi.model.AdminGetUserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

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


    //根据用户id获取剩余容量
    @PostMapping("/api/user/datasize/getDatasizeByMemberId/{memberId}")
    Double getDatasizeByMemberId(@PathVariable String memberId);

    //增加用户剩余容量
    @PostMapping("/api/user/datasize/addDatasize/{memberId}/{size}")
    boolean addDatasize(@PathVariable String memberId, @PathVariable Double size);

    //减少用户剩余容量
    @PostMapping("/api/user/datasize/cutDatasize/{memberId}/{size}")
    boolean cutDatasize(@PathVariable String memberId, @PathVariable Double size);

    //根据用户id获取剩余下载次数
    @PostMapping("/api/user/credit/getQuantityById/{memberId}")
    Integer getQuantityById(@PathVariable String memberId);

    //减少1次下载次数
    @PutMapping("/api/user/credit/cutQuantityById/{memberId}/{num}")
    boolean cutQuantityById(@PathVariable String memberId, @PathVariable Integer num);

}
