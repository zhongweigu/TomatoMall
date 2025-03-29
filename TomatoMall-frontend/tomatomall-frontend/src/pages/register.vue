<template>
<el-container class="main-frame">

  <el-main class="register">
    <el-card style="max-width: 480px ;max-height: 650px;">
      <h1>番茄书城</h1>

      <h2>注册</h2>
      <hr>
      <br>
      <el-form :model="userRegister" style="width: 480px;height: 380px;">
        <el-form-item label="Name" label-width="100px">
          <el-input v-model="name"
                    placeholder="请输入昵称"
                    style="width: 300px"
          >
          </el-input>
        </el-form-item>


        <el-form-item label="Email" label-width="100px">
          <el-input v-model="email"
                    placeholder="请输入电子邮箱"
                    style="width: 300px"
          >
          </el-input>
        </el-form-item>




        <el-form-item label="Phone" label-width="100px">
          <el-input v-model="telephone"
                    placeholder="请输入手机号码"
                    style="width: 300px"
          >
          </el-input>
        </el-form-item>


        <el-form-item label="role" label-width="100px">
          <el-select v-model="identity" placeholder="选择你要注册的角色" style="width: 240px">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>

        </el-form-item>


        <el-form-item label="Username" label-width="100px">
          <el-input v-model="username"
                    placeholder="请输入用户名"
                    style="width: 300px"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="Password" label-width="100px">
          <el-input v-model="password"
          placeholder="请输入密码"
          style="width: 300px">


          </el-input>
        </el-form-item>
        <el-form-item label="" label-width="100px">
          <el-input v-model="confirmPassword"
                    placeholder="请确认密码"
                    style="width: 300px">


          </el-input>
        </el-form-item>
        <el-form-item >
            <span class="center-button">
              <el-button @click="handleRegister" type="success" round size="large" style="width: 100px ;font-size: 25px">注册</el-button>
              </span>
        </el-form-item>


      </el-form>


    </el-card>




  </el-main>

</el-container>

</template>
<style scoped>
.center-button{
  padding-left: 160px;
}
.main-frame{
  min-height: 100vh;
  width: 100vw;

  background-image:url("../photo/back.png") ;
  background-size: cover;
  background-position: center; /* 图片居中 */
  background-repeat: no-repeat;
  background-attachment: fixed; /* 固定背景（可选） */

  /* 内容居中 */
  display: flex;
  justify-content: center;
  align-items: center;
}
.register{
  padding-left: 1000px;
}
</style>


<script setup >
import {ElButton, ElCard, ElForm, ElFormItem, ElInput, ElMain,ElContainer,ElSelect,ElOption} from "element-plus";
import {computed, reactive, ref} from "vue"
import { ElMessage } from 'element-plus'
import {router} from "../router/index.js"
import {userRegister} from "@/api/accounts.js";

const options =reactive([{
      label:"顾客",
      value:"CUSTOMER"
    },
    {
      label:"卖家",
      value:"STAFF"
    }])

const username = ref('')
const email = ref('')
const name = ref('')
const identity = ref('')
const telephone = ref('')
const password = ref('')
const confirmPassword = ref('')
const storeId = ref();

// 电话号码是否为空
const hasTelInput = computed(() => telephone.value !== '')
// 密码是否为空
const hasPasswordInput = computed(() => password.value !== '')
// 重复密码是否为空
const hasConfirmPasswordInput = computed(() => confirmPassword.value !== '')
// 身份是否为空
const hasIdentityChosen = computed(() => identity.value !== '')
// 对于商家用户，商店Id是否为空
const hasStoreName = computed(() => storeId.value !== undefined)
// 电话号码的规则
const chinaMobileRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/
const telLegal = computed(() => chinaMobileRegex.test(telephone.value))
// 重复密码的规则
const isPasswordIdentical = computed(() => password.value === confirmPassword.value)
// 注册按钮可用性
const registerDisabled = computed(() => {
  if (!hasIdentityChosen.value) {
    return true
  } else if (identity.value === 'CUSTOMER') {
    return !(hasTelInput.value && hasPasswordInput.value && hasConfirmPasswordInput  &&
        telLegal.value && isPasswordIdentical.value)
  } else if (identity.value === 'STAFF') {
    return !(hasTelInput.value && hasPasswordInput.value && hasConfirmPasswordInput  &&
        hasStoreName.value && telLegal.value && isPasswordIdentical.value)
  }
})

function handleRegister() {
  userRegister({
    username: username.value,
    role: identity.value,
    name: name.value,
    telephone: telephone.value,
    password: password.value,
    email: email.value,
    storeId: storeId.value
  }).then(res => {
    if (res.data.code === '000') {  //类型守卫，它检查 res.data 对象中是否存在名为 code 的属性
      ElMessage({
        message: "注册成功！请登录账号",
        type: 'success',
        center: true,
      })
      router.push('/login');
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}


</script>