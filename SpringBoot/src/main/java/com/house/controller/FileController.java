package com.house.controller;

import cn.hutool.core.io.FileUtil;
import com.house.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件控制器
 * 
 * 处理文件上传和下载的HTTP请求，支持不同类型文件的存储和访问
 * 提供房源图片等资源的管理功能
 */
@RestController
@RequestMapping("/files")
public class FileController {

    /**
     * 文件上传接口
     * 接收客户端上传的文件，根据类型保存到不同目录，并返回可访问的URL
     * 
     * @param file 上传的文件对象，通过multipart/form-data方式提交
     * @param type 文件类型，如"house"表示房源图片，默认为"default"
     * @return 包含文件访问URL的成功响应
     * @throws IOException 当文件读写发生错误时抛出异常
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file, 
                        @RequestParam(value = "type", defaultValue = "default") String type) throws IOException {
        // 根据类型确定存储路径
        String baseFilePath = System.getProperty("user.dir") + "/files/";
        String specificPath;
        
        switch (type) {
            case "house":
                specificPath = baseFilePath + "house_images/";
                break;
            // 可以添加其他类型的文件夹
            default:
                specificPath = baseFilePath + "default/";
        }

        // 创建目录（如果不存在）
        if (!FileUtil.exist(specificPath)) {
            FileUtil.mkdir(specificPath);
        }

        // 生成文件名，使用时间戳避免重名
        byte[] bytes = file.getBytes();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        
        // 写入文件到服务器文件系统
        FileUtil.writeBytes(bytes, specificPath + fileName);
        
        // 返回可访问的URL路径给前端
        String url = "http://localhost:8080/files/download/" + type + "/" + fileName;
        return Result.success(url);
    }

    /**
     * 文件下载接口
     * 根据文件类型和名称读取服务器上的文件，并通过HTTP响应流返回文件内容
     * 
     * @param type 文件类型，决定从哪个目录查找文件
     * @param fileName 文件名称，包含时间戳前缀
     * @param response HTTP响应对象，用于写入文件内容
     * @throws IOException 当文件读写发生错误时抛出异常
     * @throws RuntimeException 当请求的文件不存在时抛出异常
     */
    @GetMapping("/download/{type}/{fileName}")
    public void download(@PathVariable String type, 
                        @PathVariable String fileName, 
                        HttpServletResponse response) throws IOException {
        // 根据类型确定文件路径
        String baseFilePath = System.getProperty("user.dir") + "/files/";
        String specificPath;
        
        switch (type) {
            case "house":
                specificPath = baseFilePath + "house_images/";
                break;
            default:
                specificPath = baseFilePath + "default/";
        }

        String realPath = specificPath + fileName;
        
        // 检查文件是否存在
        if (!FileUtil.exist(realPath)) {
            throw new RuntimeException("文件不存在");
        }

        // 读取文件内容并写入响应流
        byte[] bytes = FileUtil.readBytes(realPath);
        ServletOutputStream os = response.getOutputStream();
        os.write(bytes);
        os.flush();
        os.close();
    }
}
