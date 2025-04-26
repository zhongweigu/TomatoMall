import {axios} from '../utils/request'
import {ADVER_MODULE} from './_prefix'

type AdInfo = {
    id?: string,
    title: string,
    content: string,
    imgUrl: string,
    productId: string,
}

export const createAd = (adInfo : AdInfo) => {
    return axios.post(`${ADVER_MODULE}`, adInfo,{headers: {'Content-Type': 'application/json'}}).then(res => {
        return res
    })
}

export const getAllAds = () => {
    return axios.get(`${ADVER_MODULE}`).then(res => {
        return res
    })
}

export const updateAd = (adInfo : AdInfo) => {
    return axios.put(`${ADVER_MODULE}`, adInfo,{headers: {'Content-Type': 'application/json'}}).then(res => {
        return res
    })
}

export const deleteAd = (id : Number) => {
    return axios.delete(`${ADVER_MODULE}/${id}`).then(res => {
        return res
    })
}