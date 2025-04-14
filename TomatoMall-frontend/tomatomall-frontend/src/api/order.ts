import {axios} from '../utils/request'
import {ORDER_MODULE} from './_prefix'


export const pay = (orderId : number) => {
    return axios.post(`${ORDER_MODULE}/${orderId}/pay`).then(res => {
        return res
    })
}

// 用于订单管理页面获取所有订单
export const getAllCartsOrdersRelation = () => {
    return axios.get(`${ORDER_MODULE}/`).then(res => {
        return res
    })
}

// 用于订单管理页面获取订单详情
export const getCartsOrdersRelation = (relationId: string) => {
    return axios.get(`${ORDER_MODULE}/${relationId}`).then(res => {
        return res
    })
}

// 用于删除订单
export const deleteCartsOrdersRelation = (relationId: string) => {
    return axios.delete(`${ORDER_MODULE}/${relationId}`).then(res => {
        return res
    })
}
