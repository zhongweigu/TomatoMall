package com.example.tomatomall.Repository;

import com.example.tomatomall.po.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;  // 确保 List 已被正确导入

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // 使用 Optional 返回，以避免返回 null
    Optional<Product> findById(int id);

    // 删除商品的方法，Spring Data JPA 默认方法已经很好
    @Override
    void deleteById(Integer id);

    // 使用 JpaRepository 提供的 findAll 方法
    // 你不需要自己声明 findAll 方法了，直接使用它即可
    List<Product> findAll();
}
