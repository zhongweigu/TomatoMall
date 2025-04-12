package com.example.tomatomall.service;

import com.example.tomatomall.po.Stockpile;
import com.example.tomatomall.vo.StockpileVO;

import java.util.List;

public interface StockpileService {
    List<StockpileVO> getAllStockpiles();
    StockpileVO getStockpileById(String id);
    StockpileVO createStockpile(Stockpile stockpile);
    StockpileVO updateStockpile(String id, Stockpile stockpile);
    Boolean deleteStockpile(String id);
    StockpileVO getStockpileByProductId(String productId);
    boolean updateStockpileByProductId(String productId, Integer amount, Integer frozen);
}