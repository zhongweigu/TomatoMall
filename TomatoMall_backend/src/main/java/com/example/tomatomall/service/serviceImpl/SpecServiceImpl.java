package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.ProductRepository;
import com.example.tomatomall.Repository.SpecRepository;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Specification;
import com.example.tomatomall.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;
@Service
public class SpecServiceImpl implements SpecService {
    @Autowired
    SpecRepository specRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public Boolean updateSpec(Set<Specification> specifications, int product_id) {
        Set<Specification> oldSpecs = specRepository.findAllByProduct_Id(product_id);
        Set<Specification> newSpecs = specifications;

        // 处理删除的规格（存在于旧数据但不在新数据中）
        Set<Specification> specsToRemove = oldSpecs.stream()
                .filter(oldSpec -> newSpecs.stream()
                        .noneMatch(newSpec -> Objects.equals(newSpec.getId(), oldSpec.getId())))
                .collect(Collectors.toSet());
        specRepository.deleteAll(specsToRemove);

        newSpecs.forEach(newSpec -> {
            // 设置关联商品
            newSpec.setProduct(productRepository.findById(product_id));

            // 根据ID判断是新增还是更新
            if (newSpec.getId() != null) {
                specRepository.findById(newSpec.getId()).ifPresent(existingSpec -> {
                    // 更新字段（根据业务需求调整）
                    existingSpec.setItem(newSpec.getItem());
                    existingSpec.setValue(newSpec.getValue());
                    specRepository.save(existingSpec);
                });
            } else {
                specRepository.save(newSpec); // 新增
            }
        });

        return true;
    }

    @Override
    public Boolean addSpecs(Set<Specification> specifications, Product product){
        specifications.forEach(spec -> {
            spec.setProduct(product); // 建立双向关联
            specRepository.save(spec);
        });
        return true;
    }
}
