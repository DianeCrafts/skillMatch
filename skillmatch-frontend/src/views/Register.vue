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
          <option value="USER">User</option>
          <option value="RECRUITER">Recruiter</option>
        </select>


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
      role: "USER"   // recommended default
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
  font-size: 15px;
  color: var(--color-primary);
  text-align: left;
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
