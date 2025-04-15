package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.AccountRepository;
import com.example.tomatomall.Repository.CartItemRepository;
import com.example.tomatomall.Repository.ProductRepository;
import com.example.tomatomall.dto.CartItemDTO;
import com.example.tomatomall.po.Account;
import com.example.tomatomall.po.CartItem;
import com.example.tomatomall.service.CartService;
import com.example.tomatomall.vo.CartItemVO;
import com.example.tomatomall.vo.CartResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final AccountRepository accountRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(AccountRepository accountRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.accountRepository = accountRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CartItemVO addToCart(CartItemDTO cartItemDTO) {
        // 将 CartItemDTO 转换为 CartItem
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(cartItemDTO.getQuantity());
        // 假设有方法获取 Product 和 User 对象
        // cartItem.setProduct(productService.getProductById(cartItemDTO.getProductId()));
        // cartItem.setUser(userService.getUserById(cartItemDTO.getUserId()));

        // 保存逻辑（省略）

        // 转换为 CartItemVO 并返回
        CartItemVO cartItemVO = new CartItemVO();
        cartItemVO.setQuantity(cartItem.getQuantity());
        // 设置其他字段
        return cartItemVO;
    }


    @Override
    public List<CartItemVO> getCartItems() {
        Integer userId = getCurrentUserId();
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        return cartItems.stream()
                .map(CartItem::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteCartItem(Integer cartItemId) {
        try {
            cartItemRepository.deleteById(cartItemId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateCartItemQuantity(Integer cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            return true;
        }
        return false;
    }

    @Override
    public BigDecimal calculateTotalAmount(Integer userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return cartItems.stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<CartItem> getCartItemsByUserId(Integer userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public List<CartItem> getCartItemsByOrderId(Integer orderId) {
        return null;
    }


    @Override
    public CartResponseVO getCartView() {
        Integer userId = getCurrentUserId();
        List<CartItemVO> items = cartItemRepository.findByUserId(userId)
                .stream()
                .map(CartItem::toVO)
                .collect(Collectors.toList());

        BigDecimal totalAmount = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartResponseVO response = new CartResponseVO();
        response.setItems(items);
        response.setTotal(items.size());
        response.setTotalAmount(totalAmount);
        return response;
    }






    private Integer getCurrentUserId() {
        return 1; // 示例实现
    }
}
