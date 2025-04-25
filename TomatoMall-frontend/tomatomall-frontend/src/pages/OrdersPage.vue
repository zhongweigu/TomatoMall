<script setup>
import NavigationBar from "@/components/NavigationBar.vue";
import {computed, onMounted, ref} from "vue";
import {getAllCartsOrdersRelation, getOrderByOrderId} from "@/api/order.ts";
import {dayjs, ElMessage} from "element-plus";
import {getCartByCartItemId} from "@/api/carts.js";
const initEmptyRow = () => ({
  id: '',
  cartItemId: '',
  orderId: '',
  cover: '',
  title: '',
  quantity: 0,
  status: '',
  total: 0,
  time: '',
  paymentMethod: '',
  price: ''
})
const userId = sessionStorage.getItem('userId')
const username = sessionStorage.getItem("username");
const allRows = ref([])
const allOrdersRelation = ref([])
const loading = ref(true)
const fetchOrdersRelation =  async () => {
  loading.value = true
  Loading();
   try{
     const res = await getAllCartsOrdersRelation();
     if (res.data.code === '200') {
       allOrdersRelation.value = res.data.data;
       // 并行处理所有请求
       const promises = allOrdersRelation.value.map(async (item) => {
         const row = initEmptyRow();
         row.id = item.id;
         row.cartItemId = item.cartItemId;
         row.orderId = item.orderId;

         // 并行获取订单和购物车项
         const [orderRes, cartItemRes] = await Promise.all([
           getOrderByOrderId(item.orderId),
           getCartByCartItemId(item.cartItemId)
         ]);

         // 处理订单数据
         if (orderRes.data.code === '200') {
           Object.assign(row, {
             status: orderRes.data.data.status,
             paymentMethod: orderRes.data.data.paymentMethod,
             time: orderRes.data.data.createTime,
             total: orderRes.data.data.totalAmount
           });
         }

         // 处理购物车项数据
         if (cartItemRes.data.code === '200') {
           if (userId !== cartItemRes.data.data.userId) {
             Object.assign(row, {
               quantity: cartItemRes.data.data.quantity,
               title: cartItemRes.data.data.title,
               cover: cartItemRes.data.data.cover,
               price: cartItemRes.data.data.price
             });
             return row;
           }
         }
         return null;
       });

       // 等待所有请求完成并过滤无效项
       allRows.value = (await Promise.all(promises)).filter(Boolean);
     }
   }
   catch(error){
     console.error('获取订单列表出错:', error)
     ElMessage.error('网络错误，请重试')
   } finally {
     loading.value = false // 结束加载
     LoadingSuccess();
   }
}
onMounted(() => {
  fetchOrdersRelation()
})

// 对话框控制
const orderDialogVisible = ref(false)
const selectedOrder = ref('');
const selectedCartItem = ref('');


// 行点击处理
const handleRowClick = async (row) => {
  const order = await getOrderByOrderId(row.orderId);
  if (order.data.code === '200') {
    selectedOrder.value = order.data.data;
  } else {
    ElMessage.error(order.data.message || '获取order失败');
  }

  const cartItem = await getCartByCartItemId(row.cartItemId);
  if (order.data.code === '200') {
    selectedCartItem.value = cartItem.data.data;
    console.log(selectedCartItem.value);
  } else {
    ElMessage.error(cartItem.data.message || '获取cartItem失败');
  }

  orderDialogVisible.value = true;
};

// 行悬停样式
const tableRowClassName = ({ rowIndex }) => {
  return rowIndex % 2 === 0 ? 'hover-row' : '';
};

const selectedStatus = ref('ALL')

const statusCategories = computed(() => {
  return [
   {
     value: 'ALL',
     label: '全部订单',
     count: allRows.value.length
   },
   {
     value: 'PENDING',
     label: '待付款',
     count: allRows.value.filter(item => item.status === 'PENDING').length

   },
   {
     value: 'SUCCESS',
     label: '已发货',
     count: allRows.value.filter(item => item.status === 'SUCCESS').length

   },
   {
     value: 'FAILED',
     label: '支付失败',
     count: allRows.value.filter(item => item.status === 'FAILED').length

   },
   {
      value: 'TIMEOUT',
      label: '订单超时',
      count: allRows.value.filter(item => item.status === 'TIMEOUT').length

   }
 ]
})
// 在组件脚本部分
const statusConfig = {
  PENDING: {
    type: 'warning',
    effect: 'light',
    text: '待支付'
  },
  SUCCESS: {
    type: 'success',
    effect: 'dark',
    text: '已完成'
  },
  FAILED: {
    type: 'danger',
    effect: 'plain',
    text: '失败'
  },
  TIMEOUT: {
    type: 'info',
    effect: 'light',
    text: '超时'
  }
}
const getStatusType = (status) => {
  return statusConfig[status]?.type || 'info'
}
const statusTextMap = computed(() => {
  return Object.fromEntries(
      Object.entries(statusConfig).map(([key, val]) => [key, val.text])
  )
})

const statusEffectMap = computed(() => {
  return Object.fromEntries(
      Object.entries(statusConfig).map(([key, val]) => [key, val.effect])
  )
})

// 状态选择处理（参考网页3的事件处理）
const handleStatusSelect = (status) => {
  selectedStatus.value = status
  console.log(selectedStatus.value)
}

// 过滤后的订单数据（基于网页2的计算属性模式）
const filteredOrders = computed(() => {
  return selectedStatus.value === 'ALL'
      ? allRows.value
      : allRows.value.filter(item => item.status === selectedStatus.value)
})


// 日期格式化
const formatDateTime = (timestamp) => {
  return dayjs(timestamp).format('YYYY-MM-DD HH:mm:ss')
}

const Loading = () => {
  ElMessage.info('加载中...')
}
const LoadingSuccess = () => {
  ElMessage.success('加载成功！')
}

</script>

<template>
  <NavigationBar ></NavigationBar>

  <el-main>
    <br><br>

    <div v-if="loading">
    </div>
    <div v-else>
      <el-row :gutter="20" style="background-color: transparent">
        <el-col :span="4">
          <h3 class="category-header">订单状态</h3>
          <el-menu
              :default-active="selectedStatus"
              @select="handleStatusSelect"
              class="category-menu"
          >
            <el-menu-item
                v-for="status in statusCategories"
                :index="status.value"
                :key="status.value"
                class="category-item"
            >
              <span class="category-label">{{ status.label }}</span>
              <span class="category-count">{{ status.count }}</span>
            </el-menu-item>
          </el-menu>
        </el-col>

        <el-col :span="20">
          <el-table
              :data="filteredOrders"
              style="width: 100%"
              @row-click="handleRowClick"
              :row-class-name="tableRowClassName"
          >
            <!-- 商品图片列 -->
            <el-table-column label="商品封面" width="250">
              <template #default="{ row }">
                <div class="product-info">
                  <img
                      :src="row.cover"
                      class="product-image"
                      alt="商品封面"
                  />
                </div>
              </template>
            </el-table-column>

            <el-table-column  label="商品名称" width="300">
              <template #default="{ row }">
                <div class="title">{{row.title}}</div>
              </template>
            </el-table-column>
            <el-table-column  label="数量" width="200">
              <template #default="{ row }">
                <div class="title">{{ row.quantity }}</div>
              </template>
            </el-table-column>

            <!-- 价格列 -->
            <el-table-column prop="price" label="单价" width="200" >
              <template #default="{ row }" >
                ￥{{ row.total.toFixed(2) }}
              </template>
            </el-table-column>

            <!-- 订单状态列 -->
            <el-table-column label="状态" width="200">
              <template #default="{ row }">
                <el-tag
                    :type="getStatusType(row.status)"
                    :effect="statusEffectMap[row.status] || 'light'"
                >
                  {{ statusTextMap[row.status] || row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>

          <!-- 对话框 -->
          <el-dialog
              v-model="orderDialogVisible"
              title="订单详情"
              width= "40%"
              custom-class="order-detail-dialog"
          >
            <el-scrollbar>
              <!-- 订单基本信息 -->
              <el-descriptions :column="1" border>
                <el-descriptions-item label="订单号">{{selectedOrder.orderId}}</el-descriptions-item>
                <el-descriptions-item label="用户">{{username}}</el-descriptions-item>
                <el-descriptions-item label="总金额">
                  <span class="price">¥{{selectedOrder.totalAmount.toFixed(2)}}</span>
                </el-descriptions-item>
                <el-descriptions-item label="支付方式">
                  <el-tag >
                    {{selectedOrder.paymentMethod}}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="下单时间">
                  {{formatDateTime(selectedOrder.createTime)}}
                </el-descriptions-item>
              </el-descriptions>

              <!-- 商品信息 -->
              <div class="product-section">
                <h4>商品详情</h4>
                <el-descriptions :column="1" border>
                  <el-descriptions-item label="名称">{{selectedCartItem.title}}</el-descriptions-item>
                  <el-descriptions-item label="单价">
                    <span class="price">¥{{selectedCartItem.price.toFixed(2)}}</span>
                  </el-descriptions-item>
                  <el-descriptions-item label="描述">{{selectedCartItem.description}}</el-descriptions-item>
                  <el-descriptions-item label="简介">{{selectedCartItem.detail}}</el-descriptions-item>
                  <el-descriptions-item label="购买数量">
                    {{selectedCartItem.quantity}}
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </el-scrollbar>

            <template #footer>
              <el-button type="danger" @click="orderDialogVisible = false">关闭</el-button>
            </template>
          </el-dialog>
        </el-col>

      </el-row>
    </div>
  </el-main>

</template>

<style scoped>
.product-info {
  display: flex;
  align-items: center;
  padding: 8px 0;
}

.product-image {
  width: 100px;
  height: 80px;
  object-fit: cover;
  margin-right: 12px;
  border-radius: 4px;
}


.title {
  font-weight: 500;
  margin-bottom: 4px;
}



/* 悬停效果 */
:deep(.el-table__body tr:hover>td) {
  background-color:  rgba(255, 228, 196, 0.5) !important;
  cursor: pointer;
}



.category-header {
  padding: 15px 20px;
  color: #333;
  border-bottom: 1px solid #ebeef5;
}

.category-menu {
  border-right: none;
}

.category-item {
  display: flex;
  justify-content: space-between;
  height: 48px;
  line-height: 48px;
  padding: 0 20px !important;
}

.category-count {
  color: #999;
  font-size: 0.9em;
}




</style>
