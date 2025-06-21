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
            <el-icon :size="28" color="#409EFC" class="title-icon">
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
            <el-button type="danger" round size="large" class="action-btn" @click="handleAddToCart">
              <el-icon :size="20" class="btn-icon">
                <ShoppingCart />
              </el-icon>
              加入购物车
            </el-button>
<!--            <el-button type="danger" round size="large" class="action-btn" @click="handleBuyNow">-->
<!--              <el-icon :size="20" class="btn-icon">-->
<!--                <Wallet />-->
<!--              </el-icon>-->
<!--              立即购买-->
<!--            </el-button>-->
            <el-button type="danger" round size="large" class="action-btn" @click="handleReading">
              <el-icon :size="20" class="btn-icon">
                <Reading />
              </el-icon>
              在线试读
            </el-button>
          </div>
        </div>
      </transition>
    </div>
  </div>

  <!-- 添加订单确认对话框 -->
  <el-dialog v-model="orderDialogVisible" title="订单确认" width="50%">
    <div class="order-content">
      <h3>购买商品</h3>
      <el-card shadow="hover" class="product-preview">
        <div class="product-info-row">
          <el-image
              :src="productData.cover || defaultImage"
              fit="cover"
              class="product-preview-image"
              style="scale: min(76%, 100%)"
          ></el-image>
          <div class="product-preview-info">
            <h1>{{ productData.title }}</h1>
            <p class="product-price">单价: ¥{{ productData.price }}</p>
            <el-input-number
                v-model="buyQuantity"
                :min="1"
                :max="maxQuantity"
                size="small"
                label="数量"
            ></el-input-number>
          </div>
        </div>
      </el-card>

      <div class="shipping-info">
        <h3>收货信息</h3>
        <el-form :model="orderInfo.shipping" label-width="100px">
          <el-form-item label="收货人">
            <el-input v-model="orderInfo.shipping.name" placeholder="请输入收货人姓名"></el-input>
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="orderInfo.shipping.telephone" placeholder="请输入联系电话"></el-input>
          </el-form-item>
          <el-form-item label="邮政编码">
            <el-input v-model="orderInfo.shipping.zipcode" placeholder="请输入邮政编码"></el-input>
          </el-form-item>
          <el-form-item label="收货地址">
            <el-input v-model="orderInfo.shipping.address" placeholder="请输入详细地址"></el-input>
          </el-form-item>
        </el-form>
      </div>

      <div class="payment-method">
        <h3>支付方式</h3>
        <div class="payment-info">
          <el-radio v-model="orderInfo.paymentMethod" label="alipay" disabled>支付宝</el-radio>
          <div class="payment-notice">
            <i class="el-icon-info"></i>
            当前仅支持支付宝支付（沙箱环境）
          </div>
        </div>
      </div>

      <div class="order-summary">
        <h1 class="total-amount">订单总金额：¥{{ (productData.price * buyQuantity).toFixed(2) }}</h1>
      </div>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="orderDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="submitOrder">提交订单</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 支付确认对话框 -->
  <el-dialog v-model="orderConfirmDialogVisible" title="订单支付" width="40%">
    <div class="payment-content" v-if="currentOrder">
      <el-result icon="success" title="订单创建成功" subTitle="请确认订单信息后进行支付">
        <template #extra>
          <div class="order-details">
            <p><strong>订单号：</strong> {{ currentOrder.orderNo }}</p>
            <p><strong>创建时间：</strong> {{ parseTime(currentOrder.createTime) }}</p>
            <p><strong>订单金额：</strong> ¥{{ currentOrder.totalAmount }}</p>
            <p><strong>收货地址：</strong> {{ currentOrder.address }}</p>
          </div>
          <el-button type="danger" @click="handlePay">确认支付</el-button>
          <el-button @click="orderConfirmDialogVisible = false">取消</el-button>
        </template>
      </el-result>
    </div>
  </el-dialog>
</template>

<script setup>
import {ref, reactive, onMounted, toRaw} from 'vue';
import Navigation from "@/components/NavigationBar.vue";
import { getProductById, getProductStock } from "@/api/products.js";
import { ElMessage } from "element-plus";
import { pay } from "@/api/order.js";
import {checkout, getAllCarts, updateCart} from "@/api/carts.js";
import { useRoute } from 'vue-router';
import { parseTime } from '@/utils/index';
import {
  Reading,
  PriceTag,
  ShoppingCart,
  Wallet,
  User,
  School,
  Collection,
  DocumentCopy, Clock
} from '@element-plus/icons-vue';
import { addCart } from "@/api/carts.js";
import {userInfo} from "@/api/accounts.js";
import {router} from "@/router/index.js";

const route = useRoute()
const id = route.params.id
const defaultImage = 'https://tomato-mall-milvelas.oss-cn-nanjing.aliyuncs.com/BookCover/defaultCover.png'
const productData = ref({})
const specIcons = ref({
  '作者': User,
  '出版社': School,
  '商品编码（ISBN）': Collection,
  '页数': DocumentCopy,
  '出版时间': Clock,
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

const username = sessionStorage.getItem("username");
const email = ref('')
const name = ref('')
const telephone = ref('')
const location = ref('')
function getUserInfo() {
  userInfo(username).then(res => {
    console.log(res)
    name.value = res.data.data.name
    telephone.value = res.data.data.telephone
    location.value = res.data.data.location
    email.value = res.data.data.email
  })
}
getUserInfo()

const cartItems = ref([]);
const loading = ref(false);
// 获取购物车列表（正常）
const fetchCarts = async () => {
  loading.value = true;
  try {
    const response = await getAllCarts();
    if (response.data.code === "200") {
      cartItems.value = response.data.data;
      console.log(cartItems.value);
      ElMessage.success("购物车加载成功");
    } else {
      ElMessage.error(response.data.msg || "购物车加载失败");
    }
  } catch (error) {
    console.error("获取购物车列表出错:", error);
    ElMessage.error("网络错误，请重试");
  } finally {
    loading.value = false;
  }
};
onMounted(() => {
  fetchCarts();
});

// 订单相关的响应式数据
const orderDialogVisible = ref(false);
const orderConfirmDialogVisible = ref(false);
const buyQuantity = ref(1);
const orderInfo = reactive({
  shipping: {
    name: name,
    telephone: telephone,
    zipcode: 210000,
    address: location,
  },
  paymentMethod: "alipay"
});
const currentOrder = ref(null);

const maxQuantity = ref(1);
// 处理立即购买
const handleBuyNow = async () => {
  if (!productData.value.id) {
    ElMessage.warning('商品信息不完整');
    return;
  }

  try {
    // 获取商品库存
    const stockResponse = await getProductStock(productData.value.id);
    if (stockResponse.data.code === "200") {
      const stockInfo = stockResponse.data.data;
      const availableStock = stockInfo.amount;

      if (availableStock <= 0) {
        ElMessage.warning("商品库存不足");
        return;
      }

      // 设置最大购买数量
      buyQuantity.value = 1; // 默认购买数量为1
      maxQuantity.value = availableStock; // 存储最大可购买数量
      orderDialogVisible.value = true;
    } else {
      ElMessage.error("获取库存信息失败");
    }
  } catch (error) {
    console.error("获取库存信息出错:", error);
    ElMessage.error("网络错误，请重试");
  }
};

const handleReading = () => {
  router.push({
    name: 'ReadingPage', // 路由名称
    params: { id: id } // 传递参数
  })
}

// 添加到购物车
const handleAddToCart = async () => {
  try {
    // 获取商品库存
    const stockResponse = await getProductStock(productData.value.id);
    if (stockResponse.data.code === "200") {
      const stockInfo = stockResponse.data.data;
      const availableStock = stockInfo.amount - stockInfo.frozen;

      if (availableStock <= 0) {
        ElMessage.warning("商品库存不足");
        return;
      }

      const existingItem = cartItems.value.find(item =>
          Number(item.productId) === Number(id)
      );
      console.log(existingItem);
      if(existingItem && existingItem.status === "PENDING") {
        const newQuantity = existingItem.quantity + 1;

        if (newQuantity > availableStock) {
          ElMessage.warning(`库存不足，最多可添加 ${availableStock} 件`);
          return;
        }

        const updateRes = await updateCart(
            {
              cartItemId: existingItem.cartItemId,
              quantity: newQuantity
            },
            sessionStorage.getItem("token")
        );
        if (updateRes.data.code === "200") {
          ElMessage.success("商品数量已更新");
        } else {
          ElMessage.error(updateRes.data.msg || "更新失败");
        }
        return
      }

      // 调用添加购物车API
      const cartData = {
        productId: productData.value.id,
        quantity: 1  // 默认添加1件，也可以设置变量让用户选择数量
      };

      const response = await addCart(cartData, sessionStorage.getItem("token"));
      if (response.data.code === "200") {
        ElMessage.success("已添加到购物车");
      } else {
        ElMessage.error(response.data.msg || "添加失败");
      }

    } else {
      ElMessage.error("获取库存信息失败");
    }
  } catch (error) {
    console.error("添加购物车出错:", error);
    ElMessage.error("网络错误，请重试");
  }
};

// 提交订单
const submitOrder = async () => {
  if (!orderInfo.shipping.name || !orderInfo.shipping.telephone ||
      !orderInfo.shipping.address || !orderInfo.shipping.zipcode) {
    ElMessage.warning("请完整填写收货信息");
    return;
  }

  try {
    const cartData = {
      productId: productData.value.id,
      quantity: buyQuantity.value  // 默认添加1件，也可以设置变量让用户选择数量
    };
    await addCart(cartData, sessionStorage.getItem("token"));
    // 直接购买时，使用一个特殊的结构传给后端
    const checkoutData = {
      cartItemIds: Array.from({ length: buyQuantity.value }, () => productData.value.id),
      shipping_address: orderInfo.shipping,
      paymentMethod: orderInfo.paymentMethod
    };

    const response = await checkout(checkoutData);
    if (response.data.code === '200') {
      currentOrder.value = response.data.data;
      orderDialogVisible.value = false;
      orderConfirmDialogVisible.value = true;
      ElMessage.success("订单创建成功");
    } else {
      ElMessage.error(response.data.msg || "创建订单失败");
    }
  } catch (error) {
    console.error("提交订单出错:", error);
    ElMessage.error("网络错误，请重试");
  }
};

// 处理支付
const handlePay = () => {
  if (!currentOrder.value || !currentOrder.value.orderId) {
    ElMessage.error("订单信息不完整");
    return;
  }

  // 打开支付宝沙箱支付页面，以查询参数方式传递订单号
  // 动态创建表单
  const form = document.createElement('form');
  form.method = 'POST';
  form.action = `http://localhost:8080/api/orders/${currentOrder.value.orderId}/pay`; // 你的支付接口
  form.target = '_blank'; // 关键：在新窗口打开
  console.log(form);

// 将表单插入页面并自动提交
  document.body.appendChild(form);
  form.submit();
  document.body.removeChild(form); // 提交后清理


  orderConfirmDialogVisible.value = false;

};
// DONE: 立即购买，同样是 确认订单-支付-返回 流程，cartPage写完了应该很好写。
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
  margin-left: 2vw;
  width: 20vw;
  height: 40vh;
  border-radius: 12px;
  overflow: hidden;
  margin-top: 20px;
  background: rgb(255, 255, 255);
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
  margin-left: 23vw;
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
  background: rgb(255, 255, 255);
  margin-left: 0vh;
  min-width: calc(80vw - 300px);
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.3);
}

.product-title {
  font-size: 38px;
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
  font-size: 28px;
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
  white-space: pre-wrap; /* 关键样式 */
  word-break: break-word;
}

.detail-description {
  margin: 25px 0;
}

.spec-label {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 15vw;
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