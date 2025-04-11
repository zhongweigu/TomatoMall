
package com.example.tomatomall.controller;

import com.example.tomatomall.vo.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @PostMapping("/image")
    public Response<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Response.buildFailure("文件为空", null);
        }

        String fileName = file.getOriginalFilename();
        String filePath = "path/to/save/images/"; // 请根据实际情况修改路径
        File dest = new File(filePath + fileName);

        try {
            file.transferTo(dest);
            return Response.buildSuccess("文件上传成功: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.buildFailure("文件上传失败", null);
        }
    }
}