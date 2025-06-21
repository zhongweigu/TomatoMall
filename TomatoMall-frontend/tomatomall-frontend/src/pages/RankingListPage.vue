<template>
  <NavigationBar/>
  <div class="app-container">
    <el-card class="ranking-card">
      <template #header>
        <div class="card-header">
          <el-icon :size="28" class="title-icon"><Trophy /></el-icon>
          <h1> 番茄好评 </h1>
        </div>
      </template>

      <el-skeleton :loading="loading" animated :count="5">
        <template #template>
          <el-skeleton-item variant="image" style="width: 100px; height: 150px" />
          <div style="margin-left: 20px">
            <el-skeleton-item variant="h3" style="width: 30%" />
            <el-skeleton-item variant="text" style="width: 50%" />
            <el-skeleton-item variant="text" style="width: 40%" />
          </div>
        </template>

        <el-row :gutter="20">
          <el-col
              v-for="(book, index) in sortedBooks"
              :key="book.id"
              :xs="24"
              :sm="12"
              :lg="8"
          >
            <el-card class="book-card" shadow="hover">
              <div class="card-content">
                <el-tag
                    class="rank-tag"
                    :type="getRankType(index)"
                    effect="dark"
                    round
                >
                  #{{ index + 1 }}
                </el-tag>

                <el-image
                    :src="book.cover"
                    class="book-cover"
                    fit="cover"
                    @error="handleImageError"
                    @click="goToDetail(book.id)"
                >
                  <template #error>
                    <div class="image-error">
                      <el-icon><Picture /></el-icon>
                      <span>封面加载失败</span>
                    </div>
                  </template>
                </el-image>

                <div class="book-info">
                  <h3 class="book-title">{{ book.title }}</h3>
                  <div class="meta-info">
                    <el-rate
                        v-model="book.rate"
                        disabled
                        :max="10"
                        :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                    />
                    <div class="price-rate">
                      <span class="price">
                        <el-icon><Money /></el-icon>
                        {{ book.price.toFixed(2) }}
                      </span>
                      <el-tag type="warning" size="small">
                        {{ book.rate.toFixed(1) }}
                      </el-tag>
                    </div>
                  </div>
                  <el-text truncated class="description">
                    {{ book.description || '暂无描述' }}
                  </el-text>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-skeleton>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Trophy, Picture, Money } from '@element-plus/icons-vue'
import { getAllProducts } from '@/api/products'
import NavigationBar from "@/components/NavigationBar.vue";
import {router} from "@/router/index.js";

const books = ref([])
const loading = ref(true)

// 获取数据
const fetchData = async () => {
  try {
    const res = await getAllProducts()
    if(res.data.code === '200'){
      books.value = res.data.data
      console.log(books.value)
    }else {
      ElMessage.error('数据加载失败')
    }
  } catch (err) {
    ElMessage.error('数据加载失败')
  } finally {
    loading.value = false
  }
}

// 处理图片错误
const handleImageError = (e) => {
  e.target.src = 'https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png'
}

// 排名样式
const getRankType = (index) => {
  return index < 3 ? ['success', 'warning', 'danger'][index] : ''
}

// 排序逻辑
const sortedBooks = computed(() => {
  return [...books.value]
      .sort((a, b) => b.rate - a.rate)
      .slice(0, 12)
})

const goToDetail = (id) => {
  router.push({
    name: 'ProductDetail', // 路由名称
    params: { id: id } // 传递参数
  })
}

fetchData()
</script>

<style scoped>
.app-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;

  h1 {
    margin: 0;
    color: var(--el-color-primary);
  }
}

.ranking-card {
  margin-top: 5vh;
  border-radius: 12px;
}

.book-card {
  margin-bottom: 20px;
  height: 25vh;
  transition: transform 0.3s;

  &:hover {
    transform: translateY(-5px);
  }
}

.card-content {
  position: relative;
  display: flex;
  gap: 15px;
}

.rank-tag {
  position: absolute;
  top: -10px;
  left: -10px;
  z-index: 1;
  font-size: 16px;
  padding: 8px 12px;
}

.book-cover {
  width: 100px;
  height: 140px;
  border-radius: 6px;
  flex-shrink: 0;
}

.book-info {
  flex: 1;
  min-width: 0;

  .book-title {
    margin: 0 0 8px 0;
    color: var(--el-text-color-primary);
  }
}

.meta-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin: 10px 0;
}

.price-rate {
  display: flex;
  align-items: center;
  gap: 12px;

  .price {
    color: var(--el-color-danger);
    font-weight: bold;

    .el-icon {
      margin-right: 4px;
    }
  }
}

.description {
  color: var(--el-text-color-secondary);
  font-size: 13px;
  line-height: 1.4;
}

.image-error {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);

  .el-icon {
    font-size: 24px;
    margin-bottom: 8px;
  }
}

.title-icon {
  color: var(--el-color-warning);
}
</style>