package com.imooc.controller;

import com.imooc.config.MinIOConfig;
import com.imooc.pojo.Users;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "FileController 文件上传")
@RestController
@RequestMapping("upload")
public class FileController extends BaseController {

    @Autowired
    private MinIOConfig minIOConfig;

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private RedisOperator redisOperator;
    @PostMapping("/testUpload")
    public IMOOCJSONResult upload(MultipartFile file) throws  Exception{
//        获取文件名字
        String fileName = file.getOriginalFilename();
        MinIOUtils.uploadFile(minIOConfig.getBucketName(),file,fileName, file.getInputStream().toString());

        // http://43.143.191.173:9000/imooc/test.png
        // fileHost对应在yml文件中
        String url=minIOConfig.getFileHost()+"/"+minIOConfig.getBucketName()+"/"+fileName;
        return  IMOOCJSONResult.ok(url);
    }

    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(
            String userId,
            MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url = "";
        // 开始文件上传
        if (file != null) {
            // 获得文件上传的文件名称
            String fileName = file.getOriginalFilename();
            if (StringUtils.isNotBlank(fileName)) {
                // 文件重命名  imooc-face.png -> ["imooc-face", "png"]
                String fileNameArr[] = fileName.split("\\.");

                // 获取文件的后缀名
                String suffix = fileNameArr[fileNameArr.length - 1];
                // 验证图片格式，equalsIgnoreCase表示忽略大小写
                if (!suffix.equalsIgnoreCase("png") && !suffix.equalsIgnoreCase("jpg") && !suffix.equalsIgnoreCase("jpeg")) {
                    return IMOOCJSONResult.errorMsg("图片格式不正确！");
                }
                // 开始格式化代码
                MinIOUtils.uploadFile(minIOConfig.getBucketName(), file, fileName, file.getInputStream().toString());
                // http://43.143.191.173:9000/imooc/test.png
                // fileHost对应在yml文件中
                url = minIOConfig.getFileHost() + "/" + minIOConfig.getBucketName() + "/" + fileName;
                return IMOOCJSONResult.ok(url);
            } else {
                return IMOOCJSONResult.errorMsg("文件不能为空！");
            }
        }
        if (StringUtils.isNotBlank(url)) {
            // 更新用户头像到数据库
            Users userResult = centerUserService.updateUserFace(userId, url);
            UsersVO usersVO = new UsersVO();
            BeanUtils.copyProperties(userResult, usersVO);
            CookieUtils.setCookie(request, response, "user",
                    JsonUtils.objectToJson(usersVO), true);
        } else {
            return IMOOCJSONResult.errorMsg("上传头像失败");
        }
        return IMOOCJSONResult.ok();
    }
}
