<template>
  <div class="forum-page">
    <NavigationBar />

    <el-container class="main-content">
      <!-- 左侧分区导航 -->
      <el-aside width="240px" class="category-sidebar">
        <h3 class="sidebar-title">论坛分区</h3>
        <el-menu
            :default-active="selectedCategory"
            @select="handleCategoryChange"
            class="category-menu"
        >
          <el-menu-item index="all">
            <el-icon><Collection /></el-icon>
            <span>全部帖子</span>
          </el-menu-item>
          <el-menu-item index="BookComment">
            <el-icon><Star /></el-icon>
            <span>书籍评价</span>
          </el-menu-item>
          <el-menu-item index="SmallTalks">
            <el-icon><ChatDotRound /></el-icon>
            <span>番茄杂谈</span>
          </el-menu-item>
          <el-menu-item index="Creation">
            <el-icon><EditPen /></el-icon>
            <span>衍生创作</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="posts-container">
        <!-- 顶部操作栏 -->
        <div class="action-bar">
          <h2 class="section-title">{{ categoryTitleMap[selectedCategory] || '全部帖子' }}</h2>
          <el-button type="danger" @click="openPostDialog()">发布新帖</el-button>
        </div>

        <!-- 帖子列表 -->
        <div v-if="loading" class="loading-container">
          <el-skeleton :rows="6" animated />
        </div>

        <div v-else-if="posts.length === 0" class="empty-state">
          <el-empty description="暂无帖子，赶快发表第一篇吧！" />
        </div>

        <div v-else class="posts-list">
          <el-card
              v-for="post in posts"
              :key="post.id"
              class="post-card"
              @click="viewPostDetail(post)"
          >
            <div class="post-header">
              <h3 class="post-title">{{ post.title }}</h3>
              <el-tag size="small" :type="getCategoryTagType(post.category)">
                {{ categoryTitleMap[post.category] }}
              </el-tag>
            </div>

            <div class="post-content">
              <p class="post-text">{{ post.briefContent }}</p>



              <div v-if="post.image" class="post-image-container">
                <el-image
                    :src="post.image"
                    fit="cover"
                    class="post-image"
                    :preview-src-list="[post.image]"
                />
              </div>


            </div>

            <div class="post-footer">
              <div class="post-info">
                <span class="post-author">
                  <el-avatar :size="24" :src="post.avatar">{{ post.author }}</el-avatar>
                  {{ post.nickname }}
                </span>
                <span class="post-time">{{ formatDateTime(post.timeStamp) }}</span>
                <div v-if="post.category === 'BookComment'" class="rating-container">
                  <span class="rating-score">{{ post.rating || 0 }}</span>
                  <el-rate
                      v-model="post.rating"
                      :max="10"
                      :disabled="post.author !== username"
                      @change="updateRating(post.id, post.rating)"
                  />
                </div>

              </div>

              <div class="post-actions">
                <span class="action-item">
                  <el-icon><ChatDotSquare /></el-icon>
                  {{ post.commentsNumber || 0 }}
                </span>

                <span
                    class="action-item"
                >
                  <el-icon><Top /></el-icon>
                  {{ post.likes || 0 }}
                </span>

                <template v-if="canManagePost(post)">
                  <el-button
                      type="primary"
                      size="small"
                      round
                      @click.stop="openPostDialog(post)"
                  >
                    编辑
                  </el-button>
                  <el-button
                      type="danger"
                      size="small"
                      round
                      @click.stop="confirmDeletePost(post)"
                  >
                    删除
                  </el-button>
                </template>
              </div>
            </div>


          </el-card>
        </div>

        <!-- 分页控制 -->
        <div class="pagination-wrapper">
          <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 30]"
              layout="total, sizes, prev, pager, next"
              :total="posts.length"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
          />
        </div>
      </el-main>
    </el-container>

    <!-- 发帖/编辑帖子对话框 -->
    <el-dialog
        v-model="postDialogVisible"
        :title="isEditing ? '编辑帖子' : '发布新帖'"
        width="50%"
        top="5vh"
    >
      <el-form :model="postForm" label-width="80px" label-position="top">
        <el-form-item label="标题" required>
          <el-input v-model="postForm.title" placeholder="请输入标题（20字以内）" maxlength="20" show-word-limit />
        </el-form-item>

        <el-form-item label="分区" required>
          <el-select v-model="postForm.category" placeholder="请选择分区" style="width: 100%">
            <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="内容" required>
          <el-input
              v-model="postForm.content"
              type="textarea"
              placeholder="请输入内容..."
              :autosize="{ minRows: 6, maxRows: 12 }"
          />
        </el-form-item>

        <el-form-item label="图片">
          <el-upload
              class="post-image-upload"
              :http-request="handleImageUpload"
              :show-file-list="false"
              :before-upload="beforeImageUpload"
          >
            <div v-if="postForm.image" class="image-preview">
              <el-image :src="postForm.image" fit="cover" class="preview-image" />
              <div class="image-actions">
                <el-button type="danger" size="small" circle @click.stop="removeImage">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </div>
            </div>
            <el-button v-else type="primary" plain>
              <el-icon><Upload /></el-icon> 上传图片
            </el-button>
          </el-upload>
        </el-form-item>

        <el-form-item v-if="postForm.category === 'BookComment'" label="评分">
          <el-input v-model="postForm.bookId" placeholder="请输入书籍id" maxlength="20" show-word-limit />
          <div class="rating-input">
            <el-rate
                v-model="postForm.rating"
                :max="10"
                :allow-half="true"
                style="margin-right: 10px"
            />
            <span>{{ postForm.rating }}</span>
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="postDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="submitPost" :loading="submitting">
            {{ isEditing ? '更新' : '发布' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 帖子详情对话框 -->
    <el-dialog
        v-model="detailDialogVisible"
        :title="currentPost.title"
        width="60%"
        top="5vh"
    >
      <div v-if="currentPost.id" class="post-detail">
        <div class="post-detail-header">
          <div class="detail-author-info">
            <el-avatar :size="36" :src="currentPost.avatar">{{ currentPost.author }}</el-avatar>
            <div class="detail-meta">
              <span class="detail-author">{{ currentPost.author }}</span>
              <span class="detail-time">{{ formatDateTime(currentPost.timeStamp) }}</span>
            </div>
          </div>
          <el-tag size="small" :type="getCategoryTagType(currentPost.category)">
            {{ categoryTitleMap[currentPost.category] }}
          </el-tag>
        </div>

        <div class="post-detail-content">
          <p class="detail-text">{{ currentPost.content }}</p>

          <div v-if="currentPost.image" class="detail-image-container">
            <el-image
                :src="currentPost.image"
                fit="contain"
                class="detail-image"
                :preview-src-list="[currentPost.image]"
            />
          </div>
        </div>

        <div v-if="currentPost.category === 'BookComment'" class="detail-rating">
          <span class="rating-label">书籍评分：</span>
          <el-rate
              v-model="currentPost.rating"
              :max="10"
              :disabled="currentPost.author !== username"
              @change="updateRating(currentPost.id, currentPost.rating)"
          />
          <span class="rating-score">{{ currentPost.rating || 0 }}</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  List, ChatDotRound, Star, StarFilled, Promotion, QuestionFilled,
  View, Delete, Upload, EditPen, Collection, ChatDotSquare, Top
} from '@element-plus/icons-vue'
import NavigationBar from '@/components/NavigationBar.vue'
import {
  getAllPosts, getPostById, createPost, updatePost, deletePost,
  createRate, updateRate, togglePostLike
} from '@/api/forum'
import { uploadImage } from '@/api/image'

import { useRouter } from 'vue-router'

// setup 中声明 router
const router = useRouter()

// 当前用户信息
const username = sessionStorage.getItem('username')
const role = sessionStorage.getItem('role')

// 页面状态
const loading = ref(false)
const submitting = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const posts = ref([])
const selectedCategory = ref('all')

// 对话框控制
const postDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const isEditing = ref(false)
const currentPost = ref({})

// 帖子表单
const postForm = reactive({
  id: '',
  title: '',
  content: '',
  category: 'SmallTalks',
  image: '',
  rating: 0,
  bookId: ''
})

// 分区配置
const categoryTitleMap = {
  all: '全部帖子',
  BookComment: '书籍评价',
  Creation: '衍生创作',
  SmallTalks: '番茄杂谈'
}

const categoryOptions = [
  { label: '番茄杂谈', value: 'SmallTalks' },
  { label: '书籍评价', value: 'BookComment' },
  { label: '衍生创作', value: 'Creation' },
]

const userId = sessionStorage.getItem('userId')
// 判断用户是否可以管理帖子
const canManagePost = (post) => {
  return post.userId == userId || role == 'staff'
}

// 获取分类标签样式
const getCategoryTagType = (category) => {
  const typeMap = {
    general: '',
    review: 'danger',
    recommendation: 'success',
    question: 'warning'
  }
  return typeMap[category] || ''
}

// 格式化时间函数
const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return ''
  const date = new Date(dateTimeString)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 初始化加载帖子
onMounted(() => {
  fetchPosts()
})

// 获取帖子列表
const fetchPosts = async () => {
  loading.value = true
  try {

    const response = await getAllPosts()
    if (response.data.code === '200') {
      posts.value = response.data.data|| []
      console.log(posts.value)
      if(selectedCategory.value === 'all'){
        posts.value = response.data.data
      }else{
        posts.value = response.data.data.filter(post => post.category === selectedCategory.value)
      }
    } else {
      ElMessage.error(response.data.msg || '获取帖子列表失败')
    }
  } catch (error) {
    console.error('获取帖子列表出错:', error)
    ElMessage.error('网络错误，请重试')
  } finally {
    loading.value = false
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  fetchPosts()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchPosts()
}

// 分区切换
const handleCategoryChange = (category) => {
  selectedCategory.value = category
  currentPage.value = 1
  fetchPosts()
}

// 图片上传前验证
const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  return true
}

// 处理图片上传
const handleImageUpload = async (options) => {
  const { file } = options
  try {
    const response = await uploadImage(file, 'ForumPost', sessionStorage.getItem('token'))
    if (response.data.code === '200') {
      postForm.image = response.data.data
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error(response.data.msg || '图片上传失败')
    }
  } catch (error) {
    console.error('上传图片出错:', error)
    ElMessage.error('网络错误，请重试')
  }
}

// 移除已上传图片
const removeImage = () => {
  postForm.imageUrl = ''
}

// 打开帖子发布/编辑对话框
const openPostDialog = (post = null) => {
  resetPostForm()

  if (post) {
    isEditing.value = true
    postForm.id = post.id
    postForm.title = post.title
    postForm.content = ''
    postForm.category = post.category
    postForm.image = post.image || ''
    postForm.rating = post.rating || 0
    postForm.bookId = post.bookId || ''
  } else {
    isEditing.value = false
  }

  postDialogVisible.value = true
}

// 重置帖子表单
const resetPostForm = () => {
  postForm.id = ''
  postForm.title = ''
  postForm.content = ''
  postForm.category = 'general'
  postForm.imageUrl = ''
  postForm.rating = 0
}

// 提交帖子
const submitPost = async () => {
  if (!postForm.title.trim()) {
    ElMessage.warning('请输入帖子标题')
    return
  }

  if (!postForm.content.trim()) {
    ElMessage.warning('请输入帖子内容')
    return
  }

  submitting.value = true
  try {

    // 从会话存储中获取用户ID
    const userId = sessionStorage.getItem('userId');

    if (!userId) {
      // 如果没有用户ID，提示用户登录
      alert('请先登录');
      await router.push('/login');
      return;
    }

    let response
    const postData = {
      title: postForm.title,
      content: postForm.content,
      category: postForm.category,
      image: postForm.image,
      userId: parseInt(userId),
      rating: postForm.category === 'BookComment' ? postForm.rating : undefined,
      bookId: postForm.bookId
    }

    if (isEditing.value) {
      postData.id = postForm.id
      response = await updatePost(postData)
    } else {
      response = await createPost(postData)
    }

    if (response.data.code === '200') {
      ElMessage.success(isEditing.value ? '帖子更新成功' : '帖子发布成功')
      postDialogVisible.value = false
      await fetchPosts() // 刷新帖子列表
    } else {
      ElMessage.error(response.data.msg || (isEditing.value ? '更新失败' : '发布失败'))
    }
  } catch (error) {
    console.error('提交帖子出错:', error)
    ElMessage.error('网络错误，请重试')
  } finally {
    submitting.value = false
  }
}



// 帖子详情查看函数
const viewPostDetail = (post) => {
  router.push({
    path: `/forum/post/${post.id}`
  })
}

// 确认删除帖子
const confirmDeletePost = (post) => {
  ElMessageBox.confirm(
      `确定要删除标题为"${post.title}"的帖子吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(async () => {
    try {
      const response = await deletePost(post.id)
      if (response.data.code === '200') {
        ElMessage.success('删除成功')
        await fetchPosts() // 刷新列表
      } else {
        ElMessage.error(response.data.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除帖子出错:', error)
      ElMessage.error('网络错误，请重试')
    }
  })
}

// 更新评分
const updateRating = async (postId, rating) => {
  try {
    const response = await updateRate(postId, rating)
    if (response.data.code === '200') {
      ElMessage.success('评分更新成功')
    } else {
      ElMessage.error(response.data.msg || '评分更新失败')
    }
  } catch (error) {
    console.error('更新评分出错:', error)
    ElMessage.error('网络错误，请重试')
  }
}


</script>

<style scoped>
.forum-page {
  padding-top: 60px;
  min-height: 100vh;
  background-color: #f5f7fa;
  width: 100%;
  overflow-x: hidden; /* 防止水平滚动 */
}

.main-content {
  max-width: calc(100% - 40px);
  margin: 0 auto;
  padding: 20px;
  min-height: 90vh;
}

/* 左侧导航栏样式 */
.category-sidebar {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  margin-right: 20px;
  padding: 10px 0;
}

.sidebar-title {
  padding: 15px 20px;
  margin: 0;
  color: #333;
}

.category-menu {
  border-right: none;
}

/* 帖子列表容器 */
.posts-container {
  flex-grow: 1;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  padding: 20px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  margin: 0;
  color: #333;
}

.loading-container {
  padding: 20px 0;
}

.empty-state {
  padding: 40px 0;
}

/* 帖子卡片样式 */
.posts-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.post-card {
  cursor: pointer;
  transition: all 0.3s;
}

.post-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.post-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.post-content {
  display: flex;          /* 启用 Flex 布局 */
  gap: 20px;              /* 文本与图片间距 */
  align-items: flex-start; /* 顶部对齐 */
  margin-bottom: 15px;
}

.post-text {
  flex: 1;                /* 文本占据剩余空间 */
  margin: 0;
  color: rgba(96, 98, 102, 0.77);
}

.post-image-container {
  flex-shrink: 0;         /* 禁止图片容器收缩 */
  width: 150px;           /* 固定宽度（根据需求调整） */
  height: 100px;          /* 固定高度 */
  border-radius: 4px;
  overflow: hidden;
}

.post-image {
  width: 100%;            /* 图片填充容器 */
  height: 100%;
  object-fit: cover;      /* 缩放填充（保持比例裁剪） */
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.post-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.post-author {
  display: flex;
  align-items: center;
  gap: 5px;
  font-weight: 500;
  color: #409eff;
}

.post-time {
  color: #909399;
  font-size: 0.9em;
}

.post-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #909399;
  cursor: pointer;
}

.action-item:hover {
  color: #f56c6c;
}

/* 评分容器样式 */
.rating-container {
  margin-bottom: 10px;
  display: flex;
  align-items: flex-end;
}

.rating-label {
  margin-right: 5px;
  color: #606266;
}

.rating-score {
  margin-left: 5px;
  font-weight: 600;
  color: #f56c6c;
  margin-right: 5px;
  margin-bottom: 5px;
}

/* 发帖对话框样式 */
.image-preview {
  position: relative;
  width: 200px;
  height: 150px;
  border-radius: 4px;
  overflow: hidden;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-actions {
  position: absolute;
  top: 5px;
  right: 5px;
  display: flex;
  gap: 5px;
}

.rating-input {
  display: flex;
  align-items: center;
}

/* 帖子详情样式 */
.post-detail {
  padding: 10px;
}

.post-detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.detail-author-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.detail-meta {
  display: flex;
  flex-direction: column;
}

.detail-author {
  font-weight: 600;
  color: #303133;
}

.detail-time {
  font-size: 0.8em;
  color: #909399;
}

.post-detail-content {
  margin-bottom: 20px;
}

.detail-text {
  white-space: pre-wrap;
  line-height: 1.5;
  color: #303133;
}

.detail-image-container {
  margin-top: 20px;
  text-align: center;
}

.detail-image {
  max-width: 100%;
  max-height: 500px;
  border-radius: 4px;
}

.detail-rating {
  display: flex;
  align-items: center;
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
}

/* 分页样式 */
.pagination-wrapper {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    flex-direction: column;
  }

  .category-sidebar {
    width: 100% !important;
    margin-right: 0;
    margin-bottom: 20px;
  }

  .post-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .post-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .post-actions {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>