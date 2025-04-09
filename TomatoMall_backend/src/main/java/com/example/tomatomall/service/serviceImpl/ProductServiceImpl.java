package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.ProductRepository;
import com.example.tomatomall.Repository.SpecRepository;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Specification;
import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.service.SpecService;
import com.example.tomatomall.utils.TokenUtil;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.SpecificationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    SpecService specService;

    @Autowired
    SpecRepository specRepository;

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
        // TODO : updateSpecs

        productRepository.save(product);
        return true;
    }

    @Override
    public Boolean deleteProductById(int id) {
        return productRepository.deleteById(id);
    }

    @Override
    public Product addProduct(ProductVO productVO) {
        // 1. 转换 ProductVO → Product（不包含关联）
        Product product = productVO.toPO();

        // 2. 处理 Specifications 关联
        if (productVO.getSpecifications() != null) {
            Set<Specification> specs = productVO.getSpecifications().stream()
                    .map(specVO -> {
                        Specification spec = specVO.toPO(); // 转换 SpecificationVO → Specification
                        spec.setProduct(product); // 手动建立双向关联
                        return spec;
                    })
                    .collect(Collectors.toSet());
            product.setSpecifications(specs); // 设置关联
        }

        // 3. 级联保存（由于配置了 cascade = CascadeType.ALL，会自动保存 specs）
        // 4. 返回带 ID 的 VO
        return productRepository.save(product);
    }
}
