<template>
  <div class="product-management">
    <NavigationBar v-if="router.currentRoute.value.meta.showNavbar"></NavigationBar>

    <el-container class="container">
      <el-header style="display: flex; justify-content: space-between; align-items: center;">
        <h2>商品管理</h2>
        <el-button type="primary" @click="handleAddProduct">添加商品</el-button>
      </el-header>

      <el-main>
        <!-- 商品列表 -->
        <el-table :data="products" style="width: 100%" v-loading="loading">
          <el-table-column prop="id" label="ID" width="70" />
          <el-table-column label="封面" width="100">
            <template #default="scope">
              <el-image
                style="width: 60px; height: 80px"
                :src="scope.row.cover"
                fit="cover"
                :preview-src-list="[scope.row.cover]">
                <template #error>
                  <div class="image-slot">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
            </template>
          </el-table-column>
          <el-table-column prop="title" label="商品名称" min-width="150" />
          <el-table-column prop="price" label="价格" width="90" />
          <el-table-column prop="rate" label="评分" width="90" />
          <el-table-column label="库存" width="120">
            <template #default="scope">
              <el-button link type="primary" @click="handleViewStock(scope.row)">
                查看库存
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" @click="handleEditProduct(scope.row)">编辑</el-button>
              <el-button
                size="small"
                type="danger"
                @click="handleDeleteProduct(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>

    <!-- 添加/编辑商品对话框 -->
    <el-dialog
      v-model="productDialogVisible"
      :title="isEditing ? '编辑商品' : '添加商品'"
      width="60%"
    >
      <el-form :model="productForm" label-width="120px">
        <el-form-item label="商品名称" required>
          <el-input v-model="productForm.title" />
        </el-form-item>
        <el-form-item label="价格" required>
          <el-input-number v-model="productForm.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="评分" required>
          <el-rate v-model="productForm.rate" :max="10" :allow-half="true" />
        </el-form-item>

        <!-- 替换为图片上传组件 -->
        <el-form-item label="商品封面">
          <el-upload
            class="cover-uploader"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :http-request="handleUpload"
          >
            <img v-if="productForm.cover" :src="productForm.cover" class="cover-image" />
            <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <div v-if="uploadLoading" style="margin-top: 10px;">
            <el-progress :percentage="uploadProgress"></el-progress>
          </div>
        </el-form-item>

        <el-form-item label="商品描述">
          <el-input v-model="productForm.description" type="textarea" />
        </el-form-item>
        <el-form-item label="详细说明">
          <el-input v-model="productForm.detail" type="textarea" :rows="3" />
        </el-form-item>

        <!-- 规格部分 -->
        <el-divider>商品规格</el-divider>
        <div v-for="(spec, index) in productForm.specifications" :key="index" style="margin-bottom: 10px; display: flex; align-items: center;">
          <el-form-item :label="`规格${index + 1}`" :prop="`specifications[${index}].item`" style="margin-right: 10px; width: 30%;">
            <el-input v-model="spec.item" placeholder="规格名称" />
          </el-form-item>
          <el-form-item :prop="`specifications[${index}].value`" style="margin-right: 10px; width: 60%;">
            <el-input v-model="spec.value" placeholder="规格内容" />
          </el-form-item>
          <el-button type="danger" circle @click="removeSpecification(index)">
            <el-icon><Delete /></el-icon>
          </el-button>
        </div>
        <el-button type="primary" @click="addSpecification" plain>添加规格</el-button>
      </el-form>
      <template #footer>
        <span>
          <el-button @click="productDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitProductForm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 库存管理对话框 -->
    <el-dialog
      v-model="stockDialogVisible"
      title="库存管理"
      width="30%"
    >
      <div v-if="currentStock">
        <h3>{{ currentProduct?.title }}</h3>
        <p>当前可售库存: {{ currentStock.amount }}</p>
        <p>冻结库存: {{ currentStock.frozen }}</p>

        <el-divider />

        <el-form>
          <el-form-item label="调整库存数量">
            <el-input-number v-model="newStockAmount" :min="0" />
          </el-form-item>
        </el-form>
      </div>
      <div v-else>
        <el-empty description="获取库存信息中..." />
      </div>
      <template #footer>
        <span>
          <el-button @click="stockDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="updateStock">确认修改</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Picture, Plus } from '@element-plus/icons-vue'
import NavigationBar from '@/components/NavigationBar.vue'
import { router } from '@/router/index.js'
import {
  getAllProducts,
  getProductById,
  addProduct,
  updateProduct,
  deleteProduct,
  getProductStock,
  updateProductStock
} from '@/api/products'
import { uploadProductImage } from '@/api/image'

// 假数据
import { mockProducts, mockStockData } from '@/mock/productData'

// 状态管理
const loading = ref(false)
const uploadLoading = ref(false)
const uploadProgress = ref(0)
const products = ref([])
const productDialogVisible = ref(false)
const stockDialogVisible = ref(false)
const isEditing = ref(false)
const currentProduct = ref(null)
const currentStock = ref(null)
const newStockAmount = ref(0)

// 商品表单
const productForm = reactive({
  id: '',
  title: '',
  price: 0,
  rate: 0,
  description: '',
  cover: '',
  detail: '',
  specifications: []
})

// 生命周期钩子 - 组件挂载后获取商品列表
onMounted(() => {
  fetchProducts()
})

// 获取所有商品
const fetchProducts = async () => {
  loading.value = true
  try {

    // 使用假数据时,可以注释掉
    const response = await getAllProducts()
    if (response.data.code === 200) {
      products.value = response.data.data
    } else {
      ElMessage.error(response.data.msg || '获取商品列表失败')
    }

    /*
    // 使用模拟数据
    setTimeout(() => {
      products.value = mockProducts
      loading.value = false
    }, 500)
    */

  } catch (error) {
    console.error('获取商品列表出错:', error)
    ElMessage.error('网络错误，请重试')
  } finally {
    loading.value = false
  }
}

// 图片上传前的验证
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 自定义上传方法
const handleUpload = async (options) => {
  const { file } = options
  uploadLoading.value = true
  uploadProgress.value = 0

  // 模拟上传进度
  const progressInterval = setInterval(() => {
    if (uploadProgress.value < 90) {
      uploadProgress.value += 10
    }
  }, 300)

  try {
    const response = await uploadProductImage(file)
    clearInterval(progressInterval)
    uploadProgress.value = 100

    if (response.data.code === 200) {
      productForm.cover = response.data.data
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error(response.data.msg || '图片上传失败')
    }
  } catch (error) {
    clearInterval(progressInterval)
    console.error('上传图片出错:', error)
    ElMessage.error('网络错误，请重试')
  } finally {
    setTimeout(() => {
      uploadLoading.value = false
    }, 500)
  }
}

// 清空商品表单
const resetProductForm = () => {
  productForm.id = ''
  productForm.title = ''
  productForm.price = 0
  productForm.rate = 0
  productForm.description = ''
  productForm.cover = ''
  productForm.detail = ''
  productForm.specifications = []
}

// 添加商品按钮点击
const handleAddProduct = () => {
  resetProductForm()
  isEditing.value = false
  productDialogVisible.value = true
}

// 编辑商品按钮点击
const handleEditProduct = (product) => {
  isEditing.value = true
  currentProduct.value = product

  // 复制商品数据到表单
  productForm.id = product.id
  productForm.title = product.title
  productForm.price = product.price
  productForm.rate = product.rate
  productForm.description = product.description || ''
  productForm.cover = product.cover || ''
  productForm.detail = product.detail || ''
  productForm.specifications = product.specifications ? [...product.specifications] : []

  productDialogVisible.value = true
}

// 删除商品按钮点击
const handleDeleteProduct = (product) => {
  ElMessageBox.confirm(
    `确定要删除 "${product.title}" 吗？此操作不可恢复。`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      const response = await deleteProduct(product.id)
      if (response.data.code === 200) {
        ElMessage.success('删除成功')
        fetchProducts() // 重新获取商品列表
      } else {
        ElMessage.error(response.data.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除商品出错:', error)
      ElMessage.error('网络错误，请重试')
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 查看库存按钮点击
const handleViewStock = async (product) => {
  currentProduct.value = product
  currentStock.value = null
  newStockAmount.value = 0
  stockDialogVisible.value = true

  try {
    // 使用假数据时,可以注释掉
    const response = await getProductStock(product.id)
    if (response.data.code === 200) {
      currentStock.value = response.data.data
      newStockAmount.value = currentStock.value.amount
    } else {
      ElMessage.error(response.data.msg || '获取库存信息失败')
    }

/*
    // 使用模拟数据
    setTimeout(() => {
      currentStock.value = mockStockData[product.id]
      newStockAmount.value = currentStock.value.amount
    }, 300)
*/

  } catch (error) {
    console.error('获取库存信息出错:', error)
    ElMessage.error('网络错误，请重试')
  }
}

// 添加规格
const addSpecification = () => {
  productForm.specifications.push({
    item: '',
    value: '',
    productId: productForm.id
  })
}

// 移除规格
const removeSpecification = (index) => {
  productForm.specifications.splice(index, 1)
}

// 提交商品表单
const submitProductForm = async () => {
  if (!productForm.title || productForm.price < 0 || productForm.rate < 0 || productForm.rate > 10) {
    ElMessage.warning('请填写必要的商品信息，并确保价格和评分有效')
    return
  }

  try {
    let response
    if (isEditing.value) {
      // 更新商品
      response = await updateProduct(productForm)
    } else {
      // 添加新商品
      response = await addProduct(productForm)
    }

    if (response.data.code === 200) {
      ElMessage.success(isEditing.value ? '商品更新成功' : '商品添加成功')
      productDialogVisible.value = false
      fetchProducts() // 刷新商品列表
    } else {
      ElMessage.error(response.data.msg || (isEditing.value ? '更新失败' : '添加失败'))
    }
  } catch (error) {
    console.error('提交商品表单出错:', error)
    ElMessage.error('网络错误，请重试')
  }
}

// 更新库存
const updateStock = async () => {
  if (!currentProduct.value || newStockAmount.value < 0) {
    ElMessage.warning('请输入有效的库存数量')
    return
  }

  try {
    const response = await updateProductStock(currentProduct.value.id, newStockAmount.value)
    if (response.data.code === 200) {
      ElMessage.success('库存更新成功')
      stockDialogVisible.value = false
    } else {
      ElMessage.error(response.data.msg || '库存更新失败')
    }
  } catch (error) {
    console.error('更新库存出错:', error)
    ElMessage.error('网络错误，请重试')
  }
}
</script>

<style scoped>
.product-management {
  /* 为顶部固定导航栏留出空间 */
  padding-top: 60px;
}

.container {
  min-height: calc(100vh - 60px);
  width: 100%;
  padding: 20px;
}

.el-header {
  padding: 20px;
  background-color: #f5f7fa;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
}

.cover-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 200px;
  height: 200px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.cover-uploader:hover {
  border-color: #409EFF;
}

.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 30px;
  height: 30px;
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>