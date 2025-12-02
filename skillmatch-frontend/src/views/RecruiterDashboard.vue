<template>
  <div class="recruiter-page">

    <div class="dashboard-card">

      <!-- Logout inside frame -->
      <button class="logout-btn" @click="logout">Logout</button>

      <h1 class="welcome-title">Welcome, {{ recruiterName }}!</h1>

      <button class="action-btn" @click="$router.push('/post-job')">
        Post a Job
      </button>

      <button class="action-btn" @click="$router.push('/recruiter/my-jobs')">
        View My Jobs
      </button>

    </div>

  </div>
</template>

<script>
export default {
  data() {
    return {
      recruiterName: localStorage.getItem("userName")
    };
  },

  methods: {
    logout() {
      localStorage.clear();
      this.$router.push("/");
    }
  }
};
</script>

<style scoped>
/* -----------------------------
   Page Layout
------------------------------ */
.recruiter-page {
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
.dashboard-card {
  position: relative;
  width: 460px;
  background: var(--color-white);
  border-radius: 18px;
  padding: 3rem 3rem;
  text-align: center;

  box-shadow: 0px 10px 25px rgba(0, 0, 0, 0.08);
  animation: fadeIn 0.4s ease;

  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

/* -----------------------------
   Logout Button (Inside Card)
------------------------------ */
.logout-btn {
  position: absolute;
  top: 18px;
  right: 18px;

  background: var(--color-red);
  color: var(--color-white);

  border: none;
  padding: 8px 14px;
  border-radius: 8px;

  font-size: 14px;
  cursor: pointer;
  transition: 0.2s ease;
}

.logout-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* -----------------------------
   Title
------------------------------ */
.welcome-title {
  font-size: 32px;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 1.5rem;
}

/* -----------------------------
   Buttons
------------------------------ */
.action-btn {
  background: var(--color-warm);
  color: var(--color-white);

  font-size: 18px;
  font-weight: 600;

  padding: 0.9rem 2rem;
  border: none;
  border-radius: 10px;

  cursor: pointer;
  transition: 0.25s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.12);
}

/* -----------------------------
   Animation
------------------------------ */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
