package com.dizhongdi.serviceuser.client;

import com.dizhongdi.model.UserSourceDownLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * ClassName:SourceClient
 * Package:com.dizhongdi.serviceuser.client
 * Description:
 *
 * @Date: 2022/10/18 17:41
 * @Author:dizhongdi
 */
@Component
@FeignClient(value = "service-oss" ,fallback = SourceClientImpl.class)
public interface SourceClient {
    //下载记录
    @GetMapping("/api/oss/source/getSourceDownByUserId/{memberId}")
    public List<UserSourceDownLog> getSourceDownByUserId(@PathVariable String memberId);
}
