<script setup  >
import { useRoute } from 'vue-router';
import {computed, markRaw, onMounted, reactive, ref} from 'vue'

import {Comment, Delete, Pointer, Upload} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {
  cancelComment,
  cancelLike,
  deleteLike,
  getAllComments,
  getPostDetails,
  getRating,
  giveComment,
  giveLike
} from "@/api/postDetail.js";
import {getProductById} from "@/api/products.js";
import {userInfo} from "@/api/accounts.js";
import NavigationBar from "@/components/NavigationBar.vue";
import {router} from "@/router/index.js";
import {uploadImage} from "@/api/image.js";
const route = useRoute();
const postId = route.params.id;
const apiData = ref([])
const postData = ref({})

onMounted(() => {
  InitPage()
})
const userId = sessionStorage.getItem("userId")
const role = sessionStorage.getItem("role")
const InitPage = async () => {
  await fetchPostData()
  console.log('帖子数据:',postData.value)
  if(postData.value.category === 'BookComment'){
    await getBookInfo()
  }
  await getComments()
}
const fetchPostData = async () => {
  try {
    const res = await getPostDetails(postId);
    if(res.data.code === '200'){
      console.log('res.data.data:',res.data.data)
      postData.value = res.data.data
      postData.content = res.data.data.content.replace(/\r?\n/g, '\n')
      console.log('content:',postData.content)
    }else {
      ElMessage.error(res.data.msg)
    }
  }catch (error) {
    console.error('获取帖子详情失败:',error);
    ElMessage.error('网络错误，请重试')
  }
}

const bookInfo = ref({})

const getBookInfo = async () => {
  try {
    console.log('bookId:' , postData.value.bookId)
    const res = await getProductById(postData.value.bookId);
    if(res.data.code === '200'){
      bookInfo.value = res.data.data
      console.log('书籍信息:',bookInfo.value)
    }else {
      ElMessage.error(res.data.msg)
    }
  }catch (error) {
    console.error('获取书籍详情失败:',error);
    ElMessage.error('网络错误，请重试')
  }
}

const goToBookDetail = (bookId) => {
  router.push({
    path: `/product/${bookId}`
  })
}

const comments = ref([])
const getComments = async () => {
  try {
    const res = await getAllComments(postId);
    if(res.data.code === '200'){
      comments.value = res.data.data
      console.log('评论信息:',comments.value)
    }else {
      ElMessage.error(res.data.msg)
    }
  }catch (error) {
    console.error('获取评论失败:',error);
    ElMessage.error('网络错误，请重试')
  }
}
// 时间格式化函数
const formatTime = (timeString) => {
  return new Date(timeString).toLocaleString()
}
const isLiked = computed({
  get: () => postData.value.liked,
  set: (value) => postData.value.liked = value
})
const toggleLike = async () => {
  const requestBody ={
    userId: sessionStorage.getItem("userId"),
    postId: postId,
  }
  if (postData.value.liked) {
    postData.value.likesCount--;
    postData.value.liked = false;
    const res = await cancelLike(requestBody);
    if(res.data.code === '200'){
      ElMessage.success('取消点赞成功')
    }else {
      ElMessage.error(res.data.msg)
    }
  } else {
    postData.value.likesCount++;
    postData.value.liked = true;
    const res = await giveLike(requestBody);
    if(res.data.code === '200'){
      ElMessage.success('点赞成功')
    }else {
      ElMessage.error(res.data.msg)
    }
  }
};
const commentlike = async (comment)=>{
  const requestBody ={
    userId: sessionStorage.getItem("userId"),
    commentId: comment.id,
  }
  console.log(requestBody)
  if(comment.liked){
    const res = await cancelLike(requestBody);
    if(res.data.code === '200'){
      ElMessage.success('取消点赞成功')
    }else {
      ElMessage.error(res.data.msg)
    }
  }
  else {
    const res = await giveLike(requestBody);
    if(res.data.code === '200'){
      ElMessage.success('点赞成功')
    }else {
      ElMessage.error(res.data.msg)
    }
  }
  comment.liked = !comment.liked
  comment.likesCount += comment.liked ? 1 : -1
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
const submitComment = async () => {
  if (!commentForm.content.trim()) {
    ElMessage.error('评论内容不能为空')
    return
  }

  // 生成新评论

  const requestBody ={
    userId : sessionStorage.getItem("userId"),
    postId: postId,
    content: commentForm.content,
    image:imagePreview.value
  }

  console.log(requestBody)

  try {
    const res = await giveComment(requestBody);
    if(res.data.code === '200'){
      ElMessage.success('评论发表成功')
    }else{
      ElMessage.error(res.data.msg)
    }
  }catch (error) {
      console.error('提交评论失败:',error);
      ElMessage.error('网络错误，请重试')
  }


  // 重置表单
  commentForm.content = ''
  commentForm.image = null
  imagePreview.value = ''
  dialogVisible.value = false
  await getAllComments(postId)

}
const handleImageUpload = async (options) => {
  const { file } = options
  try {
    const response = await uploadImage(file, 'Comment', sessionStorage.getItem('token'))
    if (response.data.code === '200') {
      imagePreview.value = response.data.data
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error(response.data.msg || '图片上传失败')
    }
  } catch (error) {
    console.error('上传图片出错:', error)
    ElMessage.error('网络错误，请重试')
  }
}

const removeImage = () => {
  imagePreview.value = '';
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
  ).then(async () => {
    const res = await cancelComment(comment.id);
    if(res.data.code === '200'){
      ElMessage.success('删除评论成功')
      comments.value = comments.value.filter(item => item.id !== comment.id)
    }else {
      ElMessage.error('删除失败:',res.data.msg)
    }
  }).catch(() => {
    // 取消删除不做操作
  })
}

const categoryTitleMap = {
  BookComment: '评书',
  Creation: '衍生',
  SmallTalks: '杂谈'
}

</script>



<template>
  <NavigationBar/>
    <div class="post-detail-container">
      <!-- 帖子主体 -->
      <el-card class="post-card">
        <!-- 用户信息 -->
        <div class="user-info">
          <el-avatar :size="50" :src="postData.avatar" />
          <div class="user-meta">
            <h3>{{ postData.nickname }}</h3>
            <span class="post-time">{{ formatTime(postData.timeStamp) }}</span>
          </div>
        </div>
        <el-divider></el-divider>

        <!-- 帖子内容 -->
        <div class="post-content">

          <h2>
            <el-tag size="large" :type="'primary'">
              {{ categoryTitleMap[postData.category] }}
            </el-tag>
            {{ postData.title }}</h2>
          <div class="content-text">{{ postData.content }}</div>
          <img
              v-if="postData.image"
              :src="postData.image"
              class="post-image"
              alt="帖子图片"
          />
        </div>
        <div class="interaction">
          <el-button type="success" plain>
            <template #icon>
              <el-icon><Comment /></el-icon>
            </template>
            {{ postData.commentsNumber }} 条评论
          </el-button>

          <el-button
              :type="postData.liked ? 'danger' : 'primary'"
              plain
              @click="toggleLike"
          >
            <template #icon >
              <el-icon ><Pointer /></el-icon>
            </template>
            {{ postData.likesCount }} 点赞
          </el-button>
        </div>

        <!-- 书籍信息 -->
        <div v-if="postData.category === 'BookComment'" class="book-info">
          <el-divider>相关书籍</el-divider>
          <div class="book-meta">
            <el-image
                :src="bookInfo.cover"
                class="book-cover"
                fit="contain"
                @click="goToBookDetail(postData.bookId)"
            />
            <div class="book-details">
              <h3>{{ bookInfo.title }}</h3>
              <p>
                综合评分：
              <el-rate
                  v-model="bookInfo.rate"
                  disabled
                  :max="10"
                  show-score
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
              />
              </p>
              <p>
                帖主评分：
                <el-rate
                    v-model="postData.rating"
                    disabled
                    :max="10"
                    show-score
                    :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                />
              </p>
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

            <el-form-item label="图片">
              <el-upload
                  class="post-image-upload"
                  :http-request="handleImageUpload"
                  :show-file-list="false"
              >
                <div v-if="imagePreview" class="image-preview">
                  <el-image :src="imagePreview" fit="cover" class="preview-image" />
                  <div class="image-actions">
                    <el-button type="danger" size="small" circle @click="removeImage">
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </div>
                </div>
                <el-button v-else type="primary" plain>
                  <el-icon><Upload /></el-icon> 上传图片
                </el-button>
              </el-upload>
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


        <div v-if="comments.length === 0" class="empty-state">
          <el-empty description="暂无评论，赶快发表评论吧！" />
        </div>


        <el-form v-else>
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
                  <p class="content-text">{{ comment.content }}</p>
                  <el-image
                      v-if="comment.image"
                      :src="comment.image"
                      class="comment-image"
                  />
                </div>
                <div class="comment-actions">
                  <span class="comment-time">{{ formatTime(comment.timeStamp) }}</span>

                  <div class="action-buttons">
                    <el-button round :class="{ 'liked-comment': comment.liked }" @click="commentlike(comment)" >
                      <template #icon>
                        <el-icon><Pointer /></el-icon>
                      </template>
                      {{ comment.likesCount }}
                    </el-button>
                    <el-button
                        v-if="comment.userId == userId || role == 'staff'"
                        type="danger"
                        round
                        @click="deleteComment(comment)"
                    >
                      删除
                    </el-button>
                  </div>
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
  max-width: 80vw;
  margin: 10vh auto;
  padding: 0 20px;
  min-width: 60vw;
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

.content-text{
  white-space: pre-wrap; /* 关键样式 */
  word-break: break-word;
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
  margin-left: auto; /* 左侧自动填充剩余空间 */
  width: fit-content; /* 宽度根据内容自适应 */
}

/* 卡片容器样式 */
.comment-section {
  max-width: 800px;
  margin: 20px auto;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  min-width: 50vw;
}

/* 头部样式 */
.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #eee;
}

.comment-header span {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

/* 评论项整体样式 */
.comment-item {
  margin-bottom: 20px;
  padding: 16px;
  transition: all 0.3s ease;
  border-radius: 8px;
}

.comment-item:hover {
  background-color: #f8f9fa;
}

/* 评论内容布局 */
.comment-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 100%;
}

/* 用户信息 */
.comment-user {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-name {
  font-weight: 500;
  color: #2c3e50;
  font-size: 15px;
}

/* 评论正文 */
.comment-body {
  margin-left: 52px; /* 与头像对齐 */
}

.comment-body p {
  margin: 8px 0;
  line-height: 1.6;
  color: #34495e;
  font-size: 14px;
}

.comment-image {
  margin-top: 12px;
  max-width: 300px;
  border-radius: 6px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.comment-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-top: 8px;
}

.comment-time {
  /* 自动占据左侧空间 */
  margin-right: auto;
  color: #95a5a6;
  font-size: 12px;
}

/* 新增按钮容器 */
.action-buttons {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}
/* 点赞按钮 */
.liked-comment {
  color: #ff4757 !important;
  border-color: #ff4757 !important;
}

/* 头像样式 */
.el-avatar {
  border: 2px solid #eee;
}

/* 响应式处理 */
@media (max-width: 768px) {
  .comment-section {
    margin: 10px;
  }

  .comment-actions {
    flex-wrap: wrap;
    justify-content: flex-start;
    gap: 12px;
  }

  .comment-image {
    max-width: 200px;
  }
}

</style>