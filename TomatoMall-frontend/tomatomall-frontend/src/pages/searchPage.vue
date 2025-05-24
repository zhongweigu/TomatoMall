<template>
  <NavigationBar/>
  <div class="book-search-container">
    <!-- 搜索栏 -->
    <el-row :gutter="20" class="search-bar">
      <el-col :span="18">
        <el-input
            v-model="searchQuery"
            placeholder="搜索书籍标题或描述"
            clearable
            @keyup.enter="searchBooks"
            class="search-input"
        >
          <template #prefix>
            <el-icon class="search-icon"><search /></el-icon>
          </template>
        </el-input>
      </el-col>
      <el-col :span="6">
        <el-select
            v-model="sortBy"
            placeholder="排序方式"
            @change="handleSortChange"
            class="sort-select"
        >
          <el-option label="默认排序" value="default" />
          <el-option label="价格从低到高" value="price_asc" />
          <el-option label="价格从高到低" value="price_desc" />
          <el-option label="评分从高到低" value="rate_desc" />
        </el-select>
      </el-col>
    </el-row>

    <!-- 书籍列表 -->
    <div v-loading="loading" element-loading-text="正在加载..." class="book-list-container">
      <el-row :gutter="20" class="book-list">
        <el-col
            v-for="book in books"
            :key="book.id"
            :xs="24" :sm="12" :md="8" :lg="6" :xl="4"
        >
          <el-card class="book-card" shadow="hover" @click="goToDetail(book.id)">
            <div class="cover-container">
              <img
                  :src="book.cover || defaultCover"
                  alt="书籍封面"
                  class="book-cover"
                  @error="handleImageError"
              />
            </div>

            <div class="book-info">
              <h3 class="title">{{ book.title }}</h3>
              <div class="meta-info">
                <div class="rating-container">
                  <el-rate
                      v-model="book.rate"
                      disabled
                      size="small"
                      :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                      :max="10"
                  />
                  <span class="rating-text">{{ book.rate.toFixed(1) }}</span>
                </div>
                <div class="price-container">
                  <span class="price-symbol">¥</span>
                  <span class="price-number">{{ book.price.toFixed(2) }}</span>
                </div>
              </div>

              <div class="description">
                {{ truncateDescription(book.description) }}
              </div>

              <el-tag
                  :type="getTagType(book.type)"
                  class="type-tag"
                  effect="dark"
                  round
              >
                {{ typeLabels[book.type] || book.type }}
              </el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-empty
          v-if="!loading && books.length === 0"
          description="未找到相关书籍"
          class="empty-state"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { getSearchedProducts } from "@/api/products.js";
import { ElMessage } from "element-plus";
import NavigationBar from "@/components/NavigationBar.vue";
import {router} from "@/router/index.js";

const defaultCover = ref('https://cdn.pixabay.com/photo/2016/09/10/17/18/book-1659717_1280.jpg')

// 响应式数据
const searchQuery = ref('')
const sortBy = ref('default')
const books = ref([])
const originalBooks = ref([]) // 保存原始数据用于排序
const loading = ref(false)

// 类型标签映射
const typeLabels = {
  literature: '文学经典',
  history: '历史著作',
  science: '科学技术'
}

const goToDetail = (id) => {
  router.push({
    name: 'ProductDetail', // 路由名称
    params: { id: id } // 传递参数
  })
}
// 获取书籍数据（移除排序参数）
const searchBooks = async () => {
  try {
    loading.value = true
    const response = await getSearchedProducts(searchQuery.value)
    originalBooks.value = response.data.data
    console.log('originalBooks', originalBooks.value)
    handleSortChange() // 初始排序
  } catch (error) {
    ElMessage.error('获取数据失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 前端排序处理
const handleSortChange = () => {
  try {
    // 安全访问
    const source = originalBooks.value || []

    if(originalBooks.length == 0) {
      return
    }

    // 二次验证
    if (!Array.isArray(source)) {
      throw new Error('原始数据不是数组')
    }

    const sorted = [...source]

    switch(sortBy.value) {
      case 'price_asc':
        sorted.sort((a, b) => a.price - b.price)
        break
      case 'price_desc':
        sorted.sort((a, b) => b.price - a.price)
        break
      case 'rate_desc':
        sorted.sort((a, b) => b.rate - a.rate)
        break
    }

    books.value = sorted
  } catch (error) {
    ElMessage.error('排序失败: ' + error.message)
    console.error('排序错误详情:', error)
  }
}
// 工具方法
const truncateDescription = (desc) => {
  return desc?.length > 100 ? desc.substring(0, 100) + '...' : desc
}

const handleImageError = (e) => {
  e.target.src = defaultCover.value
}

const getTagType = (type) => {
  const typeMap = {
    'science': 'success',
    'literature': 'primary',
    'history': 'warning',
  }
  return typeMap[type] || ''
}

</script>

<style scoped lang="scss">
.book-search-container {
  max-width: 80vw;
  justify-content: center;
  align-items: center;
  margin: auto;
  padding: 0 1.5rem;

  .search-bar {
    margin-bottom: 2rem;
    margin-top: 15vh;
    width: 60vw;

    .search-input {
      :deep(.el-input__wrapper) {
        border-radius: 2rem;
        padding: 0.5rem 1.5rem;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);

        .search-icon {
          font-size: 1.2rem;
          margin-right: 0.5rem;
          color: var(--el-color-primary);
        }
      }
    }

    .sort-select {
      width: 100%;

      :deep(.el-input__wrapper) {
        border-radius: 2rem;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
      }
    }
  }

  .book-list-container {
    min-height: 60vh;
    position: relative;

    .book-list {
      margin: -0.75rem;
    }

    .book-card {
      margin: 0.75rem;
      border-radius: 1rem;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      border: none;
      min-width: 18vw;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
      }

      .cover-container {
        height: 300px;
        border-radius: 0.8rem;
        overflow: hidden;
        background: #f5f7fa;

        .book-cover {
          width: 100%;
          height: 100%;
          object-fit: cover;
          transition: transform 0.4s ease;

          &:hover {
            transform: scale(1.05);
          }
        }
      }

      .book-info {
        padding: 1.5rem;

        .title {
          margin: 0 0 1rem;
          font-size: 1.1rem;
          font-weight: 600;
          color: #2c3e50;
          line-height: 1.4;
          min-height: 3.2em;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }

        .meta-info {
          @apply flex items-center justify-between mb-4;

          .rating-container {
            @apply flex items-center gap-1;

            .rating-text {
              font-size: 0.9rem;
              color: #f7ba2a;
              font-weight: 500;
            }
          }

          .price-container {
            .price-symbol {
              font-size: 0.9rem;
              color: #f56c6c;
              vertical-align: super;
            }

            .price-number {
              font-size: 1.4rem;
              color: #f56c6c;
              font-weight: 700;
            }
          }
        }

        .description {
          font-size: 12px;
          @apply text-gray-500 text-sm leading-relaxed mb-4;
          min-height: 3.6em;
          display: -webkit-box;
          -webkit-line-clamp: 3;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }

        .type-tag {
          @apply text-sm px-3 py-1;
        }
      }
    }

    .empty-state {
      @apply absolute left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2;
    }
  }
}

@media (max-width: 768px) {
  .book-search-container {
    padding: 0 1rem;

    .search-bar {
      margin-bottom: 1.5rem;

      .el-col {
        width: 100%;
        margin-bottom: 1rem;
      }
    }

    .book-card {
      .cover-container {
        height: 240px;
      }

      .price-number {
        font-size: 1.2rem !important;
      }
    }
  }
}
</style>