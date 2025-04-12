import './assets/main.css'
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import { createApp } from 'vue'
import App from './App.vue'
import './assets/fonts/font.css'
import {router} from './router/index.js';


createApp(App).use(ElementPlus).use(router).mount('#app')