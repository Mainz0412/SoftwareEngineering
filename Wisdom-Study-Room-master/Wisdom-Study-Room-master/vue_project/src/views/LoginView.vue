<template>
  <div id='building'>
    <img src="../assets/logo.png" class="logo" alt="logo"> <!-- 添加徽标 -->
    <div class="login-container">
      <h2>用户登录</h2>
      <el-form :model="form" :rules="rules" ref="loginForm" label-width="80px" class="login-form">
        <el-form-item label="账号" prop="useraccount">
          <el-input v-model="form.useraccount" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" placeholder="请输入密码" clearable show-password></el-input>
        </el-form-item>
        <el-form-item>
          <div class="button-group">
            <el-button type="primary" @click="login">登录</el-button>
            <el-button type="text" @click="goToRegister">注册</el-button>
            <el-button type="text" @click="changePassword">修改密码</el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data () {
    return {
      form: {
        useraccount: '',
        password: ''
      },
      rules: {
        useraccount: [
          {
            required: true,
            message: '请输入账号',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          }
        ]
      },
    }
  },
  mounted() {
    document.title = this.$route.meta.title;
  },
  methods: {
    login () {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          const url = `user/list?userAccount=${this.form.useraccount}`
          axios.get(url).then(response => {
            console.log(response.data)
              const violationData = response.data;
              const vio = violationData[0]
              if (vio.userIllegalState === 1) {
                this.$message.error(`用户违规,直到${vio.userIllegalDate}`)
              }
              else{
                axios.post("user/login",{
                  userAccount: this.form.useraccount,
                  userPassword: this.form.password
                }).then(res =>{
                  if(res.data.code === 200 && res.data.token.toString() === '1')
                  {
                    this.$message.success('登录成功！')
                    sessionStorage.setItem('userAccount',this.form.useraccount)
                    this.$router.push('/Admin')
                  }
                  else if(res.data.code === 200 && res.data.token.toString() === '0')
                  {
                    this.$message.success('登录成功！')
                    sessionStorage.setItem('userAccount',this.form.useraccount)
                    this.$router.push('/HomeView')
                  }
                  else if(res.data.code === 500){
                    this.$message.error('账号或密码错误')
                  }
                })
              }
          }).catch(error => {
            this.$message.error("未查询到该用户")
            this.form.useraccount = ''
            this.form.password = ''
          });
        }
      })
    },
    goToRegister () {
      this.$router.push('/RegisterView')
    },
    changePassword () {
      this.$router.push('/ChangePassword')
    }
  }
}
</script>

<style scoped>
/* 全局样式中设置背景图片 */
#building {
  background: url("../assets/backgroud.jpg");
  width: 100%;
  height: 100%;
  position: fixed;
  background-size: 100% 100%;
}

.login-container {
    max-width: 350px;
    margin: 100px auto;
    padding: 20px;
    border-radius: 5px;
    background-color: #f9f9f9;
}

.login-form {
  margin-bottom: 20px;
}

.error-message {
  color: red;
}

.button-group {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.logo {
  width: 100px;
  height: auto;
  display: block;
  margin: 15px auto 20px;
  /* 调整徽标位置 */
}
</style>
