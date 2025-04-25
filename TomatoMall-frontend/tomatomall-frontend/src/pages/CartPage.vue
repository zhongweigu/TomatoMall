<script setup >
import NavigationBar from "@/components/NavigationBar.vue";
import { onMounted, ref, reactive } from "vue";
import { getAllCarts, deleteCart, updateCart, checkout } from "@/api/carts.js";
import { pay } from "@/api/order.js";
import { getProductById } from "@/api/products.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { parseTime } from "@/utils/index";
// 需要添加 getProductStock 导入
import { getProductStock } from "@/api/products.js";
import {userInfo} from "@/api/accounts.js";

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

// 购物车列表和加载状态
const cartItems = ref([]);
const loading = ref(false);
const totalAmount = ref(0);
const selectedItems = ref([]);

// 订单相关状态
const orderDialogVisible = ref(false);
const orderConfirmDialogVisible = ref(false);
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

// 获取购物车列表（正常）
const fetchCarts = async () => {
  loading.value = true;
  try {
    const response = await getAllCarts();
    if (response.data.code === "200") {
      cartItems.value = response.data.data;
      calculateTotal();
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

// 计算总金额
const calculateTotal = () => {
  totalAmount.value = selectedItems.value
      .reduce((sum, item) => {
        // 直接使用选中项的 price 和 quantity
        return sum + (item.price * item.quantity);
      }, 0)
      .toFixed(2);
};

// 商品数量变化处理
// 修改 handleQuantityChange 方法
const handleQuantityChange = async (row) => {
  if (row.quantity < 1) return;
  try {
    // 先获取商品库存信息
    const stockResponse = await getProductStock(row.productId);
    if (stockResponse.data.code === "200") {
      const stockInfo = stockResponse.data.data;
      const availableStock = stockInfo.amount - stockInfo.frozen;

      // 检查是否超过库存
      if (row.quantity > availableStock) {
        ElMessage.warning(`库存不足，当前库存仅剩 ${availableStock} 件`);
        // 将数量设置为最大可用库存
        row.quantity = availableStock;
      }

      // 如果符合库存条件，继续更新购物车
      const response = await updateCart({
        cartItemId: row.cartItemId,
        quantity: row.quantity
      }, sessionStorage.getItem("token"));

      if (response.data.code === "200") {
        calculateTotal();
        ElMessage.success("数量更新成功");
      } else {
        ElMessage.error(response.data.msg || "更新失败");
      }
    } else {
      ElMessage.error("获取库存信息失败");
    }
  } catch (error) {
    console.error("更新数量出错:", error);
    ElMessage.error("网络错误，请重试");
  }
};

// 删除商品
const handleDelete = async (item) => {
  try {
    await ElMessageBox.confirm(`确定要从购物车中移除 "${item.title}" 吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    });

    const response = await deleteCart(item.cartItemId);
    if (response.data.code === "200") {
      // 从选中项中移除
      selectedItems.value = selectedItems.value.filter(id => id !== item.cartItemId);
      // 从购物车列表中移除
      cartItems.value = cartItems.value.filter(cartItem => cartItem.cartItemId !== item.cartItemId);
      calculateTotal();
      ElMessage.success("商品已移除");
    } else {
      ElMessage.error(response.data.msg || "删除失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除商品出错:", error);
      ElMessage.error("网络错误，请重试");
    }
  }
};

// 选择项变化
const handleSelectionChange = (selectedIds) => {
  selectedItems.value = selectedIds;
  calculateTotal();
};

// 显示订单确认对话框
const handleCheckout = () => {
  if (selectedItems.value.length === 0) {
    ElMessage.warning("请至少选择一件商品");
    return;
  }
  orderDialogVisible.value = true;
};

// 提交订单
const submitOrder = async () => {
  if (!orderInfo.shipping.name || !orderInfo.shipping.telephone ||
      !orderInfo.shipping.address || !orderInfo.shipping.zipcode) {
    ElMessage.warning("请完整填写收货信息");
    return;
  }


  try {
    const checkoutData = {
      cartItemIds: selectedItems.value.flatMap(item =>
          Array.from({ length: item.quantity }, () => item.cartItemId)
      ),
      shipping_address: orderInfo.shipping,
      paymentMethod: orderInfo.paymentMethod
    };

    console.log(checkoutData);

    const response = await checkout(checkoutData);
    if (response.data.code === "200") {
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

// 支付订单
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

  // 刷新购物车
  setTimeout(() => {
    fetchCarts();
  }, 1500);
};

onMounted(() => {
  fetchCarts();
});
</script>

<template>
  <NavigationBar></NavigationBar>
  <div class="cart-container">
    <el-card class="cart-card">
      <template #header>
        <div class="card-header">
          <span class="title">我的购物车</span>
        </div>
      </template>

      <el-table
          v-loading="loading"
          :data="cartItems"
          stripe
          @selection-change="handleSelectionChange"
          style="width: 100%"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="商品封面" width="120">
          <template #default="{ row }">
            <el-image
                style="width: 80px; height: 100px"
                :src="row.cover || 'https://tomato-mall-milvelas.oss-cn-nanjing.aliyuncs.com/BookCover/defaultCover.png'"
                fit="cover"
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="商品名称" min-width="180"></el-table-column>
        <el-table-column prop="price" label="单价" width="100">
          <template #default="{ row }">
            <span class="price">¥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数量" width="150">
          <template #default="{ row }">
            <el-input-number
                v-model="row.quantity"
                :min="1"
                size="small"
                @change="(value) => handleQuantityChange(row)"
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="小计" width="120">
          <template #default="{ row }">
            <span class="subtotal">¥{{ (row.price * row.quantity).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="cart-footer">
        <div class="total-info">
          已选择 <span class="count">{{ selectedItems.length }}</span> 件商品
          <span class="total">合计: ¥{{ totalAmount }}</span>
        </div>
        <el-button type="danger" size="large" @click="handleCheckout" :disabled="selectedItems.length === 0">
          结算
        </el-button>
      </div>
    </el-card>
  </div>

  <!-- 订单确认对话框 -->
  <el-dialog v-model="orderDialogVisible" title="订单确认" width="50%">
    <div class="order-content">
      <h3>已选商品</h3>
      <el-table :data="selectedItems" border>
        <el-table-column label="商品封面" width="80">
          <template #default="{ row }">
            <el-image
                style="width: 60px; height: 80px"
                :src="row.cover || 'https://tomato-mall-milvelas.oss-cn-nanjing.aliyuncs.com/BookCover/defaultCover.png'"
                fit="cover"
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="商品名称" min-width="150"></el-table-column>
        <el-table-column prop="price" label="单价" width="80"></el-table-column>
        <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
        <el-table-column label="小计" width="100">
          <template #default="{ row }">
            ¥{{ (row.price * row.quantity).toFixed(2) }}
          </template>
        </el-table-column>
      </el-table>

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
        <span class="total-amount">订单总金额：¥{{ totalAmount }}</span>
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
            <p><strong>订单号：</strong> {{ currentOrder.orderId }}</p>
            <p><strong>创建时间：</strong> {{ parseTime(currentOrder.createTime) }}</p>
            <p><strong>订单金额：</strong> ¥{{ currentOrder.totalAmount }}</p>
          </div>
          <el-button type="danger" @click="handlePay">确认支付</el-button>
          <el-button @click="orderConfirmDialogVisible = false">取消</el-button>
        </template>
      </el-result>
    </div>
  </el-dialog>
</template>

<style scoped>
.cart-container {
  margin-top: 80px;
  padding: 20px;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
}

.cart-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.cart-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.total-info {
  margin-right: 20px;
  font-size: 16px;
}

.count {
  color: #f56c6c;
  font-weight: bold;
  margin: 0 5px;
}

.total {
  margin-left: 20px;
  font-size: 18px;
  color: #f56c6c;
  font-weight: bold;
}

.price {
  color: #f56c6c;
}

.subtotal {
  font-weight: bold;
  color: #f56c6c;
}

.order-content {
  padding: 10px 0;
}

.shipping-info, .payment-method {
  margin-top: 20px;
}

.order-summary {
  margin-top: 20px;
  text-align: right;
}

.total-amount {
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
}

.order-details {
  text-align: left;
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 5px;
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
