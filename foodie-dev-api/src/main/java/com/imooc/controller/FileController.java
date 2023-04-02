package com.imooc.controller;

import com.imooc.config.MinIOConfig;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MinIOUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "FileController 文件上传测试的接口")
@RestController
@RequestMapping("upload")
public class FileController {

    @Autowired
    private MinIOConfig minIOConfig;

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
}
