<template>
  <Navigation></Navigation>
  <div class="product-detail-container">
    <!-- 商品主体 -->
    <div class="fixed-image-container">
      <div class="image-wrapper">
        <el-image
            :src="productData.cover || defaultImage"
            :preview-src-list="[productData.cover]"
            fit="contain"
            class="product-cover"
        />
      </div>
    </div>

    <div class="content-container">
      <transition name="slide-fade">
        <div class="product-info">
          <h1 class="product-title">
            <el-icon :size="24" color="#409EFC" class="title-icon">
              <Reading />
            </el-icon>
            {{ productData.title }}
          </h1>

          <div class="price-rate">
            <div class="price-wrapper">
              <el-icon :size="28" color="#ff4500" class="price-icon">
                <PriceTag />
              </el-icon>
              <span class="product-price">¥{{ productData.price }}</span>
            </div>
            <el-divider />
            <div class="product-rate">
              <el-rate
                  v-model="productData.rate"
                  :max="10"
                  disabled
                  show-score
                  score-template="{value} 分"
                  :colors="['#ff4500', '#ff4500', '#ff4500']"
              />
            </div>
          </div>

          <div class="product-description">
            <el-card shadow="hover" class="description-card">
              <p>{{ productData.description }}</p>
              <p class="detail">{{ productData.detail }}</p>
            </el-card>
          </div>

          <el-divider border-style="dashed" />

          <!-- 商品规格 -->
          <el-descriptions
              title="图书信息"
              :column="1"
              border
              :label-width="100"
              class="detail-description"
          >
            <el-descriptions-item
                v-for="spec in productData.specifications"
                :key="spec.id"
                :label="spec.item"
            >
              <template #label>
                <div class="spec-label">
                  <el-icon>
                    <component :is="specIcons[spec.item]" />
                  </el-icon>
                  {{ spec.item }}
                </div>
              </template>
              <span class="spec-value">{{ spec.value }}</span>
            </el-descriptions-item>
          </el-descriptions>

          <!-- 操作按钮 -->
          <div class="action-buttons">
            <el-button type="danger" round size="large" class="action-btn">
              <el-icon :size="20" class="btn-icon">
                <ShoppingCart />
              </el-icon>
              加入购物车
            </el-button>
            <el-button type="danger" round size="large" class="action-btn">
              <el-icon :size="20" class="btn-icon">
                <Wallet />
              </el-icon>
              立即购买
            </el-button>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Navigation from "@/components/NavigationBar.vue";
import { getProductById } from "@/api/products.js";
import { ElMessage } from "element-plus";
import {pay} from "@/api/order.js";
import {checkout} from "@/api/carts.js";
import { useRoute } from 'vue-router'
import {
  Reading,
  PriceTag,
  ShoppingCart,
  Wallet,
  User,
  School,
  Collection,
  DocumentCopy
} from '@element-plus/icons-vue'

const route = useRoute()
const id = route.params.id
const defaultImage = 'https://tomato-mall-milvelas.oss-cn-nanjing.aliyuncs.com/BookCover/defaultCover.png'
const productData = ref({})
const specIcons = ref({
  '作者': User,
  '出版社': School,
  'ISBN': Collection,
  '分类': DocumentCopy
})

const loadData = async () => {
  try {
    const res = await getProductById(id)
    if(res.data.code === '200') {
      productData.value = res.data.data
      ElMessage.success("商品加载成功")
    }
  } catch (error) {
    ElMessage.error("商品加载失败")
  }
}
loadData()

// TODO: 立即购买，同样是 确认订单-支付-返回 流程，cartPage写完了应该很好写。
//    不同之处在于: cartPage的checkout需要统计cartItemIds和总金额，这里cartItemIds就一个当前页书的id，金额就是该书的金额
//    建议风格统一，显示订单模态框。

</script>

<style scoped>
.product-detail-container {
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
  position: relative;
}

.main-content {
  margin-top: 50px;
  transition: all 0.3s ease;
}

/* 固定图片容器 */
.fixed-image-container {
  position: fixed;
  left: 40px; /* 距离屏幕左侧固定距离 */
  top: 80px;
  width: 360px; /* 图片区域宽度 */
  height: calc(100vh - 120px);
  z-index: 99;
}

.image-wrapper {
  width: 100%;
  height: 40%;
  border-radius: 12px;
  overflow: hidden;
  margin-top: 20px;
  background: rgba(223, 180, 154, 0.13);
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.3);
}


.product-cover {
  width: 100%;
  height: 100%;
  object-fit: contain;
  transition: transform 0.3s ease;
}

.product-cover:hover {
  transform: scale(1.03);
}

/* 内容容器 - 精确间距控制 */
.content-container {
  position: relative;
  margin-left: 500px;
  margin-top: 70px;
  padding-right: 40px; /* 距离屏幕右侧固定距离 */
  max-width: calc(100% - 440px - 40px); /* 100% - 左边距 - 右边距 */
}

.content-wrapper {
  max-width: 800px; /* 内容最大宽度 */
  margin: 0 auto;
  padding: 40px 0;
}


.product-info {
  background: rgba(223, 180, 154, 0.13);
  margin-left: -200px;
  min-width: calc(80vw - 300px);
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.3);
}

.product-title {
  font-size: 28px;
  color: #303133;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.price-rate {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 25px 0;
}

.price-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
}

.product-price {
  font-size: 32px;
  color: #ff4500;
  font-weight: 700;
}

:deep(.el-rate) {
  --el-rate-icon-size: 28px;
}

.description-card {
  margin: 15px 0;
  border-radius: 8px;
}

.detail {
  margin-top: 12px;
  color: #606266;
  line-height: 1.8;
}

.detail-description {
  margin: 25px 0;
}

.spec-label {
  display: flex;
  align-items: center;
  gap: 8px;
}

.spec-value {
  font-weight: 500;
  color: #303133;
}

.action-buttons {
  margin-top: 35px;
  display: flex;
  gap: 20px;
  justify-content: center;
}

.action-btn {
  padding: 18px 35px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255,69,0,0.3);
}

.btn-icon {
  margin-right: 8px;
}

/* 动效 */
.slide-fade-enter-active {
  transition: all 0.5s ease-out;
}

.slide-fade-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

@media (max-width: 768px) {
  .product-detail-container {
    padding: 20px 15px;
  }

  .product-info {
    padding: 20px;
  }


  .product-title {
    font-size: 22px;
  }

  .product-price {
    font-size: 28px;
  }

  .action-buttons {
    flex-direction: column;
  }

  .action-btn {
    width: 100%;
  }
}
</style>