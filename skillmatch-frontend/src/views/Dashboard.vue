<template>
  <div class="dashboard-page">

    <div class="dashboard-card">

      <!-- Logout -->
      <button class="logout-btn" @click="logout">Logout</button>

      <h2 class="title">Welcome, {{ userName }}!</h2>

      <button class="upload-btn" @click="goToResumePage">
        {{ resumeStatus === "Uploaded" ? "Edit Resume" : "Upload Resume" }}
      </button>

      <p class="resume-status">
        Resume status: <strong>{{ resumeStatus }}</strong>
      </p>

      <div class="job-section">
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
      resumeStatus: "Checking..."
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
/* -----------------------------
   Page Layout
------------------------------ */
.dashboard-page {
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
}

/* -----------------------------
   Logout Button
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
.title {
  font-size: 32px;
  margin-bottom: 2rem;
  font-weight: 700;
  color: var(--color-primary);
}

/* -----------------------------
   Resume Button
------------------------------ */
.upload-btn {
  background: var(--color-warm);
  color: var(--color-white);
  font-size: 18px;
  padding: 0.9rem 2rem;

  border: none;
  border-radius: 10px;
  cursor: pointer;

  font-weight: 600;
  transition: 0.25s ease;
  margin-bottom: 1.3rem;
}

.upload-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.12);
}

/* -----------------------------
   Resume Status
------------------------------ */
.resume-status {
  font-size: 16px;
  color: var(--color-primary);
  margin-bottom: 2rem;
}

/* -----------------------------
   Job Button
------------------------------ */
.job-section {
  display: flex;
  justify-content: center;
}

.job-btn {
  background: var(--color-primary);
  color: var(--color-white);
  font-size: 17px;

  padding: 0.8rem 2.4rem;
  border-radius: 10px;
  border: none;
  cursor: pointer;

  transition: 0.25s ease;
}

.job-btn:hover {
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
