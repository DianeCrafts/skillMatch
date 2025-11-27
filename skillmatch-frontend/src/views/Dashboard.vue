<template>
  <div class="dashboard-page">
    <div class="dashboard-card">

      <!-- Logout button -->
      <button class="logout-btn" @click="logout">Logout</button>

      <h2 class="title">Welcome {{ userName }}!</h2>

      <button class="upload-btn" @click="goToResumePage">
        {{ resumeStatus === "Uploaded" ? "Edit Resume" : "Upload Resume" }}
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
import resumeApi from "@/apis/resumeApi.js";

export default {
  data() {
    return {
      userName: localStorage.getItem("userName"),
      resumeStatus: "Checking...",
    };
  },

  async created() {
    const userId = localStorage.getItem("userId");

    try {
      await resumeApi.get(`/resumes/user/${userId}`);
      this.resumeStatus = "Uploaded";
    } catch (err) {
      if (err.response?.status === 404) {
        this.resumeStatus = "Not uploaded";
      } else {
        this.resumeStatus = "Error checking resume";
        console.error(err);
      }
    }
  },

  methods: {
    goToResumePage() {
      if (this.resumeStatus === "Uploaded") {
        const userId = localStorage.getItem("userId");
        this.$router.push(`/resume-form?resumeId=${userId}`);
      } else {
        this.$router.push("/upload-resume");
      }
    },

    logout() {
      localStorage.clear();
      this.$router.push("/");
    }
  }
};
</script>
<style scoped>
.dashboard-page {
  min-height: 100vh;
  background-color: var(--color-bg);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

.dashboard-card {
  position: relative;
  background: var(--color-white);
  border: 2px solid var(--color-primary);
  border-radius: 20px;
  padding: 3rem 3.5rem;
  width: 460px;
  text-align: center;
}

/* Logout button */
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

/* Title */
.title {
  font-size: 36px;
  color: var(--color-primary);
  margin-bottom: 1.5rem;
}

/* Upload/Edit Resume button */
.upload-btn {
  background-color: var(--color-warm);
  color: var(--color-white);
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

/* Resume status text */
.resume-status {
  font-size: 16px;
  color: var(--color-primary);
  margin-bottom: 1.5rem;
}

/* Browse Jobs Button */
.job-buttons-single {
  display: flex;
  justify-content: center;
}

.job-btn {
  background-color: var(--color-primary);
  color: var(--color-white);
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

