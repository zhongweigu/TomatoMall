package com.example.tomatomall.service;

import com.example.tomatomall.vo.ProductVO;

import java.util.List;

public interface ProductService {
    List<ProductVO> getProductList();

    ProductVO getProductById(int id);

    Boolean updateProduct(ProductVO productVO);

    Boolean deleteProductById(int id);

    ProductVO addProduct(ProductVO productVO);
}
