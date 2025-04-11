<template>

<el-container class="main-frame">

<el-main class="login" >
    <el-card  style="max-width: 480px ;max-height: 340px;flex: auto;margin-right: auto" >
      <h1>番茄书城</h1>
        <h1>登录</h1>
      <hr>
      <br>
        <el-form style="width: 480px;height: 180px;">
            <el-form-item label="Username" label-width="100px">
                <el-input v-model="username"
                placeholder="请输入用户名"
                style="width: 300px"
                >
                </el-input>
            </el-form-item>
          <el-form-item label="Password" label-width="100px">
            <el-input v-model="password"
            type="password"
            style="width: 300px"
            placeholder="请输入密码">
              </el-input>
          </el-form-item>
          <el-form-item >
            <span class="center-button">
              <el-button @click="handleLogin" type="success" round size="large">登录</el-button>
              <router-link to="/register" v-slot="{navigate}">
                <el-button @click="navigate" type="success" round size="large">注册</el-button>
              </router-link>
              </span>
          </el-form-item>


        </el-form>



    </el-card>

</el-main>

</el-container>


</template>
<style scoped>
.center-button  {
  padding-top: 10px;
  padding-left: 130px;
  display: flex;
  flex-direction: row;
  gap: 15px;
  align-items: center;
  justify-content: right;
}
.main-frame{
  min-height: 100vh;
  width: 100vw;

  /* 内容居中 */
  display: flex;
  justify-content: center;
  align-items: center;
}
.login{
  padding-left: 1000px;
}
</style>

<script setup  >
import {ElForm, ElFormItem, ElButton, ElMessage} from "element-plus"
import {ElCard,ElMain,ElInput,ElContainer} from 'element-plus'
import {ref, reactive, computed} from "vue"
import {router} from "../router/index.js"
import axios from "axios"
import {userInfo, userLogin} from "@/api/accounts.js";

// 输入框值（需要在前端拦截不合法输入：是否为空+额外规则）
const username = ref('')
const password = ref('')

// 登录按钮触发
function handleLogin() {
  console.log('login')
  userLogin({
    username: username.value,
    password: password.value
  }).then(res => {
    if (res.data.code === '200') {
      ElMessage({
        message: "登录成功！",
        type: 'success',
        center: true,
      })
      const token = res.data.data;
      sessionStorage.setItem('token', token)
      console.log(token);

      userInfo(username.value).then(res => {
        console.log(res);
        sessionStorage.setItem('username', res.data.data.username)
        sessionStorage.setItem('role', res.data.data.role)
        sessionStorage.setItem('storeId', res.data.data.storeId)
        router.push({path:"/personal"});
      })
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
      password.value = ''
    }
  })
}

function handleRegister(){
  router.push({path:"/register"})
}



</script>

