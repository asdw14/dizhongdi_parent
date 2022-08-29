package com.dizhongdi.serviceoss.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.model.AdminGetUserVo;
import com.dizhongdi.serviceoss.client.UserClient;
import com.dizhongdi.serviceoss.entity.DzdSource;
import com.dizhongdi.serviceoss.entity.vo.SourceInfoVo;
import com.dizhongdi.serviceoss.entity.vo.SourceQuery;
import com.dizhongdi.serviceoss.entity.vo.UploadInfo;
import com.dizhongdi.serviceoss.mapper.DzdSourceMapper;
import com.dizhongdi.serviceoss.service.DzdSourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dizhongdi.serviceoss.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-21
 */
@Service
public class DzdSourceServiceImpl extends ServiceImpl<DzdSourceMapper, DzdSource> implements DzdSourceService {

    @Autowired
    UserClient userClient;

    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    @Value("${aliyun.oss.file.endpoint}")
    String endpoint ;
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    @Value("${aliyun.oss.file.keyid}")
    private String accessKeyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String accessKeySecret;
    // 填写Bucket名称，例如examplebucket。
    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    @Override
    public List<SourceInfoVo> getPublicPageList(Page<DzdSource> sourcePage, SourceQuery sourceQuery, Boolean isAdmin) {
        List<SourceInfoVo> sourcesList = new ArrayList<>();
        QueryWrapper<DzdSource> wrapper = new QueryWrapper<>();
        //用户id
        String userId = sourceQuery.getUserId();
        //文件大小范围
        BigDecimal minfileSize = sourceQuery.getMinfileSize();
        BigDecimal maxfileSize = sourceQuery.getMaxfileSize();
        String sourceName = sourceQuery.getSourceName();
        Boolean isCharge = sourceQuery.getIsCharge();

        //公开资源
        wrapper.eq("is_public",1);

        //如果是管理员
        if (isAdmin==true){
            //管理员算法：根据最新上传来排序
            wrapper.orderByDesc("gmt_create");
        }   //如果是用户，获取热度最高的
        else {
            wrapper.orderByDesc("down_count");
            //获取未封禁资源
            wrapper.eq("is_ban",0);
        }

        if (sourceQuery!=null){
            //if查询条件里的上传用户id不为空
            if (!StringUtils.isEmpty(userId))
                wrapper.eq("user_id",userId);

            //if查询条件里的文件大小范围不为空
            if (!(StringUtils.isEmpty(minfileSize)&&StringUtils.isEmpty(maxfileSize)))
                wrapper.between("file_size",minfileSize,maxfileSize);

            //if查询条件里的文件名不为空
            if (!StringUtils.isEmpty(userId))
                wrapper.eq("source_name",sourceName);

            //if查询条件里的是否免费不为空
            if (isCharge!=null) {
                if (isCharge) {
                    wrapper.eq("is_charge", 0);
                }else {
                    wrapper.eq("is_charge", 1);
                }
            }
        }

        List<DzdSource> records = this.page(sourcePage, wrapper).getRecords();
        records.stream().map(record ->{
            SourceInfoVo source = new SourceInfoVo();
            BeanUtils.copyProperties(record,source);
            //获取用户头像昵称
            AdminGetUserVo user = userClient.getAllInfoId(record.getUserId());
            source.setAvatar(user.getAvatar()).setNickname(user.getNickname());
            return source;
        }).forEach(source -> sourcesList.add(source));

        return sourcesList;
    }

    //更新资源信息，如资源名称之类
    @Override
    public boolean updateInfo(String id, DzdSource source) {
        return this.updateById(source.setId(id));
    }

    //根据id修改封禁状态，封禁改为未封禁，未封禁改为封禁
    @Override
    public boolean updateBan(String id) {
        DzdSource source = baseMapper.selectById(id);
        if (source==null)
            return false;
        return
                this.updateById(
                        (source.getIsBan() == 0 ?
                                source.setIsBan(1) : source.setIsBan(0)));
    }

    //根据id获取资源
    @Override
    public SourceInfoVo getInfoById(String id) {
        //获取资源信息
        DzdSource source = this.getById(id);
        SourceInfoVo sourceInfo = new SourceInfoVo();
        BeanUtils.copyProperties(source,sourceInfo);
        //远程调用根据用户id获取用户信息
        AdminGetUserVo user = userClient.getAllInfoId(id);
        sourceInfo.setAvatar(user.getAvatar()).setUserId(user.getId());
        return sourceInfo;
    }


    //根据id删除资源,包括oss上保存的文件
    @Override
    public boolean deleteByid(String id) {
        boolean flag = this.removeById(id);

        //待完善删除云端资源


        if (flag)
            return true;
        return false;
    }

    @Override
    public boolean uploadSource(MultipartFile file, UploadInfo uploadInfo) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build("https://"+endpoint, accessKeyId, accessKeySecret);
        try {

            InputStream inputStream = file.getInputStream();
            //获取文件md5值
            String md5 = DigestUtils.md5DigestAsHex(inputStream);
            uploadInfo.setMd5(md5);
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            uploadInfo.setOriginalName(originalFilename);
            //大小 byte
            double fileSize = file.getBytes().length;
            //把byte转为MB单位
            uploadInfo.setFileSize(new BigDecimal((fileSize/1024/1024)));

            //构建日期路径：avatar/2019/02/26/文件名
            String datePath = new DateTime().toString("yyyy/MM/dd");

            //文件路径:用户id/上传日期/用户给的文件名
            String userId = uploadInfo.getUserId();
            String uploadUrl = null;
            String filepath;
            if (!StringUtils.isEmpty(userId))
                filepath = userId +  datePath + "/" + uploadInfo.getSourceName();

            filepath = datePath + "/" + uploadInfo.getSourceName();
            //文件上传至阿里云
            ossClient.putObject(bucketName,filepath,inputStream);
            //获取url地址
            uploadUrl = "https://" + bucketName + "." + endpoint + "/" + filepath;
            System.out.println(uploadUrl);


        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }


        return true;
    }
}
