<template>
  <div class="recruiter-page">

    <!-- Logout button -->
    <button class="logout-btn" @click="logout">Logout</button>

    <div class="dashboard-card">

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
      recruiterName: localStorage.getItem("userName"),
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
/* PAGE BACKGROUND */
.recruiter-page {
  min-height: 100vh;
  background-color: var(--color-bg);

  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;

  position: relative; /* for logout absolute positioning */
}

/* LOGOUT BUTTON */
.logout-btn {
  position: absolute;
  top: 20px;
  right: 20px;

  background: var(--color-red);
  color: var(--color-white);

  border: none;
  padding: 8px 14px;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
}
.logout-btn:hover {
  opacity: 0.9;
}

/* DASHBOARD CARD */
.dashboard-card {
  background: var(--color-white);
  border: 2px solid var(--color-primary);
  border-radius: 20px;
  padding: 3rem 3.5rem;
  width: 460px;
  text-align: center;

  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);

  display: flex;
  flex-direction: column;
  gap: 1.3rem;
}

/* TITLE */
.welcome-title {
  font-size: 36px;
  font-weight: 600;
  color: var(--color-primary);
  margin-bottom: 1.5rem;
}

/* BUTTONS */
.action-btn {
  background-color: var(--color-warm);
  color: var(--color-white);
  font-size: 18px;
  font-weight: 600;

  padding: 0.9rem 2rem;
  border: none;
  border-radius: 10px;

  cursor: pointer;
}
.action-btn:hover {
  opacity: 0.95;
}
</style>
