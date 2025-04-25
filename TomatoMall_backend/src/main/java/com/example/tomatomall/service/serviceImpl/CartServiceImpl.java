package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.AccountRepository;
import com.example.tomatomall.Repository.CartItemRepository;
import com.example.tomatomall.Repository.ProductRepository;
import com.example.tomatomall.dto.CartItemDTO;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Account;
import com.example.tomatomall.po.CartItem;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.service.CartService;
import com.example.tomatomall.vo.CartItemVO;
import com.example.tomatomall.vo.CartResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private HttpServletRequest request;

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
        // 添加日志来确认传入的 productId
        System.out.println("Product ID: " + cartItemDTO.getProductId());

        // 检查商品 ID 是否为空
        if (cartItemDTO.getProductId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        // 获取商品信息
        Optional<Product> optionalProduct = productRepository.findById(cartItemDTO.getProductId());
        if (!optionalProduct.isPresent()) {
            throw new IllegalArgumentException("Product does not exist");
        }
        Product product = optionalProduct.get();

        // 获取当前用户信息
        Integer userId = getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("User is not logged in");
        }

        Optional<Account> optionalAccount = accountRepository.findById(userId);
        if (!optionalAccount.isPresent()) {
            throw new IllegalStateException("User does not exist");
        }
        Account account = optionalAccount.get();

        // 创建 CartItem 实例
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setProduct(product);
        cartItem.setUser(account);

        // 保存到数据库
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        // 打印保存的 CartItem ID
        System.out.println("Saved CartItem ID: " + savedCartItem.getCartItemId());

        // 转换为 CartItemVO 并返回
        CartItemVO cartItemVO = new CartItemVO();
        cartItemVO.setCartItemId(savedCartItem.getCartItemId());
        cartItemVO.setProductId(savedCartItem.getProduct().getId());
        cartItemVO.setQuantity(savedCartItem.getQuantity());
        cartItemVO.setPrice(savedCartItem.getProduct().getPrice());
        cartItemVO.setTitle(savedCartItem.getProduct().getTitle());
        cartItemVO.setDescription(savedCartItem.getProduct().getDescription());
        cartItemVO.setCover(savedCartItem.getProduct().getCover());

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
        Account account = (Account) request.getSession().getAttribute("currentUser");
        if (account == null) {
            throw TomatoMallException.notLogin();
        }
        return account.getId();
    }
}
