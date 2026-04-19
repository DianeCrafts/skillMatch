import { createApp } from "vue";
import App from "./App.vue";
import router from "./router/index.js";
import "./assets/main.css";
import toastPlugin from "@/plugins/toast";
createApp(App).use(router).use(toastPlugin).mount("#app");
