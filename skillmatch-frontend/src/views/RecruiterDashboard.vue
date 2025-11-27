<template>
  <div class="recruiter-page">

    <!-- Logout Button -->
    <button class="logout-btn" @click="logout">Logout</button>

    <h1 class="welcome-title">Welcome, {{ recruiterName }}</h1>

    <div class="dashboard-card">

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
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;

  padding-top: 4rem;
  position: relative; /* Needed for logout positioning */
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

/* WELCOME TITLE */
.welcome-title {
  font-size: 48px;
  font-weight: 600;
  color: var(--color-primary);
  margin-bottom: 2.5rem;
}

/* MAIN CARD */
.dashboard-card {
  background: var(--color-white);
  border-radius: 16px;
  padding: 2.5rem 3rem;
  width: 420px;
  text-align: center;

  box-shadow: 0 2px 6px rgba(0,0,0,0.08);

  display: flex;
  flex-direction: column;
  gap: 1.3rem;
}

/* BUTTONS */
.action-btn {
  width: 100%;
  padding: 1rem 0;
  font-size: 20px;
  font-weight: 500;

  color: var(--color-primary);
  background: var(--color-white);
  border: 2px solid var(--color-border);

  border-radius: 12px;
  cursor: pointer;
}

.action-btn:hover {
  background: var(--color-border);
}
</style>