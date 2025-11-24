<template>
  <div class="dashboard-page">
    <div class="dashboard-card">

      <h2 class="title">Welcome {{ userName }}!</h2>

      <button class="upload-btn" @click="$router.push('/upload-resume')">
        Upload Resume
      </button>

      <p class="resume-status">
        Resume status: <strong>{{ resumeStatus }}</strong>
      </p>

      <!-- ONE BUTTON ONLY -->
      <div class="job-buttons-single">
        <button class="job-btn" @click="$router.push('/jobs')">
            Browse All Jobs
        </button>

      </div>

    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userName: "Anna",
      resumeStatus: "Not uploaded",
    };
  }
};
</script>

<style scoped>
.dashboard-page {
  min-height: 100vh;
  background-color: #FFF9F3;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

.dashboard-card {
  background: white;
  border: 2px solid #305669;
  border-radius: 20px;
  padding: 3rem 3.5rem;
  width: 460px;
  text-align: center;
}

/* Title */
.title {
  font-size: 36px;
  color: #305669;
  margin-bottom: 1.5rem;
}

/* Upload Resume button */
.upload-btn {
  background-color: #C1785A;
  color: white;
  font-size: 18px;
  font-weight: 600;
  padding: 0.9rem 2rem;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  margin-bottom: 1.3rem;
}
.upload-btn:hover {
  opacity: 0.95;
}

/* Resume status */
.resume-status {
  font-size: 16px;
  color: #305669;
  margin-bottom: 1.5rem;
}

/* SINGLE job button */
.job-buttons-single {
  display: flex;
  justify-content: center;
}

.job-btn {
  background-color: #305669;
  color: white;
  font-size: 16px;
  padding: 0.7rem 2rem;
  border-radius: 10px;
  border: none;
  cursor: pointer;
}

.job-btn:hover {
  opacity: 0.9;
}
</style>
