package com.example.tomatomall.controller;

import com.example.tomatomall.po.Product;
import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Resource
    ProductService productService;

    @GetMapping()
    public Response<List<ProductVO>> getProducts() {
        return Response.buildSuccess(productService.getProductList());
    }

    @GetMapping("/{id}")
    public Response<ProductVO> getProductById(@PathVariable String id){
        return Response.buildSuccess(productService.getProductById(Integer.parseInt(id)));
    }

    @PutMapping()
    public Response<String> updateProduct(@RequestBody ProductVO productVO){
        return Response.buildSuccess(productService.updateProduct(productVO) ? "更新成功":null);
    }

    @PostMapping()
    public Response<ProductVO> addProduct(@RequestBody ProductVO productVO){
        return Response.buildSuccess(productService.addProduct(productVO));
    }

    @DeleteMapping("/{id}")
    public Response<String> deleteProductById(@PathVariable String id){
        return Response.buildSuccess(productService.deleteProductById(Integer.parseInt(id)) ? "删除成功":null);
    }
}
