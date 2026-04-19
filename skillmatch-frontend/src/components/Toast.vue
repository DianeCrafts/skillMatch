<template>
  <transition name="toast-fade">
    <div v-if="visible" :class="['toast', type]">
      {{ message }}
    </div>
  </transition>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      message: "",
      type: "success"
    };
  },
  methods: {
    show(msg, type = "success") {
      this.message = msg;
      this.type = type;
      this.visible = true;

      // auto hide after 3 seconds
      setTimeout(() => {
        this.visible = false;
      }, 3000);
    }
  }
};
</script>

<style scoped>
.toast {
  min-width: 320px;
  max-width: 520px;
  padding: 20px 26px;
  border-radius: 12px;
  color: var(--color-white);
  font-size: 18px;
  font-weight: 500;
  text-align: center;

  /* Centered */
  position: fixed;
  top: 10%;
  left: 50%;
  transform: translate(-50%, -50%);

  /* Layering */
  z-index: 99999;

  /* Shadow */
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.18);
}

.toast.success {
  background-color: var(--color-accent);
}

.toast.error {
  background-color: var(--color-red);
}

/* Smooth fade + scale */
.toast-fade-enter-active,
.toast-fade-leave-active {
  transition: opacity .35s, transform .35s;
}

.toast-fade-enter-from,
.toast-fade-leave-to {
  opacity: 0;
  transform: translate(-50%, -50%) scale(0.92);
}
</style>
