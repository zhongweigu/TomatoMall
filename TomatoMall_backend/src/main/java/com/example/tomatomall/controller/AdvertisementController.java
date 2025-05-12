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
    public Response<AdvertisementVO> createAdvertisement(@RequestBody AdvertisementVO advertisementVO) {
        try {
            AdvertisementVO res = advertisementService.createAdvertisement(advertisementVO.getTitle(), advertisementVO.getContent(), advertisementVO.getImgUrl(), advertisementVO.getProductId());
            return Response.buildSuccess(res);
        } catch (IllegalArgumentException e) {
            return Response.buildFailure("400", e.getMessage());
        }
    }

    @PutMapping
    public Response<String> updateAdvertisement(@RequestBody AdvertisementVO advertisementVO) {
        try {
            advertisementService.updateAdvertisement(advertisementVO.getId(), advertisementVO.getTitle(), advertisementVO.getContent(), advertisementVO.getImgUrl(), advertisementVO.getProductId());
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