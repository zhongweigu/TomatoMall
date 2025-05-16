<script setup  >
import { useRoute } from 'vue-router';
import {reactive, ref} from 'vue'

import {Comment, Pointer, Upload} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {cancelLike, deleteLike, getAllComments, getPostDetails, giveComment, giveLike} from "@/api/postDetail.js";
import {getProductById} from "@/api/products.js";
import {userInfo} from "@/api/accounts.js";
const route = useRoute();
const postId = route.params.id;
const apiData = ref([])
// 静态假数据
const postData = ref({
  id: 1,
  category: "BookComment",
  title: "《JavaScript 高级程序设计》读后感",
  content: "这是一本非常好的前端学习书籍，系统性地讲解了 JavaScript 的核心概念...",
  image: "https://picsum.photos/800/400",
  comments_number: 3,
  likes: 25,
  rate: 4.0,
  time_stamp: "2024-03-15 14:30",
  user_id: 1,
  book_id: 101
})
function getPostData() {
  getPostDetails(postId).then(res => {
    Object.keys(res).forEach(key => {
      if (Object.hasOwn(postData.value, key)) {
        postData.value[key] = res.data.data[key]
      }
    })
  })
}
getPostData()
const UserInfo = ref({
  id: 1,
  nickname: "前端小白",
  avatar: "https://picsum.photos/50"
})
function getUserInfo(userId) {
  userInfo(userId).then(res => {
    UserInfo.value.id = res.data.data.username;
    UserInfo.value.nickname = res.data.data.name;
    UserInfo.value.avatar = res.data.data.avatar;
  })
}
getUserInfo(postData.value.user_id)

const bookInfo = ref({
  id: 101,
  title: "JavaScript 高级程序设计（第4版）",
  author: "马特·弗里斯比",
  cover: "https://picsum.photos/150/200",
  rate: 4.5
})

function getBookInfo(bookId) {
  getProductById(bookId).then(res => {
    bookInfo.value.id = bookId;
    bookInfo.value.title = res.data.data.title;
    bookInfo.value.author = res.data.data.specifications[0].value;
    bookInfo.value.cover = res.data.data.cover;
    bookInfo.value.rate = res.data.data.rate;
  })
}
 getBookInfo(postData.value.book_id)
const comments = ref([
  {
    id: 1,
    isLiked: false,
    content: "同感！这本书确实非常适合系统学习",
    likes: 5,
    time_stamp: "2024-03-15 15:00",
    nickname: "技术达人",
    avatar: "https://picsum.photos/50?1",
    user_id: 1

  },
  {
    id: 2,
    isLiked: false,
    content: "请问适合新手阅读吗？",
    image: "https://picsum.photos/200/100",
    likes: 2,
    time_stamp: "2024-03-15 15:30",
    nickname: "学习新人",
    avatar: "https://picsum.photos/50?2",
    user_id: 2


  }
])
function getComments(postId) {
  getAllComments(postId).then(res => {
    apiData.value=res.data.data
  })

  const newComments =[]
  for (let i = 0; i < apiData.value.length; i++) {
    const item = apiData.value[i]

    // 构建符合要求的 comment 对象
    const transformed = {
      id: item.id,
      content: item.content,
      // 如果接口没有返回 image 字段，默认设为 null
      image: item.image ,
      time_stamp: item.time_stamp,
      // 补充接口没有的字段
      isLiked: false,
      likes: 0,
      user: {
        id: item.user_id ,
        nickname: item.nickname,
        avatar: item.avatar
      }
    }

    newComments.push(transformed)
  }
  return newComments;
}

 comments.value = getComments(postId)
// 时间格式化函数
const formatTime = (timeString) => {
  return new Date(timeString).toLocaleString()
}
const isLiked = ref(false)
const toggleLike = () => {
  const requestBody ={
    userId: sessionStorage.getItem("username"),
    id:postId,
  }
  if (isLiked.value) {
    postData.value.likes--;
    cancelLike(requestBody);

  } else {
    postData.value.likes++;
    giveLike(requestBody)
  }
  isLiked.value = !isLiked.value;
};
const commentlike = (comment)=>{
  const requestBody ={
    userId: sessionStorage.getItem("username"),
    id:comment.id,
  }
  if(comment.isLiked){
    cancelLike(requestBody);
  }
  else {giveLike(requestBody)}
  comment.isLiked = !comment.isLiked
  comment.likes += comment.isLiked ? 1 : -1
}
const dialogVisible = ref(false)

// 表单数据
const commentForm = reactive({
  content: '',
  image: null
})

// 图片预览
const imagePreview = ref('')

// 打开弹窗
const openCommentDialog = () => {
  dialogVisible.value = true
}

// 提交评论
const submitComment = () => {
  if (!commentForm.content.trim()) {
    ElMessage.error('评论内容不能为空')
    return
  }

  // 生成新评论

  const requestBody ={
    user_id : sessionStorage.getItem("username"),
    post_id: postId,
    content: commentForm.content,
    image:imagePreview.value
  }


  // 重置表单
  commentForm.content = ''
  commentForm.image = null
  imagePreview.value = ''
  dialogVisible.value = false
  giveComment(requestBody)
  ElMessage.success('评论发表成功')
  getAllComments(postId)

}
const handleImageUpload = (file) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    imagePreview.value = e.target.result
  }
  reader.readAsDataURL(file.raw)
}
const deleteComment = (comment) => {
  ElMessageBox.confirm(
      '确定要删除这条评论吗？',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(() => {
    const index = comments.value.findIndex(c => c.id === comment.id)
    if (index > -1) {
      comments.value.splice(index, 1)
      deleteLike(comment.id)
      deleteComment(comment.id)
      ElMessage.success('评论删除成功')
      getAllComments(postId)
    }

  }).catch(() => {
    // 取消删除不做操作
  })
}

</script>



<template>

    <div class="post-detail-container">
      <!-- 帖子主体 -->
      <el-card class="post-card">
        <!-- 用户信息 -->
        <div class="user-info">
          <el-avatar :size="50" :src="UserInfo.avatar" />
          <div class="user-meta">
            <h3>{{ UserInfo.nickname }}</h3>
            <span class="post-time">{{ formatTime(postData.time_stamp) }}</span>
          </div>
        </div>

        <!-- 帖子内容 -->
        <div class="post-content">
          <h2>{{ postData.title }}</h2>
          <div class="content-text">{{ postData.content }}</div>
          <img
              v-if="postData.image"
              :src="postData.image"
              class="post-image"
              alt="帖子图片"
          />
        </div>
        <div class="interaction">
          <el-button type="info" plain>
            <template #icon>
              <el-icon><Comment /></el-icon>
            </template>
            {{ postData.comments_number }} 条评论
          </el-button>

          <el-button
              :type="isLiked ? 'danger' : 'primary'"
              plain
              @click="toggleLike"
          >
            <template #icon >
              <el-icon ><Pointer /></el-icon>
            </template>
            {{ postData.likes }} 点赞
          </el-button>
        </div>

        <!-- 书籍信息 -->
        <div v-if="postData.category === 'BookComment'" class="book-info">
          <el-divider>关联书籍信息</el-divider>
          <div class="book-meta">
            <el-image
                :src="bookInfo.cover"
                class="book-cover"
                fit="contain"
            />
            <div class="book-details">
              <h3>{{ bookInfo.title }}</h3>
              <p>
                综合评分：
              <el-rate
                  v-model="bookInfo.rate"
                  disabled
                  show-score
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
              />
              </p>
              <p>
                帖主评分：
                <el-rate
                    v-model="postData.rate"
                    disabled
                    show-score
                    :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                />
              </p>
              <p>作者: {{ bookInfo.author }}</p>
            </div>
          </div>
        </div>

        <!-- 互动信息 -->

      </el-card>

      <!-- 评论列表 -->
      <el-card class="comment-section">
        <template #header>
          <div class="comment-header">
            <span>全部评论（{{ comments.length }}）</span>
            <el-button @click="openCommentDialog">
              添加评论
            </el-button>
          </div>
        </template>




        <el-dialog
            v-model="dialogVisible"
            title="发表评论"
            width="40%"
        >
          <el-form
              label-position="top"
              @submit.prevent="submitComment"
          >
            <el-form-item label="评论内容">
              <el-input
                  v-model="commentForm.content"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入您的评论..."
                  show-word-limit
                  maxlength="300"
              />
            </el-form-item>

            <el-form-item label="添加图片">
              <el-upload
                  action="#"
                  :auto-upload="false"
                  :show-file-list="false"
                  :on-change="handleImageUpload"
              >
                <el-button type="info" plain>
                  <el-icon><Upload /></el-icon>
                  选择图片
                </el-button>
              </el-upload>
              <el-image
                  v-if="imagePreview"
                  :src="imagePreview"
                  class="preview-image"
              />
            </el-form-item>

            <div class="dialog-footer">
              <el-button @click="dialogVisible = false">取消</el-button>
              <el-button
                  type="primary"
                  native-type="submit"
              >
                发表评论
              </el-button>
            </div>
          </el-form>
        </el-dialog>



        <el-form>
          <el-form-item
              v-for="comment in comments"
              :key="comment.id"
              class="comment-item"
          >
            <template #default>
              <div class="comment-content">
                <div class="comment-user">
                  <el-avatar :size="40" :src="comment.avatar" />
                  <span class="user-name">{{ comment.nickname }}</span>
                </div>
                <div class="comment-body">
                  <p>{{ comment.content }}</p>
                  <el-image
                      v-if="comment.image"
                      :src="comment.image"
                      class="comment-image"
                  />
                </div>
                <div class="comment-actions">
                  <span class="comment-time">{{ formatTime(comment.time_stamp) }}</span>
                  <el-button link  :class="{ 'liked-comment': comment.isLiked }" @click="commentlike(comment)" >
                    <template #icon>
                      <el-icon><Pointer /></el-icon>
                    </template>
                    {{ comment.likes }}
                  </el-button>
                  <el-button
                      v-if="comment.user_id === UserInfo.id"
                      link
                      type="danger"
                      @click="deleteComment(comment)"
                  >
                    删除
                  </el-button>
                </div>
              </div>
            </template>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

</template>

<style scoped>
.post-detail-container {
  max-width: 1000px;
  margin: 20px auto;
  padding: 0 20px;
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.user-meta {
  margin-left: 15px;
}

.user-meta h3 {
  margin: 0;
  font-size: 18px;
}

.post-time {
  color: #999;
  font-size: 14px;
}

.post-content {
  margin: 25px 0;
}

.post-image {
  max-width: 100%;
  border-radius: 4px;
  margin-top: 15px;
}

.book-info {
  margin-top: 30px;
}

.book-meta {
  display: flex;
  gap: 20px;
}

.book-cover {
  width: 150px;
  height: 200px;
  border-radius: 4px;
}

.book-details {
  flex: 1;
}

.interaction {
  margin-top: 20px;
  display: flex;
  gap: 15px;
}

.comment-item {
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.comment-user {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.user-name {
  margin-left: 10px;
  font-weight: 500;
}

.comment-image {
  max-width: 300px;
  margin-top: 10px;
}

.comment-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 10px;
  color: #666;
}
.liked-comment {
  .el-icon {
    color: #ff4d4f;
    transform: scale(1.1);
    transition: all 0.3s ease;
  }
}
.comment-header {
  display: flex;
  justify-content: space-between; /* 两端对齐 */
  align-items: center;
  width: 100%;
}

</style>