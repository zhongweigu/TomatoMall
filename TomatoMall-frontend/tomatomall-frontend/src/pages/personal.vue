<script setup>
import {ElButton, ElCard, ElForm, ElFormItem, ElInput, ElMain,ElContainer,ElHeader,ElDialog} from "element-plus";
import {reactive, ref} from "vue"
import { ElMessage } from 'element-plus'
import axios from "axios"
import {userInfo, userInfoUpdate} from "@/api/accounts.js";
import {router} from "@/router/index.js";
import NavigationBar from "@/components/NavigationBar.vue";

const username = sessionStorage.getItem("username");
const email = ref('')
const name = ref('')
const identity = sessionStorage.getItem("role");
const telephone = ref('')
const storeId = ref();
const location = ref('')
const storeName = ref();

// 昵称修改相关
const nicknameDialogVisible = ref(false)
const newNickname = ref('')

function getUserInfo() {
  userInfo(username).then(res => {
    console.log(res)
    name.value = res.data.data.name
    telephone.value = res.data.data.telephone
    storeId.value = res.data.data.storeId
    location.value = res.data.data.address
  })
}

getUserInfo()

// 密码修改相关
const passwordDialogVisible = ref(false)
const passwordForm = reactive({
  password: '',
  confirmPassword: ''
})
const isUpdating = ref(false)

// 密码验证规则

// 显示对话框方法
const showNicknameDialog = () => {
  nicknameDialogVisible.value = true
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

</script>

<template>
  <NavigationBar v-if="router.currentRoute.value.meta.showNavbar"></NavigationBar>
  <el-container class="container">
    <el-header height="auto">
      个人中心
    </el-header>

    <el-main class="main">

       <el-card class="person-card" style="width: 380px;height: 450px">
         <h1>个人名片</h1>

         <hr>

         <h2>昵称：{{name}}</h2>
         <h2>身份：{{identity}}</h2>

       </el-card>
       <el-card class="detail-card" style="width: 1000px;height: 700px">
         <h1>账户详情信息</h1>
         <hr>
         <h2>注册手机号：{{telephone}}</h2>
         <h2>注册邮箱:{{email}}</h2>
         <h2>用户名：{{username}}</h2>
         <h2>昵称：{{name}}  <el-button type="text" @click="showNicknameDialog">修改</el-button></h2>
         <h2>密码： **********  <el-button type="text" @click="showPasswordDialog">修改</el-button>   </h2>



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
          <el-button @click="nicknameDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="updateNickname(newNickname)">确认修改</el-button>
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
          <el-button @click="passwordDialogVisible = false">取消</el-button>
          <el-button
              type="primary"
              @click="updatePassword(passwordForm.password)"
              :loading="isUpdating"
          >
            确认修改
          </el-button>
        </template>
      </el-dialog>


    </el-main>

  </el-container>





</template>

<style scoped>
.person-card {

  background-color: rgba(228, 203, 174, 0.79);
  margin: 80px;
}
.container{
  min-height: 100vh;
  width: 100vw;

  background-image:url("../photo/background.png") ;
  background-size: cover;
  background-position: center; /* 图片居中 */
  background-repeat: no-repeat;
  background-attachment: fixed; /* 固定背景（可选） */

}
.detail-card {
  margin-top: 80px;
  background-color: rgba(228, 203, 174, 0.79);
}
.main{
  display: flex;
}

</style>