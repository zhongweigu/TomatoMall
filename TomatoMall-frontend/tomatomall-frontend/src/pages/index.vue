<script setup >
import {ElButton, ElCard,  ElMain} from "element-plus";
import {computed, onMounted, reactive, ref} from "vue"
import { ElMessage } from 'element-plus'
import {router} from "@/router/index.js";
import NavigationBar from "@/components/NavigationBar.vue";

import {getAllProducts} from "@/api/products.js";
const role =sessionStorage.getItem('role');
const allProducts = ref([])
const defaultImage = 'https://tomato-mall-milvelas.oss-cn-nanjing.aliyuncs.com/BookCover/defaultCover.png'
onMounted(() => {
  fetchProducts()
})

// 获取所有商品
const fetchProducts = async () => {
  try {
    // 使用假数据时,可以注释掉
    const response = await getAllProducts()
    if (response.data.code === '200') {
      allProducts.value = response.data.data
    } else {
      ElMessage.error(response.data.msg || '获取商品列表失败')
    }

  } catch (error) {
    console.error('获取商品列表出错:', error)
    ElMessage.error('网络错误，请重试')
  }
}


const selectedCategory = ref('')
const categories = ref([
  { label: '全部书籍', value: '', count: computed(() => allProducts.value.length) },
  { label: '科学技术', value: 'science',
    count: computed(() => allProducts.value.filter(p => p.type === 'science').length) },
  { label: '文学小说', value: 'literature', count: computed(() => allProducts.value.filter(p => p.type === 'literature').length) }, // 示例其他分类
  { label: '历史经典', value: 'history', count: computed(() => allProducts.value.filter(p => p.type === 'history').length) }
])

// 优化后的商品筛选逻辑
const filteredProducts = computed(() =>
    selectedCategory.value
        ? allProducts.value.filter(p => p.type === selectedCategory.value)
        : allProducts.value
)

// 分页总数动态计算
const totalItems = computed(() => filteredProducts.value.length)

const pagination = reactive({
  current: 1,   // 当前页码
  size: 12,      // 每页展示数量
  total: totalItems       // 数据总数（需通过API获取）
})

// 带分类过滤的分页商品
const productList = computed(() => {
  const start = (pagination.current - 1) * pagination.size
  return filteredProducts.value.slice(start, start + pagination.size)
})
const handlePageChange = (newPage) => {
  pagination.current = newPage

}
const handleCategorySelect = (category) => {
  selectedCategory.value = category
  pagination.current = 1 // 切换分类时重置到第一页
  window.scrollTo({ top: 0, behavior: 'smooth' }) // 平滑滚动到顶部
}

function seeDetail(id) {
  router.push({
    name: 'ProductDetail', // 路由名称
    params: { id: id } // 传递参数
  })
}


</script>

<template>
  <NavigationBar></NavigationBar>
  <el-main class="main-container">
    <div class="layout-wrapper">
      <!-- 左侧分类栏 -->
      <el-card class="category-card">
        <h3 class="category-header">书籍分类</h3>
        <el-menu
            :default-active="selectedCategory"
            @select="handleCategorySelect"
            class="category-menu"
        >
          <el-menu-item
              v-for="cat in categories"
              :index="cat.value"
              :key="cat.value"
              class="category-item"
          >
            <span class="category-label">{{ cat.label }}</span>
            <span class="category-count">{{ cat.count }}</span>
          </el-menu-item>
        </el-menu>
      </el-card>

      <!-- 右侧商品列表 -->
      <el-card class="product-list-card">
        <div class="list-header">
          <h2 class="subtitle">产品列表</h2>
          <router-link to="/personal" v-slot="{ navigate }">
            <el-button
                v-if="role === 'manager'"
                @click="navigate"
                type="success"
                round
                size="large"
                class="edit-btn"
            >
              编辑商品列表
            </el-button>
          </router-link>
        </div>

        <el-divider class="custom-divider"></el-divider>

        <div v-if="productList.length > 0" class="product-grid">
          <div
              v-for="product in productList"
              :key="product.id"
              class="product-card"
              @click="seeDetail(product.id)"
          >
            <div class="image-container">
              <img
                  :src="product.cover || defaultImage"
                  alt="Product"
                  class="product-image"
              />
              <div class="hover-overlay"></div>
            </div>
            <div class="product-details">
              <h3 class="product-name">{{ product.title }}</h3>
              <el-text class="product-description">{{ product.description }}</el-text>
              <div class="price-section">
                <span class="price-tag">¥ {{ product.price }}</span>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="empty-state">
          <el-empty description="暂无商品信息">
            <el-button
                v-if="role === 'manager'"
                type="primary"
                class="add-btn"
            >
              立即添加商品
            </el-button>
          </el-empty>
        </div>

        <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :total="pagination.total"
            layout="prev, pager, next"
            class="pagination-wrapper"
            @current-change="handlePageChange"
        />
      </el-card>
    </div>
  </el-main>
</template>

<style scoped>
.main-container {
  min-height: calc(100vh - 50px);
  min-width: calc(100vw - 50px);
  padding: 20px;
  text-align: center;
  justify-content: center;
  max-width: 100vw;
  display: flex;
  overflow-y: hidden;
  overflow-x: hidden;
  background-color: rgba(188, 51, 49, 0.07);
}

.layout-wrapper {
  display: flex;
  gap: 20px;
  margin-top: 50px;
  min-height: calc(100vh - 60px);
  min-width: calc(100vw - 60px);
  max-width: 100vw;
}

/* 左侧分类样式 */
.category-card {
  width: 260px;
  flex-shrink: 0;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.category-header {
  color: #303133;
  font-size: 18px;
  margin: 0 0 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.category-menu {
  border-right: none;
}

.category-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 48px;
  margin: 6px 0;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.category-item:hover {
  background-color: #f5f7fa;
}

.category-item.is-active {
  background-color: #ecf5ff;
  color: #409EFF;
}

.category-count {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

/* 右侧商品列表 */
.product-list-card {
  flex: 1;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.subtitle {
  font-size: 22px;
  color: #303133;
  margin: 0;
}

/* 商品卡片样式 */
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  padding: 10px 0;
}

.product-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.12);
}

.image-container {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .product-image {
  transform: scale(1.05);
}

.hover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(180deg, rgba(0,0,0,0) 60%, rgba(0,0,0,0.1) 100%);
}

.product-details {
  padding: 16px;
  background-color: rgba(245, 149, 108, 0.13);
}

.product-name {
  font-size: 16px;
  color: #303133;
  margin: 0 0 8px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-description {
  font-size: 13px;
  color: #606266;
  line-height: 1.4;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.price-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.price-tag {
  font-size: 18px;
  color: #f56c6c;
  font-weight: 600;
}

/* 分页样式 */
.pagination-wrapper {
  margin-bottom: 30px;
  display: flex;
  justify-content: center;
}

/* 空状态样式 */
.empty-state {
  padding: 40px 0;
}

.add-btn {
  padding: 10px 24px;
  border-radius: 20px;
}

.edit-btn {
  padding: 12px 24px;
}

@media (max-width: 768px) {
  .layout-wrapper {
    flex-direction: column;
  }

  .category-card {
    width: 100%;
  }

  .product-grid {
    grid-template-columns: 1fr;
  }
}
</style>