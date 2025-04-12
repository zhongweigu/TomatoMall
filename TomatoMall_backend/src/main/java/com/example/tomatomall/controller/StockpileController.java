package com.example.tomatomall.controller;

import com.example.tomatomall.po.Stockpile;
import com.example.tomatomall.service.StockpileService;
import com.example.tomatomall.vo.Response;
import com.example.tomatomall.vo.StockpileVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/products/stockpile")
public class StockpileController {

    @Resource
    private StockpileService stockpileService;

    /**
     * 获取所有库存
     */
    @GetMapping
    public Response<List<StockpileVO>> getAllStockpiles() {
        return Response.buildSuccess(stockpileService.getAllStockpiles());
    }

    /**
     * 根据ID获取库存
     */
    @GetMapping("/stock/{id}")
    public Response<StockpileVO> getStockpileById(@PathVariable String id) {
        return Response.buildSuccess(stockpileService.getStockpileById(id));
    }

    /**
     * 创建新的库存
     */
    @PostMapping
    public Response<StockpileVO> createStockpile(@RequestBody Stockpile stockpile) {
        return Response.buildSuccess(stockpileService.createStockpile(stockpile));
    }

    /**
     * 更新库存
     */
    @PutMapping("/stock/{id}")
    public Response<StockpileVO> updateStockpile(@PathVariable String id, @RequestBody Stockpile stockpile) {
        return Response.buildSuccess(stockpileService.updateStockpile(id, stockpile));
    }

    /**
     * 删除库存
     */
    @DeleteMapping("/stock/{id}")
    public Response<Boolean> deleteStockpile(@PathVariable String id) {
        return Response.buildSuccess(stockpileService.deleteStockpile(id));
    }

    /**
     * 根据商品ID获取库存
     */
    @GetMapping("/{productId}")
    public Response<StockpileVO> getStockpileByProductId(@PathVariable String productId) {
        return Response.buildSuccess(stockpileService.getStockpileByProductId(productId));
    }



    @PatchMapping("/{productId}")
    public Response<String> updateStockpileByProductId(@PathVariable String productId, @RequestBody StockpileVO stockpileVO) {
        boolean success = stockpileService.updateStockpileByProductId(productId, stockpileVO.getAmount(), stockpileVO.getFrozen());
        if (success) {
            return Response.buildSuccess("调整库存成功");
        } else {
            return Response.buildFailure("商品不存在", null);
        }
    }
}