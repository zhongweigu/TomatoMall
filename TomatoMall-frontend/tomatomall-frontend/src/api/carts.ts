import {axios} from '../utils/request'
import {CART_MODULE} from './_prefix'

type UpdateCartInfo = {
    cartItemId : string,
    quantity : number,
}

type AddCartInfo = {
    productId : string,
    quantity : number,
}

type CheckoutInfo = {
    cartItemIds : [],
    shipping_address : {
        name : string,
        telephone : string,
        zipcode : string,
        address : string,
    },
    paymentMethods : string
}

// 获取购物车内所有商品
export const getAllCarts = () => {
    return axios.get(`${CART_MODULE}`)
        .then(res => {
            return res
        })
}


export const updateCart = (cart: UpdateCartInfo,token:string) => {
    return axios.patch(`${CART_MODULE}/${cart.cartItemId}`, cart,{headers: {
            "token": `${token}`
        }}).then(res => {
        return res
    })
}

export const deleteCart = (cartItemId: string) => {
    return axios.delete(`${CART_MODULE}/${cartItemId}`).then(res => {
        return res
    })
}

// 添加，注意这里还要输入token，可以直接把sessionStorage.getItem("token")作为参数，不然会被拦截
export const addCart = (cart: AddCartInfo,token:string) => {
    return axios.post(`${CART_MODULE}`, cart,{headers: {
            "token": `${token}`
        }}).then(res => {
        return res
    })
}

export const checkout = (checkoutInfo: CheckoutInfo) => {
    console.log(checkoutInfo)
    return axios.post(`${CART_MODULE}/checkout`, checkoutInfo, {headers: {'Content-Type': 'application/json'}}).then(res => {
        return res
    })
}
export const getCartByCartItemId = (cartItemId: string) => {
    return axios.get(`${CART_MODULE}/${cartItemId}`).then(res => {
        return res
    })
}