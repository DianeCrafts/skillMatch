<template>
  <div class="auth-page">

    <div class="auth-card">

      <h2 class="title">Register</h2>

      <form class="form" @submit.prevent="register">

        <div class="input-group">
          <label>Name</label>
          <input type="text" v-model="name" required />
        </div>

        <div class="input-group">
          <label>Email</label>
          <input type="email" v-model="email" required />
        </div>

        <div class="input-group">
          <label>Password</label>
          <input type="password" v-model="password" required />
        </div>

        <div class="input-group">
          <label>Role</label>
          <select v-model="role" required>
            <option value="USER">User</option>
            <option value="RECRUITER">Recruiter</option>
          </select>
        </div>

        <button class="auth-btn">Register</button>

      </form>

    </div>

  </div>
</template>

<script>
import userApi from "@/apis/userApi";

export default {
  data() {
    return {
      name: "",
      email: "",
      password: "",
      role: "USER"
    };
  },

  methods: {
    async register() {
      try {
        await userApi.post("/users/register", {
          name: this.name,
          email: this.email,
          password: this.password,
          role: this.role
        });

        alert("Registration successful! Please login.");
        this.$router.push("/login");

      } catch (err) {
        alert("Registration failed. Email may already exist.");
        console.error("Registration error:", err);
      }
    }
  }
};
</script>

<style scoped>
/* -----------------------------
   Page Layout
------------------------------ */
.auth-page {
  min-height: 100vh;
  background: var(--color-bg);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

/* -----------------------------
   Card
------------------------------ */
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

/* -----------------------------
   Form
------------------------------ */
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
  font-weight: 600;
  color: var(--color-primary);
}

/* Inputs + Select */
.input-group input,
.input-group select {
  padding: 0.85rem;
  font-size: 16px;
  border-radius: 10px;
  border: 2px solid var(--color-primary);
  outline: none;
  transition: 0.2s ease;
  background: #fff;
}

.input-group input:focus,
.input-group select:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px rgba(255, 170, 120, 0.3);
}

/* -----------------------------
   Button
------------------------------ */
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

/* -----------------------------
   Animation
------------------------------ */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
