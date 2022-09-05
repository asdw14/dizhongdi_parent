package com.dizhongdi.servicedzd.client;

import com.dizhongdi.model.AdminGetUserVo;
import com.dizhongdi.model.UcenterMember;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName:UserClientImpl
 * Package:com.dizhongdi.servicedzd.client
 * Description:
 *
 * @Date: 2022/8/19 17:00
 * @Author:dizhongdi
 */
@Component
public class UserClientImpl implements UserClient {
    @Override
    public AdminGetUserVo getAllInfoId(String id) {
        return null;
    }

    @Override
    public List<UcenterMember> getAllMember() {
        return null;
    }
}
