package com.dizhongdi.serviceoss.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.model.AdminGetUserVo;
import com.dizhongdi.model.ArticleStarLogByUser;
import com.dizhongdi.model.UserSourceDownLog;
import com.dizhongdi.servicebase.exceptionhandler.DzdException;
import com.dizhongdi.serviceoss.client.UserClient;
import com.dizhongdi.serviceoss.entity.DownLog;
import com.dizhongdi.serviceoss.entity.DzdSource;
import com.dizhongdi.serviceoss.entity.vo.DirectoryVo;
import com.dizhongdi.serviceoss.entity.vo.SourceInfoVo;
import com.dizhongdi.serviceoss.entity.vo.SourceQuery;
import com.dizhongdi.serviceoss.entity.vo.UploadInfo;
import com.dizhongdi.serviceoss.mapper.DzdSourceMapper;
import com.dizhongdi.serviceoss.service.DownLogService;
import com.dizhongdi.serviceoss.service.DzdSourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Autowired
    DownLogService downLogService;

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

    //分页获取公开资源
    @Override
    public List<SourceInfoVo> getPublicPageList(Page<DzdSource> sourcePage, SourceQuery sourceQuery, Boolean isAdmin) {
        List<SourceInfoVo> sourcesList = new ArrayList<>();
        QueryWrapper<DzdSource> wrapper = new QueryWrapper<>();
        //用户id
        String userId = sourceQuery.getMemberId();
        //文件大小范围
        BigDecimal minfileSize = sourceQuery.getMinfileSize();
        BigDecimal maxfileSize = sourceQuery.getMaxfileSize();
        String sourceName = sourceQuery.getSourceName();
        Boolean isCharge = sourceQuery.getIsCharge();
        //排序顺序 true为按日期
        boolean sort = sourceQuery.isSort();

        //公开资源
        wrapper.eq("is_public",1);

        //如果是管理员
        if (isAdmin==true){
            //管理员算法：根据最新上传来排序
            wrapper.orderByDesc("gmt_create");
        }   //如果是用户，获取热度最高的
        else {
            //获取未封禁资源
            wrapper.eq("is_ban",0);
        }

        if (sourceQuery!=null){
            //if查询条件里的上传用户id不为空
            if (!StringUtils.isEmpty(userId))
                wrapper.eq("member_id",userId);

            //if查询条件里的文件大小范围不为空
            if (!(StringUtils.isEmpty(minfileSize)&&StringUtils.isEmpty(maxfileSize)))
                wrapper.between("file_size",minfileSize,maxfileSize);

            //if查询条件里的文件名不为空
            if (!StringUtils.isEmpty(sourceName))
                wrapper.like("source_name",sourceName);

            //if查询条件里的是否免费不为空
            if (isCharge!=null) {
                if (isCharge) {
                    wrapper.eq("is_charge", 0);
                }else {
                    wrapper.eq("is_charge", 1);
                }
            }

            //排序
            if(sort){
                wrapper.orderByDesc("gmt_create");
            }else {
                wrapper.orderByDesc("down_count");
            }
        }

        List<DzdSource> records = this.page(sourcePage, wrapper).getRecords();
        records.stream().map(record ->{
            SourceInfoVo source = new SourceInfoVo();
            BeanUtils.copyProperties(record,source);
            //获取用户头像昵称
            AdminGetUserVo user = userClient.getAllInfoId(record.getMemberId());
            source.setAvatar(user.getAvatar()).setNickname(user.getNickname());

            // 如果不是管理员
            // 隐藏下载地址url
            if (!isAdmin)
                source.setSourceOssUrl("");
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
        sourceInfo.setAvatar(user.getAvatar()).setMemberId(user.getId());
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
            String uploadUrl = null;
            //获取上传文件流
            InputStream inputStream = file.getInputStream();
            //获取文件md5值
            String md5 = DigestUtils.md5DigestAsHex(inputStream);

            //如果用户没有设置上传文件名默认以原文件名显示
            if (StringUtils.isEmpty(uploadInfo.getSourceName())){
                uploadInfo.setSourceName(file.getOriginalFilename());
            }

            DzdSource dzdSource = new DzdSource();
            //设置是否公开
            dzdSource.setIsPublic(uploadInfo.getIsPublic());

            BeanUtils.copyProperties(uploadInfo,dzdSource);


            dzdSource.setMd5(md5);
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            dzdSource.setOriginalName(originalFilename);
            //大小 byte
            double fileSize = file.getBytes().length;
            //把byte转为MB单位
            dzdSource.setFileSize((fileSize/1024/1024));


            //判断是否有一样的文件，有直接把路径拿来不上传了
            List<DzdSource> md5SourceList = baseMapper.selectList(new QueryWrapper<DzdSource>().eq("md5", md5));
            if (md5SourceList.size()>0){
                uploadUrl = md5SourceList.get(0).getSourceOssUrl();
            }else {

                //构建日期路径：avatar/2019/02/26/文件名
                String datePath = new DateTime().toString("yyyy/MM/dd");

                //文件路径:用户id/上传日期/用户给的文件名
                String userId = uploadInfo.getMemberId();
                String filepath;
                if (!StringUtils.isEmpty(userId)){
                    filepath = userId +  datePath + "/" + file.getOriginalFilename();
                }else {
                    filepath = datePath + "/" + file.getOriginalFilename();
                }

                //文件上传至阿里云
                ossClient.putObject(bucketName,filepath,file.getInputStream());
                //获取url地址
                uploadUrl = "https://" + bucketName + "." + endpoint + "/" + filepath;
            }

            System.out.println(uploadUrl);
            //添加文件链接
            dzdSource.setSourceOssUrl(uploadUrl);
            //添加父层级id
            if (!StringUtils.isEmpty(uploadInfo.getParentId())){
                dzdSource.setParentId(uploadInfo.getParentId());
            }
            System.out.println(dzdSource);
            return this.save(dzdSource);

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

    //新建文件夹
    @Override
    public boolean newDirectory(DirectoryVo directoryVo) {
        DzdSource dzdSource = new DzdSource();
        Integer count = baseMapper.selectCount(
                new QueryWrapper<DzdSource>().
                        eq("parent_id", directoryVo.getParentId())
                        .eq("source_name", directoryVo.getSourceName())
                        .eq("member_id",directoryVo.getMemberId()));

        //如果该用户该层级已经有了这个目录，直接返回成功
        if (count>0){
            return true;
        }
        dzdSource.setFileSize(0.00);
        BeanUtils.copyProperties(directoryVo,dzdSource);
        return this.save(dzdSource);

    }

    //根据父文件夹id获取个人资源
    @Override
    public List<SourceInfoVo> getMemberSourceByDirectoryId(String id, String memberId, SourceQuery sourceQuery) {
        ArrayList<SourceInfoVo> sourceList = new ArrayList<>();

        QueryWrapper<DzdSource> wrapper;

        //根据文件名查找返回所有对应文件
        //后期改造获取远程调用es查询
        if ((sourceQuery!=null) && !(StringUtils.isEmpty(sourceQuery.getSourceName()))){
            wrapper = new QueryWrapper<DzdSource>().like("source_name", sourceQuery.getSourceName()).eq("member_id", memberId);

        }else {
            wrapper = new QueryWrapper<DzdSource>().eq("parent_id", id).eq("member_id", memberId).eq("is_public",0);

        }

        List<DzdSource> dzdSources = baseMapper.selectList(wrapper);

        dzdSources.stream().forEach( source ->{
            SourceInfoVo sourceInfoVo = new SourceInfoVo();
            BeanUtils.copyProperties(source,sourceInfoVo);
            sourceList.add(sourceInfoVo);
            }
        );


        return sourceList;
    }


    //根据获取的文件夹id获取文件夹父id以做返回
    @Override
    public String getParentDirectoryId(String id) {
        if ("0".equals(id)){
            return id;
        }
        return baseMapper.selectById(id).getParentId();
    }


    //根据id删除文件或文件夹
    @Override
    public boolean deleteByMemberAndSourceId(String memberId, String id) {
        DzdSource source = baseMapper.selectById(id);
        //如果文件不存在直接返回成功
        if (source==null){
            return true;
        }
        //判断该文件请求发起者是否和文件所有人是一位
        if ((StringUtils.isEmpty(source.getMemberId())) && !(source.getMemberId().equals(memberId))){
            throw new DzdException( 30001, "仅能删除自己上传的文件");
        }
        //删除文件记录
        baseMapper.deleteById(id);

        //添加回用户删除的容量
        userClient.addDatasize(memberId,source.getFileSize());

        return true;
    }

    //增加下载次数
    @Override
    public boolean addDownCount(String id, Integer count) {
        DzdSource dzdSource = baseMapper.selectById(id);
        DzdSource downCount = dzdSource.setDownCount(dzdSource.getDownCount() + count);
        return this.updateById(downCount);
    }

    //减少用户下载次数并返回url
    @Override
    public String getSourceUrl(String id, String memberId) {
        //剩余下载次数大于0
        if (userClient.getQuantityById(memberId)>0){
            if (userClient.cutQuantityById(memberId,1)){
                DzdSource dzdSource = baseMapper.selectById(id);
                //增加分享人一次下载次数
                userClient.addQuantityById(dzdSource.getMemberId(),1);
                //添加购买记录
                DownLog downLog = new DownLog();
                downLog.setSourceId(id).setMemberId(memberId);
                downLogService.save(downLog);
                if (dzdSource!=null){
                    return dzdSource.getSourceOssUrl();
                }
            }
        }
        return null;
    }

    //根据用户id查询资源下载记录
    @Override
    public List<UserSourceDownLog> getSourceDownByUserId(String memberId) {
        List<UserSourceDownLog> list = new ArrayList<>();

        //下载记录
        List<DownLog> downLogs = downLogService.getByMemberId(memberId);

        if (downLogs==null){
            return null;
        }

        //添加进帖子标题和描述
        downLogs.stream().map(downLog ->{
            UserSourceDownLog sourceDownLog = new UserSourceDownLog();
            sourceDownLog.setSourceId(downLog.getSourceId());

            //根据资源id查找资源信息
            DzdSource source = this.getById(downLog.getSourceId());
            if (source!=null)
                BeanUtils.copyProperties(source,sourceDownLog);
            else
                sourceDownLog.setSourceName("资源已被删除");
            //下载时间
            sourceDownLog.setGmtModified(downLog.getGmtModified());
            return sourceDownLog;

        }).forEach(list::add);

        return list;
    }


}
