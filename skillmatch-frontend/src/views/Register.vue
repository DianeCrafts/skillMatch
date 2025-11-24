<template>
  <div class="auth-page">

    <div class="auth-card">

      <h2 class="title">Register</h2>

      <form class="form" @submit.prevent="register">

        <label>Name</label>
        <input type="text" class="input" v-model="name" />

        <label>Email</label>
        <input type="email" class="input" v-model="email" />

        <label>Password</label>
        <input type="password" class="input" v-model="password" />

        <label>Role</label>
        <select class="input" v-model="role">
          <option>Select role</option>
          <option>User</option>
        </select>

        <button class="auth-btn">Register</button>

      </form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      name: "",
      email: "",
      password: "",
      role: ""
    };
  },

  methods: {
    register() {
      // TEMPORARY — until backend is connected
      // Auto-login the user after registering
      if (this.role === "USER") {
        this.$router.push("/dashboard");
      } 
      else if (this.role === "RECRUITER") {
        this.$router.push("/recruiter-dashboard");
      }
    }
  }
};
</script>

<style scoped>
/* SAME CSS AS BEFORE — unchanged */
.auth-page {
  min-height: 100vh;
  background-color: #FFF9F3;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

.auth-card {
  background: white;
  border: 2px solid #305669;
  border-radius: 20px;
  padding: 3rem 3rem 3.5rem;
  width: 380px;
}

.title {
  text-align: center;
  font-size: 36px;
  color: #305669;
  margin-bottom: 2rem;
}

.form label {
  font-size: 15px;
  color: #305669;
}

.input {
  width: 100%;
  border: 2px solid #305669;
  border-radius: 10px;
  padding: 0.7rem 1rem;
  margin: 0.7rem 0 1.2rem;
  font-size: 16px;
  outline: none;
}

.input:focus {
  border-color: #8ABEB9;
}

.auth-btn {
  width: 100%;
  background-color: #C1785A;
  color: white;
  font-size: 18px;
  font-weight: 600;
  padding: 0.9rem;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.auth-btn:hover {
  opacity: 0.95;
}
</style>
