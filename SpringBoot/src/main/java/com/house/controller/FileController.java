package com.house.controller;

import cn.hutool.core.io.FileUtil;
import com.house.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传 下载
 */

@RestController
@RequestMapping("/files")
public class FileController {

    /**
     * 文件上传
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

        // 生成文件名
        byte[] bytes = file.getBytes();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        
        // 写入文件
        FileUtil.writeBytes(bytes, specificPath + fileName);
        
        // 返回访问URL
        String url = "http://localhost:8080/files/download/" + type + "/" + fileName;
        return Result.success(url);
    }

    /**
     * 文件下载
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
        
        if (!FileUtil.exist(realPath)) {
            throw new RuntimeException("文件不存在");
        }

        byte[] bytes = FileUtil.readBytes(realPath);
        ServletOutputStream os = response.getOutputStream();
        os.write(bytes);
        os.flush();
        os.close();
    }
}
