<script setup>
import {ElButton, ElCard, ElForm, ElFormItem, ElInput, ElMain,ElContainer,ElHeader,ElDialog} from "element-plus";
import {reactive, ref} from "vue"
import { ElMessage } from 'element-plus'
import {userInfo, userInfoUpdate} from "@/api/accounts.js";
import NavigationBar from "@/components/NavigationBar.vue";
import {
  Edit,
} from '@element-plus/icons-vue'
import {uploadImage} from "@/api/image.js";
import {router} from "@/router/index.js";


const username = sessionStorage.getItem("username");
const email = ref('')
const name = ref('')
const identity = sessionStorage.getItem("role");
const telephone = ref('')
const storeId = ref();
const location = ref('')
const avatar = ref('');


function getUserInfo() {
  userInfo(username).then(res => {
    console.log(res)
    name.value = res.data.data.name
    telephone.value = res.data.data.telephone
    storeId.value = res.data.data.storeId
    location.value = res.data.data.location
    email.value = res.data.data.email
    avatar.value = res.data.data.avatar
  })
}

getUserInfo()


// 昵称修改相关
const nicknameDialogVisible = ref(false)
const newNickname = ref('')

const LocationDialogVisible = ref(false)
const newLocation = ref('')

const EmailDialogVisible = ref(false)
const newEmail = ref('')

const TelephoneDialogVisible = ref(false)
const newTelephone = ref('')

// 密码修改相关
const passwordDialogVisible = ref(false)
const passwordForm = reactive({
  password: '',
  confirmPassword: ''
})
const isUpdating = ref(false)

// 头像修改相关
const avatarDialogVisible = ref(false)

// 显示对话框方法
const showNicknameDialog = () => {
  nicknameDialogVisible.value = true
}

const showLocationDialog = () => {
  LocationDialogVisible.value = true
}

const showEmailDialog = () => {
  EmailDialogVisible.value = true
}

const showTelephoneDialog = () => {
  TelephoneDialogVisible.value = true
}

const showPasswordDialog = () => {
  passwordForm.password = ''
  passwordForm.confirmPassword = ''
  passwordDialogVisible.value = true
}

// 提交修改方法
const updateNickname =  async(newNickname) => {
  try {
    // 调用更新接口，传递新昵称和 Token
    const response = await userInfoUpdate({
      username: username, // 确保 username 是响应式变量（如 ref/reactive）
      name: newNickname        // 新昵称
    }, sessionStorage.getItem("token"));

    // 根据响应结果处理
    if (response.data.code === "200") {
      ElMessage.success('昵称修改成功');
      name.value = newNickname; // 更新本地昵称显示
    } else {
      ElMessage.error(response.data.message || "修改失败");
    }
  } catch (error) {
    console.error("昵称修改异常:", error);
    ElMessage.error("网络错误，请重试");
  } finally {
    nicknameDialogVisible.value = false; // 关闭弹窗
  }
}

const updateLocation =  async(newLocation) => {
  try {
    const response = await userInfoUpdate({
      username: username,
      location: newLocation
    }, sessionStorage.getItem("token"));

    if (response.data.code === "200") {
      ElMessage.success('地址修改成功');
      location.value = newLocation; // 更新本地显示
    } else {
      ElMessage.error(response.data.message || "修改失败");
    }
  } catch (error) {
    console.error("地址修改异常:", error);
    ElMessage.error("网络错误，请重试");
  } finally {
    LocationDialogVisible.value = false;
  }
}

const updateEmail =  async(newEmail) => {
  try {
    const response = await userInfoUpdate({
      username: username,
      email: newEmail
    }, sessionStorage.getItem("token"));

    if (response.data.code === "200") {
      ElMessage.success('邮箱修改成功');
      email.value = newEmail; // 更新本地显示
    } else {
      ElMessage.error(response.data.message || "修改失败");
    }
  } catch (error) {
    console.error("邮箱修改异常:", error);
    ElMessage.error("网络错误，请重试");
  } finally {
    EmailDialogVisible.value = false;
  }
}

const updateTelephone =  async(newTelephone) => {
  try {
    const response = await userInfoUpdate({
      username: username,
      telephone: newTelephone
    }, sessionStorage.getItem("token"));

    if (response.data.code === "200") {
      ElMessage.success('号码修改成功');
      telephone.value = newTelephone; // 更新本地显示
    } else {
      ElMessage.error(response.data.message || "修改失败");
    }
  } catch (error) {
    console.error("号码修改异常:", error);
    ElMessage.error("网络错误，请重试");
  } finally {
    TelephoneDialogVisible.value = false;
  }
}

const updatePassword = async(newPassword) => {
  try {
    // 调用更新接口，传递新昵称和 Token
    const response = await userInfoUpdate({
      username: username, // 确保 username 是响应式变量（如 ref/reactive）
      password: newPassword        // 新昵称
    }, sessionStorage.getItem("token"));

    // 根据响应结果处理
    if (response.data.code === "200") {
      ElMessage.success('密码修改成功');
    } else {
      ElMessage.error(response.data.message || "修改失败");
    }
  } catch (error) {
    console.error("密码修改异常:", error);
    ElMessage.error("网络错误，请重试");
  } finally {
    passwordDialogVisible.value = false; // 关闭弹窗
  }
}

const updateAvatar = async(options) => {
  const { file } = options;
  try{
    const response = await uploadImage(
        file,
        "UserAvatar",
        sessionStorage.getItem("token")
    );
    if (response.data.code === "200") {
      const newAvatarUrl = response.data.data;
      ElMessage.success('头像上传成功');
      try{
        const res = await userInfoUpdate({
          username: username,
          avatar: newAvatarUrl,
        }, sessionStorage.getItem("token"));
        if (res.data.code === "200") {
          avatar.value = newAvatarUrl;
          console.log(res);
          console.log(newAvatarUrl);
          console.log(avatar.value);
          ElMessage.success('头像修改成功');
        } else {
          ElMessage.error(res.data.message || "修改失败");
        }
      }catch(err){
        ElMessage.error("头像修改异常:", err.message);
      }
    } else {
      ElMessage.error(response.data.message || "上传失败");
    }
  }catch(err){
    console.error("头像上传异常:", err);
  }finally {
    avatarDialogVisible.value = false;
  }
}

const handleLogout = () => {
  sessionStorage.removeItem("token");
  router.push("/login");
}

</script>

<template>
  <NavigationBar></NavigationBar>
  <el-container class="container">

    <el-main class="main">

      <!-- 个人信息卡片 -->
      <el-card
          class="person-card animated-card"
          :body-style="{ padding: '30px' }"
      >
        <div class="profile-header">
          <!-- 头像上传 -->
          <div class="avatar-wrapper">
            <el-upload
                class="avatar-uploader"
                :http-request="updateAvatar"
                :show-file-list="false"
                :auto-upload="true"
            >
              <img  v-if="avatar" :src="avatar" class="avatar"   alt="avatar"/>
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              <div class="avatar-hover">
                <el-icon :size="24" color="#fff"><Edit /></el-icon>
              </div>
            </el-upload>
          </div>

          <div class="profile-main">
            <h1 class="title-gradient">{{ name }}</h1>
            <div class="identity-tag">
              <span v-if="identity === 'staff'">管理员</span>
              <span v-else-if="identity === 'customer'">顾客</span>
            </div>
          </div>
        </div>

      </el-card>

      <!-- 账户详情卡片 -->
      <el-card
          class="detail-card animated-card"
          :body-style="{ padding: '30px' }"
      >
        <div class="card-header">
          <h1 class="title-gradient">账户详情</h1>
          <el-divider class="custom-divider"></el-divider>
        </div>
        <div class="info-group">

          <div class="info-item">
            <span class="info-label">👤 用户名</span>
            <span class="info-value">{{ username }}</span>
          </div>
          <div class="info-item editable">
            <div>
              <span class="info-label">✨ 昵称</span>
              <span class="info-value">{{ name }}</span>
            </div>
            <el-button type="danger" :icon="Edit" circle @click="showNicknameDialog"/>
          </div>
          <div class="info-item editable">
            <div>
              <span class="info-label">🔒 密码</span>
              <span class="info-value">••••••••••</span>
            </div>
            <el-button type="danger" :icon="Edit" circle @click="showPasswordDialog"/>
          </div>

          <div class="info-item editable">
            <div>
              <span class="info-label">🏠 地址</span>
              <span class="info-value">{{ location || "未设置"}}</span>
            </div>
            <el-button type="danger" :icon="Edit" circle @click="showLocationDialog"/>
          </div>
          <div class="info-item editable">
            <div>
              <span class="info-label">📱 注册手机</span>
              <span class="info-value">{{ telephone }}</span>
            </div>
            <el-button type="danger" :icon="Edit" circle @click="showTelephoneDialog"/>
          </div>
          <div class="info-item editable">
            <div>
              <span class="info-label">📧 电子邮箱</span>
              <span class="info-value">{{ email || "未设置"}}</span>
            </div>
            <el-button type="danger" :icon="Edit" circle @click="showEmailDialog"/>
          </div>
        </div>
        <el-divider />
        <div><el-button type="danger" round @click="handleLogout">退出登录</el-button></div>
      </el-card>

      <el-dialog
          v-model="nicknameDialogVisible"
          title="修改昵称"
          width="30%"
      >
        <el-form>
          <el-form-item label="新昵称">
            <el-input v-model="newNickname" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button type="danger" @click="nicknameDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="updateNickname(newNickname)">确认修改</el-button>
        </template>
      </el-dialog>


      <!-- 修改密码对话框 -->
      <el-dialog
          v-model="passwordDialogVisible"
          title="修改密码"
          width="30%"
      >
        <el-form :model="passwordForm" >
          <el-form-item label="新密码" >
            <el-input
                v-model="passwordForm.password"
                type="password"
                show-password
            />
          </el-form-item>
          <el-form-item label="确认密码" >
            <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                show-password
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button type="danger" @click="passwordDialogVisible = false">取消</el-button>
          <el-button
              type="danger"
              @click="updatePassword(passwordForm.password)"
              :loading="isUpdating"
          >
            确认修改
          </el-button>
        </template>
      </el-dialog>

      <el-dialog
          v-model="LocationDialogVisible"
          title="修改地址"
          width="30%"
      >
        <el-form>
          <el-form-item label="新地址">
            <el-input v-model="newLocation" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button type="danger" @click="LocationDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="updateLocation(newLocation)">确认修改</el-button>
        </template>
      </el-dialog>

      <el-dialog
          v-model="EmailDialogVisible"
          title="修改邮箱"
          width="30%"
      >
        <el-form>
          <el-form-item label="新邮箱">
            <el-input v-model="newEmail" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button type="danger" @click="EmailDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="updateEmail(newEmail)">确认修改</el-button>
        </template>
      </el-dialog>

      <el-dialog
          v-model="TelephoneDialogVisible"
          title="修改号码"
          width="30%"
      >
        <el-form>
          <el-form-item label="新号码">
            <el-input v-model="newTelephone" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button type="danger" @click="TelephoneDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="updateTelephone(newTelephone)">确认修改</el-button>
        </template>
      </el-dialog>

    </el-main>

  </el-container>





</template>

<style scoped>
.container {
  height: 100vh;
  width: 100%;
  overflow-x: hidden;
  overflow-y: hidden;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 2rem;
  padding: 2rem;
  margin-top: 30px;
}

.main {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding: 20px;
}

.person-card {
  width: 400px; /* 固定宽度 */
  height: 200px;
  flex-shrink: 0; /* 禁止缩小 */
  background-color: rgba(228, 203, 174, 0.1);
}

/* 自动填充剩余空间 */
.detail-card {
  flex: 1; /* 关键：自动拉伸填充剩余宽度 */
  min-width: 0; /* 修复 flex 容器溢出问题 */
  background-color: rgba(228, 203, 174, 0.1);
}

.animated-card {
  transition: all 0.3s ease;
  border-radius: 15px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.animated-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.card-header {
  text-align: center;
  margin-bottom: 1.5rem;
}

.title-gradient {
  font-size: 1.8rem;
  background: linear-gradient(45deg, #409EFF, #67C23A);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.custom-divider {
  margin: 1rem 0;
}

.info-group {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
  transition: background 0.3s ease;
}

.info-item:hover {
  background: #f1f3f5;
}

.info-label {
  font-weight: 500;
  color: #606266;
  margin-right: 1rem;
}

.info-value {
  color: #ca757e;
  font-weight: 600;
}

.editable {
  background: rgba(64, 158, 255, 0.05);
  border: 1px solid rgba(64, 158, 255, 0.1);
}

.editable:hover {
  background: rgba(64, 158, 255, 0.08);
}

@media (max-width: 768px) {
  .container {
    grid-template-columns: 1fr;
    padding: 1rem;
  }

  .title-gradient {
    font-size: 1.5rem;
  }

  .info-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 2rem;
  margin-bottom: 1.5rem;
}

.avatar-wrapper {
  position: relative;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.avatar-wrapper:hover {
  transform: scale(1.05);
}

.avatar-uploader {
  width: 120px;
  height: 120px;
  display: block;
  cursor: pointer;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.avatar-hover {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-wrapper:hover .avatar-hover {
  opacity: 1;
}

.profile-main {
  flex: 1;
}

.identity-tag {
  background: linear-gradient(45deg, #67C23A, #409EFF);
  color: white;
  padding: 6px 15px;
  border-radius: 20px;
  display: inline-block;
  margin-top: 10px;
  font-size: 0.9em;
}

</style>