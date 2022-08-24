package com.dizhongdi.servicedzd.controller.api;


import com.dizhongdi.result.R;
import com.dizhongdi.servicedzd.service.DzdSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-16
 */
@RestController
@RequestMapping("/api/dzd/subject")
@Api(description="帖子分类")
public class DzdSubjectController {

    @Autowired
    private DzdSubjectService subjectService;

    //    嵌套课程数据列表
    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("")
    public R nestedList(){
        return R.ok().data("items",subjectService.nestedList());
    }


}

