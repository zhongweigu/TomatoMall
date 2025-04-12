import { axios } from '../utils/request'
import {API_MODULE, USER_MODULE} from './_prefix'

const PRODUCT_MODULE = `${API_MODULE}/products`

type StockInfo = {
    amount: number,
    frozen?: number
}

// 获取所有商品
export const getAllProducts = () => {
  return axios.get(`${PRODUCT_MODULE}`)
      .then(res => {
        return res
      })
}

// 获取单个商品详情
export const getProductById = (id: string) => {
  return axios.get(`${PRODUCT_MODULE}/${id}`).then(res => {
      return res
  })
}

// 添加商品
export const addProduct = (product: any,token:string) => {
  return axios.post(`${PRODUCT_MODULE}`, product,{headers: {
          "token": `${token}`
      }}).then(res => {
      return res
  })
}

// 更新商品
export const updateProduct = (product: any,token:string) => {
  return axios.put(`${PRODUCT_MODULE}`, product,{headers: {
          "token": `${token}`
      }}).then(res => {
      return res
  })
}

// 删除商品
export const deleteProduct = (id: string) => {
  return axios.delete(`${PRODUCT_MODULE}/${id}`).then(res => {
      return res
  })
}

// 获取商品库存
export const getProductStock = (productId: string) => {
  return axios.get(`${PRODUCT_MODULE}/stockpile/${productId}`).then(res => {
      return res
  })
}

// 修改商品库存
export const updateProductStock = (productId: string, stockInfo: StockInfo) => {
  return axios.patch(`${PRODUCT_MODULE}/stockpile/${productId}`,stockInfo,{headers: {'Content-Type': 'application/json'}}).then(res => {
      return res
  })
}