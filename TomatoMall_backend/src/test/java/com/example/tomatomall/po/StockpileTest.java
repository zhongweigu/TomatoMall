package com.example.tomatomall.po;

import com.example.tomatomall.vo.StockpileVO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StockpileTest {

    @Test
    void testToVO() {
        // 创建 Stockpile 实例
        Stockpile stockpile = new Stockpile();
        stockpile.setId(1);
        stockpile.setProductId(1001);
        stockpile.setAmount(50);
        stockpile.setFrozen(5);

        // 调用 toVO 方法
        StockpileVO vo = stockpile.toVO();

        // 断言检查
        assertNotNull(vo);
        assertEquals(1, vo.getId());
        assertEquals(1001, vo.getProductId());
        assertEquals(50, vo.getAmount());
        assertEquals(5, vo.getFrozen());
    }
}
