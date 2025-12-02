<template>
  <div class="job-details-container" v-if="job">
    
    <!-- HEADER -->
    <div class="job-header">
      <h1 class="job-title">{{ job.title }}</h1>
      <p class="company-name">Company: <span>Unknown Company</span></p>
    </div>

    <!-- MAIN CARD -->
    <div class="job-card">

      <!-- META INFO -->
      <div class="meta-info">
        <div class="meta-box">
          <span class="meta-label"> Location</span>
          <span class="meta-value">{{ job.location }}</span>
        </div>

        <div class="meta-box">
          <span class="meta-label"> Experience</span>
          <span class="meta-value">{{ job.experience }}</span>
        </div>

        <div class="meta-box">
          <span class="meta-label"> Salary</span>
          <span class="meta-value">{{ job.salary }}</span>
        </div>

        <div class="meta-box">
          <span class="meta-label"> Remote</span>
          <span class="meta-value">{{ job.remote ? 'Yes' : 'No' }}</span>
        </div>
      </div>

      <hr class="divider" />

      <!-- DESCRIPTION -->
      <section class="section">
        <h3 class="section-title"> Job Description</h3>
        <p class="section-text">{{ job.description }}</p>
      </section>

      <!-- REQUIREMENTS -->
      <section class="section">
        <h3 class="section-title"> Requirements</h3>

        <ul class="requirements-list">
          <li v-for="req in job.requirements" :key="req">• {{ req }}</li>
        </ul>
      </section>

      <!-- SKILLS -->
      <section class="section">
        <h3 class="section-title"> Skills Needed</h3>

        <div class="tags">
          <span v-for="skill in job.skills" :key="skill" class="tag">
            {{ skill }}
          </span>
        </div>
      </section>

      <!-- APPLY BUTTON -->
      <div class="apply-wrapper">
        <button 
          v-if="!hasApplied"
          class="apply-btn"
          @click="applyToJob"
        >
          Apply Now
        </button>

        <button v-else class="applied-btn" disabled>
          ✔ Already Applied
        </button>
      </div>

    </div>
  </div>
</template>

<script>
import jobsApi from "@/apis/jobApi.js";

export default {
  props: ["id"],

  data() {
    return {
      job: null,
      hasApplied: false
    };
  },

  mounted() {
    this.loadJobDetails();
    this.checkAppliedStatus();
  },

  methods: {
    loadJobDetails() {
      jobsApi.get(`/${this.id}`)
        .then(res => {
          this.job = res.data;
        })
        .catch(err => console.error("Failed to load job details:", err));
    },

    checkAppliedStatus() {
      const userId = localStorage.getItem("userId");

      jobsApi.get("/applied", {
        headers: { "x-user-id": userId }
      })
        .then(res => {
          this.hasApplied = res.data.some(j => j.id === parseInt(this.id));
        })
        .catch(err => console.log("Applied fetch error:", err));
    },

    applyToJob() {
      const userId = localStorage.getItem("userId");

      if (!userId) {
        alert("You must be logged in to apply.");
        return;
      }

      jobsApi.post(`/${this.id}/apply`, null, {
        headers: { "x-user-id": userId }
      })
        .then(() => {
          alert("Applied successfully!");
          this.hasApplied = true;
        })
        .catch(err => console.error(err));
    }
  }
};
</script>
<style scoped>
/* GLOBAL RESET */
*,
*::before,
*::after {
  box-sizing: border-box;
}

/* PAGE LAYOUT */
.job-details-container {
  max-width: 850px;
  margin: 3rem auto;
  padding: 1rem;
  animation: fadeIn 0.3s ease;
}

/* HEADER SECTION */
.job-header {
  background: linear-gradient(135deg, var(--color-primary), #4f8a9e);
  padding: 2.4rem 2rem;
  border-radius: 20px;
  text-align: center;
  color: white;

  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.10);
  margin-bottom: 2.5rem;
}

.job-title {
  font-size: 44px;
  font-weight: 700;
  margin-bottom: 0.4rem;
  letter-spacing: -0.5px;
}

.company-name {
  font-size: 18px;
  opacity: 0.95;
}

/* MAIN JOB CARD */
.job-card {
  background: white;
  padding: 3rem 3.2rem;
  border-radius: 20px;

  border: 1px solid var(--color-border);
  box-shadow:
    0 4px 14px rgba(0, 0, 0, 0.08),
    inset 0 0 0 1px rgba(255,255,255,0.5);
}

/* META INFO BOXES */
.meta-info {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.6rem;
  margin-bottom: 2.4rem;
}

.meta-box {
  background: var(--color-bg);
  border: 1px solid var(--color-border);
  padding: 1.2rem 1.4rem;
  border-radius: 14px;

  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.meta-label {
  font-weight: 600;
  font-size: 15px;
  color: var(--color-primary);
  margin-bottom: 4px;
  display: block;
}

.meta-value {
  font-size: 16px;
  color: #444;
}

/* DIVIDER */
.divider {
  height: 1px;
  background: var(--color-border);
  margin: 2.2rem 0;
}

/* SECTIONS */
.section {
  margin-bottom: 2.6rem;
}

.section-title {
  font-size: 22px;
  font-weight: 650;
  color: var(--color-primary);
  margin-bottom: 0.8rem;
  display: flex;
  align-items: center;
  gap: 6px;
}

.section-text {
  font-size: 16px;
  color: #444;
  line-height: 1.6;
}

/* REQUIREMENTS LIST */
.requirements-list li {
  font-size: 16px;
  color: #444;
  margin: 6px 0;
  line-height: 1.5;
}

/* SKILL TAGS */
.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tag {
  background: var(--color-primary);
  color: white;

  padding: 7px 14px;
  border-radius: 14px;
  font-size: 14px;
  font-weight: 500;

  box-shadow: 0 1px 4px rgba(0,0,0,0.10);
}

/* APPLY BUTTON AREA */
.apply-wrapper {
  text-align: center;
  margin-top: 3rem;
}

/* APPLY BUTTON */
.apply-btn {
  background: var(--color-warm);
  color: white;

  padding: 1rem 2.8rem;
  border-radius: 14px;
  border: none;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  transition: 0.25s ease;
}

.apply-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 18px rgba(0,0,0,0.15);
}

/* DISABLED BUTTON */
.applied-btn {
  background: #a9a9a9;
  color: white;

  padding: 1rem 2.8rem;
  border-radius: 14px;
  border: none;
  font-size: 18px;
  opacity: 0.85;
}

/* FADE ANIMATION */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(6px); }
  to   { opacity: 1; transform: translateY(0); }
}
</style>
