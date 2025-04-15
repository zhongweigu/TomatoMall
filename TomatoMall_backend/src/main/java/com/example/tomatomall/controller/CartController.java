package com.example.tomatomall.controller;

import com.example.tomatomall.po.CartItem;
import com.example.tomatomall.service.CartService;
import com.example.tomatomall.vo.CartItemVO;
import com.example.tomatomall.vo.CartResponseVO;
import com.example.tomatomall.vo.Response;

import com.example.tomatomall.dto.CartItemDTO;

import org.springframework.web.bind.annotation.*;



import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @PostMapping
    public Response<CartItemVO> addToCart(@RequestBody CartItemDTO cartItemDTO) {
        CartItemVO vo = cartService.addToCart(cartItemDTO);
        return Response.buildSuccess(vo);
    }

    @GetMapping
    public Response<List<CartItemVO>> getCartItems() {
        List<CartItemVO> cartItems = cartService.getCartItems();
        return Response.buildSuccess(cartItems);
    }

    @GetMapping("/view")
    public Response<CartResponseVO> getCartView() {
        CartResponseVO cartResponseVO = cartService.getCartView();
        if (cartResponseVO != null) {
            return Response.buildSuccess(cartResponseVO);
        } else {
            return Response.buildFailure("购物车为空", null);
        }
    }


    @DeleteMapping("/{cartItemId}")
    public Response<String> deleteCartItem(@PathVariable Integer cartItemId) {
        boolean deleted = cartService.deleteCartItem(cartItemId); // 使用 Integer 类型
        if (deleted) {
            return Response.buildSuccess("删除成功");
        } else {
            return Response.buildFailure("购物车商品不存在", null);
        }
    }

    @PatchMapping("/{cartItemId}")
    public Response<String> updateCartItemQuantity(@PathVariable Integer cartItemId,
                                                   @RequestBody CartItem cartItem) {
        boolean updated = cartService.updateCartItemQuantity(cartItemId, cartItem.getQuantity()); // 使用 Integer 类型
        if (updated) {
            return Response.buildSuccess("修改数量成功");
        } else {
            return Response.buildFailure("购物车商品不存在", null);
        }
    }
}