import Vue from 'vue'
import VueRouter from 'vue-router'
import axios from 'axios'
import {MessageBox} from "element-ui";
import ElementUI from 'element-ui';
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Default',
    meta: { title: '智慧自习室' },
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/LoginView',
    name: 'LoginView',
    meta: { title: '登录 - 智慧自习室' },
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/RegisterView',
    name: 'RegisterView',
    meta: { title: '注册 - 智慧自习室' },
    component: () => import('../views/RegisterView.vue')
  },
  {
    path: '/HomeView',
    name: 'HomeView',
    meta: { title: '主界面 - 智慧自习室' },
    component: () => import('../views/HomeView.vue'),
    beforeEnter: (to, from, next) => {
      const getUserAccount = sessionStorage.getItem('userAccount')
      if(getUserAccount === null)
      {
        MessageBox.alert('请先登录', '提示', {
          confirmButtonText: '确定',
          callback: action => {
            next({ name: 'LoginView' })
          }
        })
      }
      else
      {
        const url = `user/list?userAccount=${getUserAccount}`
        axios.get(url).then(response => {
          if(response.data != null)
          {
            const violationData = response.data;
            const vio = violationData[0]
            if (vio.userIllegalState === 1)
            {
              MessageBox.alert(`用户违规,直到${vio.userIllegalDate}`, '提示', {
                confirmButtonText: '确定',
                callback: action => {
                  next({ name: 'LoginView' })
                }
              })
            }
            else{
              next()
            }
          }
        }).catch(error => {
          console.error('鉴权错误:', error);
          this.$message.error('鉴权错误,请稍后重试');
        })
      }
    }
  },
  {
    path: '/Admin',
    name: 'Admin',
    meta: { title: '管理员 - 智慧自习室' },
    component: () => import('../views/admin.vue'),
    beforeEnter: (to, from, next) => {
      const getUserAccount = sessionStorage.getItem('userAccount') ;
      if (!getUserAccount) {
        ElementUI.MessageBox.alert('请先登录', '提示', {
          confirmButtonText: '去登录',
          callback: action => {
            next({ name: 'LoginView' });
          }
        });
        return;
      }
      const url = `user/list?userAccount=${getUserAccount}`;
      axios.get(url)
        .then(response => {
          const userData = response.data[0];
          const userPrivilege = userData.userPrivilege;
          if (userPrivilege === 0) {
            ElementUI.MessageBox.alert('您没有权限访问管理员界面', '错误', {
              confirmButtonText: '返回',
              callback: action => {
                next({ name: 'HomeView' });
              }
            });
            return;
          }
          next();
        })
        .catch(error => {
          console.error('鉴权失败:', error);
          ElementUI.MessageBox.alert('鉴权失败，请稍后重试或联系管理员', '错误', {
            confirmButtonText: '返回首页',
            callback: action => {
              next({ name: 'HomeView' });
            }
          });
        });
    }
  },
  {
    path: '/ChangePassword',
    name: 'ChangePassword',
    meta: { title: '更改密码 - 智慧自习室' },
    component: () => import('../views/ChangePassword.vue')
  }

]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
