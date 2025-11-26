<template>
  <div class="recruiter-page">

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
      userName: localStorage.getItem("userName"),
    };
  }
};
</script>

<style scoped>
/* PAGE BACKGROUND */
.recruiter-page {
  min-height: 100vh;
  background-color: #FFF9F3;

  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;

  padding-top: 4rem;
}

/* WELCOME TITLE */
.welcome-title {
  font-size: 48px;
  font-weight: 600;
  color: #305669;
  margin-bottom: 2.5rem;
}

/* MAIN CARD */
.dashboard-card {
  background: white;
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
  color: #305669;
  background: #F5F1EE;
  border: 2px solid #E5DFDA;
  border-radius: 12px;
  cursor: pointer;
}

.action-btn:hover {
  background: #ECE7E3;
}
</style>
