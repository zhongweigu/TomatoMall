import { axios } from '../utils/request'
import { IMAGE_MODULE } from './_prefix'

export const uploadShopLogo = (file: File) => {
    const formData = new FormData();
    formData.append("file", file); // "file" 需要和后端 `@RequestParam` 参数名匹配

    return axios.post(`${IMAGE_MODULE}/shop`, formData, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
    });
};

export const uploadProductImage = (file: File) => {
    const formData = new FormData();
    formData.append("file", file); // "file" 需要和后端 `@RequestParam` 参数名匹配

    return axios.post(`${IMAGE_MODULE}/product`, formData, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
    });
};