package com.dizhongdi.serviceuser.client;

import com.dizhongdi.model.UserSourceDownLog;
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
public class SourceClientImpl implements SourceClient {

    @Override
    public List<UserSourceDownLog> getSourceDownByUserId(String memberId) {
        return null;
    }
}
