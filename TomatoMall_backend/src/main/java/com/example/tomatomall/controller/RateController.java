package com.example.tomatomall.controller;

import com.example.tomatomall.service.RateService;
import com.example.tomatomall.vo.RateCreateVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rates")
public class RateController {

    @Autowired
    private RateService rateService;

    @PostMapping
    public Response<String> addRate(@RequestBody RateCreateVO rateCreateVO) {
        try {
            return Response.buildSuccess(rateService.addRate(rateCreateVO));
        } catch (RuntimeException e) {
            return Response.buildFailure("400", e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Response<String> deleteRate(@PathVariable("id") Integer id) {
        try {
            rateService.deleteRate(id);
            return Response.buildSuccess("删除评价成功！");
        } catch (RuntimeException e) {
            return Response.buildFailure("400", e.getMessage());
        }
    }

    @DeleteMapping("/post/{postId}")
    public Response<String> deleteRatesByPostId(@PathVariable Integer postId) {
        try {
            rateService.deleteRatesByPostId(postId);
            return Response.buildSuccess("删除该贴的评分成功！");
        } catch (RuntimeException e) {
            return Response.buildFailure("400", e.getMessage());
        }
    }
}