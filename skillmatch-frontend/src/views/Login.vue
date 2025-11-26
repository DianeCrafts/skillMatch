<template>
  <div class="auth-page">

    <div class="auth-card">

      <h2 class="title">Login</h2>

      <form class="form" @submit.prevent="login">

        <label>Email</label>
        <input type="email" class="input" v-model="email" />

        <label>Password</label>
        <input type="password" class="input" v-model="password" />

        <button class="auth-btn">Login</button>

      </form>
    </div>
  </div>
</template>
<script>
import userApi from "@/apis/userApi";   // <= Use your user API wrapper

export default {
  data() {
    return {
      email: "",
      password: ""
    };
  },

  methods: {
    async login() {
      try {
        const res = await userApi.post("/users/login", {email: this.email,password: this.password});
        
        const { token, role, userId, name, email } = res.data;
        localStorage.setItem("token", token);
        localStorage.setItem("role", role);
        localStorage.setItem("userId", userId);
        localStorage.setItem("userName", name);
        localStorage.setItem("email", email);
        if (role === "USER") {
          this.$router.push("/dashboard");
        } else if (role === "RECRUITER") {
          this.$router.push("/recruiter-dashboard");
        }

      } catch (err) {
        alert("Invalid credentials");
        console.error("Login error:", err);
      }
    }
  }
};
</script>


<style scoped>
.auth-page {
  min-height: 100vh;
  background-color: var(--color-bg);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

.auth-card {
  background: var(--color-white);
  border: 2px solid var(--color-primary);
  border-radius: 20px;
  padding: 1rem;
  width: 380px;
  box-sizing: border-box;
}

.title {
  text-align: center;
  font-size: 36px;
  color: var(--color-primary);
  margin-bottom: 2rem;
}

.form {
  width: 100%;
  display: flex;
  flex-direction: column;
}

.form label {
  text-align: left;
  font-size: 15px;
  color: var(--color-primary);
}

.input {
  width: 100%;
  border: 2px solid var(--color-primary);
  border-radius: 10px;
  padding: 0.7rem;
  margin: 0.7rem 0;
  font-size: 16px;
  outline: none;
  box-sizing: border-box;
}

.input:focus {
  border-color: var(--color-accent);
}

.auth-btn {
  width: 100%;
  background-color: var(--color-warm);
  color: var(--color-white);
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
