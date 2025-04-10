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
        if (productVO.getSpecifications() != null) {
            product.getSpecifications().clear();

            for (SpecificationVO specVO : productVO.getSpecifications()) {
                Specification spec = new Specification();
                spec.setItem(specVO.getItem());
                spec.setValue(specVO.getValue());
                spec.setProduct(product);
                product.getSpecifications().add(spec);
            }
        }

        productRepository.save(product);
        return true;
    }

    @Override
    public Boolean deleteProductById(int id) {
        if (productRepository.findById(id)!=null) { // 检查是否存在
            productRepository.deleteById(id);   // 存在则删除
            return productRepository.findById(id) == null;
        }
        return false; // 不存在返回 false
    }

    @Override
    public ProductVO addProduct(ProductVO productVO) {
        Product product = productVO.toPO();
        if(productVO.getSpecifications()!=null){
            for (SpecificationVO specVO : productVO.getSpecifications()) {
                Specification spec = new Specification();
                spec.setItem(specVO.getItem());
                spec.setValue(specVO.getValue());
                product.addSpecification(spec); // 调用双向绑定方法
            }
        }
        Product newProduct = productRepository.save(product);
        return getProductById(newProduct.getId());
    }
}
