<template>
  <div class="auth-page">

    <div class="auth-card">

      <h2 class="title">Login</h2>

      <form class="form" @submit.prevent="login">

        <div class="input-group">
          <label>Email</label>
          <input type="email" v-model="email" required />
        </div>

        <div class="input-group">
          <label>Password</label>
          <input type="password" v-model="password" required />
        </div>

        <button class="auth-btn">Login</button>

      </form>

    </div>

  </div>
</template>

<script>
import userApi from "@/apis/userApi";

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
        const res = await userApi.post("/users/login", {
          email: this.email,
          password: this.password
        });

        const { token, role, userId, name, email } = res.data;

        localStorage.setItem("token", token);
        localStorage.setItem("role", role);
        localStorage.setItem("userId", userId);
        localStorage.setItem("userName", name);
        localStorage.setItem("email", email);

        if (role === "USER") this.$router.push("/dashboard");
        else this.$router.push("/recruiter-dashboard");

      } catch (err) {
        alert("Invalid credentials");
        console.error("Login error:", err);
      }
    }
  }
};
</script>

<style scoped>
/* -------------------------------
   Page Layout
-------------------------------- */
.auth-page {
  min-height: 100vh;
  background: var(--color-bg);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

/* -------------------------------
   Card
-------------------------------- */
.auth-card {
  width: 380px;
  background: var(--color-white);
  border-radius: 18px;
  padding: 2.5rem 2rem;
  box-shadow: 0px 10px 25px rgba(0, 0, 0, 0.08);
  animation: fadeIn 0.4s ease-in-out;
}

.title {
  text-align: center;
  font-size: 34px;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 2.5rem;
}

/* -------------------------------
   Form
-------------------------------- */
.form {
  display: flex;
  flex-direction: column;
  gap: 1.6rem;
}

/* Input Group */
.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

.input-group label {
  font-size: 14px;
  color: var(--color-primary);
  font-weight: 600;
}

/* Inputs */
.input-group input {
  padding: 0.85rem;
  font-size: 16px;
  border-radius: 10px;
  border: 2px solid var(--color-primary);
  outline: none;
  transition: 0.2s ease;
  background: #fff;
}

.input-group input:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px rgba(255, 170, 120, 0.3); /* soft highlight */
}

/* -------------------------------
   Button
-------------------------------- */
.auth-btn {
  padding: 0.9rem;
  font-size: 18px;
  font-weight: 600;
  border: none;
  border-radius: 10px;
  cursor: pointer;

  background: var(--color-warm);
  color: var(--color-white);

  transition: 0.2s ease;
}

.auth-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

/* -------------------------------
   Animations
-------------------------------- */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
