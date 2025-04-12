import { axios } from '../utils/request'
import { IMAGE_MODULE } from './_prefix'

export const uploadImage = (file: File, type: string, token : string) => {
    const formData = new FormData();
    formData.append("file", file); // "file" 需要和后端 `@RequestParam` 参数名匹配
    formData.append("type", type);
    console.log(formData);
    console.log("请求上传图片");
    return axios.post(`${IMAGE_MODULE}/image`, formData, {
        headers: {
            "Content-Type": "multipart/form-data",
            "token": `${token}`
        }
    });
};