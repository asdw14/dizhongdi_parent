package com.dizhongdi.servicedzd.service;

import com.dizhongdi.servicedzd.entity.DzdSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dizhongdi.servicedzd.entity.vo.SubjectOneVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 帖子分类 服务类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-16
 */
public interface DzdSubjectService extends IService<DzdSubject> {

    //导入课程数据列表
    void importSubjectData(MultipartFile file, DzdSubjectService subjectService);
    //    嵌套课程数据列表
    List<SubjectOneVo> nestedList();
}
