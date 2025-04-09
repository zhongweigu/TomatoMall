package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.ProductRepository;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.utils.SecurityUtil;
import com.example.tomatomall.utils.TokenUtil;
import com.example.tomatomall.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Override
    public List<ProductVO> getProductList() {
        List<ProductVO> productList = new ArrayList<ProductVO>();
        for (Product product : productRepository.findAll()) {
            productList.add(product.toVO());
        }
        return productList;
    }

    @Override
    public ProductVO getProductById(int id) {
        Product product = productRepository.findById(id);
        if(product == null){
            throw TomatoMallException.productDoNotExist();
        }
        return product.toVO();
    }

    @Override
    public Boolean updateProduct(ProductVO productVO) {
        Product product = productRepository.findById(productVO.getId())
                .orElseThrow(TomatoMallException::productDoNotExist);
        if(productVO.getDescription() != null){
            product.setDescription(productVO.getDescription());
        }
        if(productVO.getPrice() != null){
            product.setPrice(productVO.getPrice());
        }
        if (productVO.getRate() != null){
            product.setRate(productVO.getRate());
        }
        if (productVO.getCover() != null){
            product.setCover(productVO.getCover());
        }
        if(productVO.getDetail() != null){
            product.setDetail(productVO.getDetail());
        }


        return null;
    }

    @Override
    public Boolean deleteProductById(int id) {
        return null;
    }

    @Override
    public ProductVO addProduct(ProductVO productVO) {
        return null;
    }
}
