import {axios} from '../utils/request'
import {POST_MODULE,COMMENTS_MODULE,LIKES_MODULE} from './_prefix'

export const getPostDetails = (id: number) => {
    return axios.get(`${POST_MODULE}/${id}`).then(res => {
        return res
    })
}
export const getAllComments = (postId: number) => {
    return axios.get(`${COMMENTS_MODULE}/${postId}`).then(res => {
        return res
    })
}
export const giveLike = (requestBody) => {
    return axios.post(`${LIKES_MODULE}`,requestBody).then(res => {
        return res
    })

}

export const cancelLike = (requestBody) => {
    return axios.delete(`${LIKES_MODULE}`,requestBody).then(res => {
        return res
    })
}

export const giveComment = (requestBody) => {
    return axios.post(`${COMMENTS_MODULE}`,requestBody).then(res => {
        return res
    })
}

export const cancelComment = (commentId) => {
    return axios.delete(`${COMMENTS_MODULE}/${commentId}`).then(res => {
        return res
    })
}
export const deleteLike = (id) => {
    return axios.delete(`${LIKES_MODULE}/${id}`).then(res => {
        return res
    })
}