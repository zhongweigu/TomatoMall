package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.service.UploadService;
import com.example.tomatomall.utils.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    private OssUtil ossUtil;
    @Override
    public String upload(MultipartFile file, String type) {
        try {
            return ossUtil.upload(type + "/", file.getOriginalFilename(), file.getInputStream());
        }catch (Exception e){
            throw TomatoMallException.fileUploadFail();
        }
    }
}
