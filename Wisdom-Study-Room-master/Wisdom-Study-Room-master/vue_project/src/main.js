import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from "axios";
Vue.prototype.$axios = axios    //全局注册，使用方法为:this.$axios
axios.defaults.baseURL = '/api';


Vue.use(ElementUI)
Vue.config.productionTip = false

 new Vue({
  router,
  render: h => h(App)
}).$mount('#app')


