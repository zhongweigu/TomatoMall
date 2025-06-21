package com.example.tomatomall.controller;

import com.example.tomatomall.Repository.ContentRepository;
import com.example.tomatomall.service.ContentService;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Resource
    ContentService contentService;

    @GetMapping("/{id}")
    public Response<String> getContent(@PathVariable String id){
        return Response.buildSuccess(contentService.getContent(Integer.parseInt(id)));
    }

}
