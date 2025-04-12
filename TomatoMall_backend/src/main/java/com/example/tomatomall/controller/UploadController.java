
package com.example.tomatomall.controller;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.service.UploadService;
import com.example.tomatomall.utils.OssUtil;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
public class UploadController {
    @Autowired
    UploadService uploadService;

    @PostMapping("/image")
    public Response<String> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) {
        if (file.isEmpty()) {
            throw TomatoMallException.NoFile();
        }

        return Response.buildSuccess(uploadService.upload(file, type));


    }
}