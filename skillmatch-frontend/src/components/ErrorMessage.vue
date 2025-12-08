<template>
  <div v-if="message" class="error-box">
    {{ message }}
  </div>
</template>

<script>
export default {
  props: {
    message: {
      type: String,
      default: ""
    }
  }
};
</script>

<style scoped>
.error-box {
  background: rgba(201, 78, 78, 0.12);  
  color: var(--color-red);            
  border: 1px solid var(--color-red);   
  padding: 10px 14px;
  margin-bottom: 15px;
  border-radius: 6px;
  font-size: 14px;
}
</style>
