package com.example.tomatomall.controller;

import com.example.tomatomall.service.AdvertisementService;
import com.example.tomatomall.vo.AdvertisementVO;
import com.example.tomatomall.vo.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    @Resource
    private AdvertisementService advertisementService;

    @GetMapping
    public Response<List<AdvertisementVO>> getAllAdvertisements() {
        return Response.buildSuccess(advertisementService.getAllAdvertisements());
    }

    @PostMapping
    public Response<AdvertisementVO> createAdvertisement(@RequestParam("title") String title,
                                                         @RequestParam("content") String content,
                                                         @RequestParam("imageUrl") String imageUrl,
                                                         @RequestParam("productId") Integer productId) {
        try {
            AdvertisementVO advertisementVO = advertisementService.createAdvertisement(title, content, imageUrl, productId);
            return Response.buildSuccess(advertisementVO);
        } catch (IllegalArgumentException e) {
            return Response.buildFailure("400", e.getMessage());
        }
    }

    @PutMapping
    public Response<String> updateAdvertisement(@RequestParam("id") Integer id,
                                                @RequestParam("title") String title,
                                                @RequestParam("content") String content,
                                                @RequestParam("imageUrl") String imageUrl,
                                                @RequestParam("productId") Integer productId) {
        try {
            advertisementService.updateAdvertisement(id, title, content, imageUrl, productId);
            return Response.buildSuccess("更新成功");
        } catch (IllegalArgumentException e) {
            return Response.buildFailure("400", e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Response<String> deleteAdvertisement(@PathVariable Integer id) {
        boolean success = advertisementService.deleteAdvertisement(id);
        if (success) {
            return Response.buildSuccess("删除成功");
        } else {
            return Response.buildFailure("400", "广告不存在");
        }
    }
}