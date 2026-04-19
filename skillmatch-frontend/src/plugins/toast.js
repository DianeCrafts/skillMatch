import { createApp } from "vue";
import Toast from "@/components/Toast.vue";

export default {
  install(app) {
    const toastApp = createApp(Toast);
    const toastVm = toastApp.mount(document.createElement("div"));
    document.body.appendChild(toastVm.$el);

    app.config.globalProperties.$toast = {
      success(msg) {
        toastVm.show(msg, "success");
      },
      error(msg) {
        toastVm.show(msg, "error");
      }
    };
  }
};
