package com.dizhongdi.serviceuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dizhongdi.model.AdminGetUserVo;
import com.dizhongdi.servicebase.exceptionhandler.DzdException;
import com.dizhongdi.servicebase.utils.MD5;
import com.dizhongdi.serviceuser.entity.*;
import com.dizhongdi.serviceuser.mapper.UcenterMemberMapper;
import com.dizhongdi.serviceuser.service.DzdCreditService;
import com.dizhongdi.serviceuser.service.DzdDatasizeService;
import com.dizhongdi.serviceuser.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dizhongdi.serviceuser.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-07-08
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    DzdCreditService creditService;

    @Autowired
    DzdDatasizeService datasizeService;

    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //校验参数
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) ) {
            throw new DzdException(20001,"手机号或密码输入有误");
        }

        //获取会员
        UcenterMember member = this.getOne(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if (null == member){
            throw new DzdException(20001,"您还未进行注册");

        }
        //校验密码
        if(!MD5.encrypt(password).equals(member.getPassword())) {
            throw new DzdException(20001,"密码错误");
        }

        //校验是否被禁用
        if(member.getIsDisabled()!=0) {
            throw new DzdException(20001,"该用户已被封禁");
        }

        //使用JWT生成token字符串
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        return token;

    }

    @Override
    public void register(RegisterVo registerVo) {
        String nickname = registerVo.getNickname();
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        //校验参数
        if(StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(nickname) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new DzdException(20001,"请输入全部选项");
        }

        //如果手机号已经注册过
        if (baseMapper.selectCount(new QueryWrapper<UcenterMember>().eq("mobile",mobile))>0){
            throw new DzdException(20001,"您已注册过");
        }

        //校验校验验证码
        //从redis获取发送的验证码
        String mobleCode = (String) redisTemplate.opsForValue().get(mobile);
        if(!code.equals(mobleCode)) {
            System.out.println(mobile);
            System.out.println(mobleCode + ".." + code);
            throw new DzdException(20001,"验证码不一致");
        }

        //添加注册信息到数据库
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(mobile);
        ucenterMember.setPassword(MD5.encrypt(password));
        ucenterMember.setNickname(nickname);
        ucenterMember.setIsDisabled(0);
        ucenterMember.setAvatar("https://thirdwx.qlogo.cn/mmopen/vi_32/Q3auHgzwzM5PhJyn79l9BzIoUoBvF0iaWq5aqGBocR953bib2hiarJu8bTNXE51ibuRVFZagbiczUBtZEl99x5pnChw/132");
        this.save(ucenterMember);

        //添加用户下载次数和积分
        String id = ucenterMember.getId();
        DzdCredit credit = new DzdCredit().setQuantity(3).setId(ucenterMember.getId());
        creditService.save(credit);

        //添加用户空间
        datasizeService.save(new DzdDatasize().setId(id));

    }

//    根据token获取用户信息
    @Override
    public LoginInfo getLoginInfo(String id) {
        UcenterMember ucenterMember = this.getById(id);
        LoginInfo loginInfo = new LoginInfo();
        if (ucenterMember != null) {
            BeanUtils.copyProperties(ucenterMember, loginInfo);

        }
        return loginInfo;
    }

//    @Cacheable(value = "openid", key = "'info'")
    @Override
    public UcenterMember getByOpenid(String openid) {

        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);

        UcenterMember member = baseMapper.selectOne(queryWrapper);
        return member;
    }

    //统计某一天的注册人数
    @Override
    public Integer countRegisterByDay(String day) {
        //根据用户数据添加时间查询记录
        return baseMapper.selectRegisterCount(day);
    }

    //根据id获取用户所有信息
    @Override
    public AdminGetUserVo getUserInfo(String id) {
        //获取用户基本信息
        UcenterMember user = this.getById(id);
        AdminGetUserVo userInfo = new AdminGetUserVo();
        BeanUtils.copyProperties(user,userInfo);
        //获取用户空间
        DzdDatasize datasize = datasizeService.getById(user.getId());
        if(datasize != null){
            userInfo.setDatasize(datasize.getDatasize());
        }

        //获取用户积分
        DzdCredit credit = creditService.getById(user.getId());
        if(credit != null){
            userInfo.setCredit(credit.getCredit()).setQuantity(credit.getQuantity());

        }
        //返回用户全部信息
        return userInfo;
    }
}
