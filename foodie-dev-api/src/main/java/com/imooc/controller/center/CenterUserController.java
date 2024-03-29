package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.resource.FileUpload;
import com.imooc.service.UserService;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//第二，关于用户信息的修改
@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private RedisOperator redisOperator;

    // 第二个需要做的
    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
            MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {

        // .sh .php
        // 定义头像保存的地址
        // 最终： face-userId.png
//        String fileSpace = IMAGE_USER_FACE_LOCATION;
        // 定义在资源文件： foodie-dev-api\src\main\resources\file-upload-dev.properties
         String fileSpace = fileUpload.getImageUserFaceLocation();
        // 在路径上为每一个用户增加一个userid，用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;

        // 开始文件上传
        if (file != null) {
            FileOutputStream fileOutputStream = null;
            try {
                // 获得文件上传的文件名称
                String fileName = file.getOriginalFilename();

                if (StringUtils.isNotBlank(fileName)) {

                    // 文件重命名  imooc-face.png -> ["imooc-face", "png"]
                    String fileNameArr[] = fileName.split("\\.");

                    // 获取文件的后缀名
                    String suffix = fileNameArr[fileNameArr.length - 1];

                    // 验证图片格式，equalsIgnoreCase表示忽略大小写
                    if (!suffix.equalsIgnoreCase("png") &&
                            !suffix.equalsIgnoreCase("jpg") &&
                            !suffix.equalsIgnoreCase("jpeg") ) {
                        return IMOOCJSONResult.errorMsg("图片格式不正确！");
                    }

                    // face-{userid}.png
                    // 文件名称重组 覆盖式上传，增量式：额外拼接当前时间
                    String newFileName = "face-" + userId + "." + suffix;

                    // 上传的头像最终保存的位置
                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
                    // 用于提供给web服务访问的地址
                    uploadPathPrefix += ("/" + newFileName);

                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null) {
                        // 创建父级文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    // 文件输出保存到目录
                    fileOutputStream = new FileOutputStream(outFile);
                    // 文件上传时获取到输入流
                    InputStream inputStream = file.getInputStream();
                    // 上传文件
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // 输出流不为空则关闭输出流
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            return IMOOCJSONResult.errorMsg("文件不能为空！");
        }

        // 获取图片服务地址
        String imageServerUrl = fileUpload.getImageServerUrl();

        // 由于浏览器可能存在缓存的情况，所以在这里，我们需要加上时间戳来保证更新后的图片可以及时刷新
        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix
                + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        // 更新用户头像到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        // 实现用户redis会话,将注册到底cookie信息保存到redis中
        String  uniqueToken= UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN+":"+userResult.getId(),uniqueToken);
        UsersVO usersVO=new UsersVO();
        BeanUtils.copyProperties(userResult,usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(usersVO), true);

        return IMOOCJSONResult.ok();
    }


//    第一个需要做的
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public IMOOCJSONResult update(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam  String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        // @Valid: 这个注解代开启CenterUserBO中的错误验证
        // BindingResult result : 代表所有的CenterUserB的message都在result里面
        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            // 发生验证错误所对应的某一个属性和验证消息
            Map<String, String> errorMap = getErrors(result);
            return IMOOCJSONResult.errorMap(errorMap);
        }

        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);

        // 实现用户redis会话,将注册到底cookie信息保存到redis中
        String  uniqueToken= UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN+":"+userResult.getId(),uniqueToken);
        UsersVO usersVO=new UsersVO();
        BeanUtils.copyProperties(userResult,usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(usersVO), true);

        return IMOOCJSONResult.ok();
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            // 发生验证错误所对应的某一个属性(比如为nickname)
            String errorField = error.getField();
            // 验证错误的信息（比如为‘用户昵称不能为空’）
            String errorMsg = error.getDefaultMessage();

            map.put(errorField, errorMsg);
        }
        return map;
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

}
