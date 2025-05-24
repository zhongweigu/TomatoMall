import { axios } from '../utils/request'
import { API_MODULE } from './_prefix'

const FORUM_MODULE = `${API_MODULE}`

// 获取所有帖子
export const getAllPosts = (category?: string) => {
    const params = category && category !== 'all' ? { category } : {}
    return axios.get(`${FORUM_MODULE}/posts`, { params }).then(res => {
        return res
    })
}

// 获取单个帖子详情
export const getPostById = (id: string) => {
    return axios.get(`${FORUM_MODULE}/posts/${id}`).then(res => {
        return res
    })
}

// 创建帖子
export const createPost = (postData: any) => {
    console.log('提交的帖子数据:', postData);
    return axios.post(`${FORUM_MODULE}/posts`, postData,{headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res;
        })
        .catch(err => {
            console.error('创建帖子错误:', err.response?.data || err);
            throw err;
        });
}

// 更新帖子
export const updatePost = (postData: any) => {
    console.log('提交的帖子数据:', postData);
    return axios.put(`${FORUM_MODULE}/posts`, postData).then(res => {
        return res
    })
}

// 删除帖子
export const deletePost = (id: string) => {
    return axios.delete(`${FORUM_MODULE}/posts/${id}`).then(res => {
        return res
    })
}

// 创建评分
export const createRate = (rateData: any) => {
    return axios.post(`${FORUM_MODULE}/rates`, rateData).then(res => {
        return res
    })
}

// 更新评分
export const updateRate = (id: string, score: number) => {
    return axios.put(`${FORUM_MODULE}/rates/${id}`, { score }).then(res => {
        return res
    })
}

// 点赞/取消点赞帖子
export const togglePostLike = (postId: string) => {
    return axios.post(`${FORUM_MODULE}/posts/${postId}/like`).then(res => {
        return res
    })
}