import {axios} from '../utils/request'
import {USER_MODULE} from './_prefix'

type LoginInfo = {
    username: string,
    password: string
}

type RegisterInfo = {
    role: string,
    name: string,
    username: string,
    telephone: string,
    email: string,
    password: string,
    storeId?: number,
    avatar?: string,
    location?: string,

}

type UpdateInfo = {
    username : String,
    name?: string,
    password?: string,
    location?: string,
    avatar?: string,
    email?: string,
    telephone?: string,
}

// 用户登录
export const userLogin = (loginInfo: LoginInfo) => {
    return axios.post(`${USER_MODULE}/login`, null, {params: loginInfo})
        .then(res => {
            console.log("res",res)
            return res
        })
}

// 用户注册
export const userRegister = (registerInfo: RegisterInfo) => {
    console.log(registerInfo)
    return axios.post(`/api/accounts/register`, registerInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            console.log(res)
            return res
        })
}

// 获取用户信息
export const userInfo = (username: String) => {
    console.log(username)
    return axios.get(`${USER_MODULE}/${username}`)
        .then(res => {
            return res
        })
}

// 更新用户信息
export const userInfoUpdate = (updateInfo: UpdateInfo, token : String) => {
    return axios.post(`${USER_MODULE}`, updateInfo, {headers: {'Content-Type': 'application/json', "token": `${token}`}})
        .then(res => {
            return res
        })
}
