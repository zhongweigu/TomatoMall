package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.ContentRepository;
import com.example.tomatomall.po.Content;
import com.example.tomatomall.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    ContentRepository contentRepository;

    @Override
    public void saveContent(int productId) {
        Content content = contentRepository.findByProductId(productId);
        if(content!=null){
            throw new RuntimeException("该内容已经存在");
        }

    }

    @Override
    public String getContent(int productId) {
        return contentRepository.findByProductId(productId).getContentUrl();
    }
}
