package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.po.Stockpile;
import com.example.tomatomall.Repository.StockpileRepository;
import com.example.tomatomall.service.StockpileService;
import com.example.tomatomall.vo.StockpileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockpileServiceImpl implements StockpileService {

    // 初始化日志记录器
    private static final Logger logger = LoggerFactory.getLogger(StockpileServiceImpl.class);

    @Autowired
    private StockpileRepository stockpileRepository;

    @Override
    public List<StockpileVO> getAllStockpiles() {
        logger.debug("Fetching all stockpiles");
        return stockpileRepository.findAll().stream()
                .map(Stockpile::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public StockpileVO getStockpileById(String id) {
        logger.debug("Fetching stockpile with id: {}", id);
        return stockpileRepository.findById(id)
                .map(Stockpile::toVO)
                .orElse(null);
    }

    @Override
    public StockpileVO createStockpile(Stockpile stockpile) {
        logger.info("Creating stockpile: {}", stockpile);
        Stockpile savedStockpile = stockpileRepository.save(stockpile);
        return savedStockpile.toVO();
    }

    @Override
    public StockpileVO updateStockpile(String id, Stockpile stockpile) {
        logger.debug("Updating stockpile with id: {}", id);
        if (stockpileRepository.existsById(id)) {
            stockpile.setId(Integer.parseInt(id));
            Stockpile updatedStockpile = stockpileRepository.save(stockpile);
            return updatedStockpile.toVO();
        }
        logger.warn("Stockpile with id: {} not found for update", id);
        return null;
    }

    @Override
    public Boolean deleteStockpile(String id) {
        logger.debug("Deleting stockpile with id: {}", id);
        if (stockpileRepository.existsById(id)) {
            stockpileRepository.deleteById(id);
            return true;
        }
        logger.warn("Stockpile with id: {} not found for deletion", id);
        return false;
    }

    @Override
    public StockpileVO getStockpileByProductId(String productId) {
        Integer pid = Integer.parseInt(productId);
        Stockpile stockpile = stockpileRepository.findByProductId(pid);
        return stockpile != null ? stockpile.toVO() : null;
    }


    @Override
    public boolean updateStockpileByProductId(String productId, Integer amount, Integer frozen) {
        logger.debug("Attempting to update stockpile for productId: {}", productId);

        // 将 productId 从 String 转换为 Integer
        Integer pid = Integer.parseInt(productId);

        Stockpile stockpile = stockpileRepository.findByProductId(pid); // 这里传递的是 Integer
        if (stockpile != null) {
            logger.debug("Found stockpile: {}", stockpile);
            if(amount!=null ) {
                stockpile.setAmount(amount);
            }
            if(frozen!=null ) {
                stockpile.setFrozen(frozen);
            }
            stockpileRepository.save(stockpile);
            return true;
        } else {
            logger.warn("No stockpile found for productId: {}", productId);
            return false;
        }
    }


}

