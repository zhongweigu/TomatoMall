package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.ContentRepository;
import com.example.tomatomall.exception.TomatoMallException;
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
            throw TomatoMallException.contentAlreadyExists();
        }

    }

    @Override
    public String getContent(int productId) {
        return contentRepository.findByProductId(productId).getContentUrl();
    }

    @Override
    public boolean deleteContent(int productId){
        Content content = contentRepository.findByProductId(productId);
        if(content==null){
            throw TomatoMallException.contentDoNotExists();
        }
        contentRepository.delete(content);
        return true;
    }
}
