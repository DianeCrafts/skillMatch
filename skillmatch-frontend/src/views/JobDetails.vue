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
          <span class="meta-label">📍 Location</span>
          <span class="meta-value">{{ job.location }}</span>
        </div>

        <div class="meta-box">
          <span class="meta-label">💼 Experience</span>
          <span class="meta-value">{{ job.experience }}</span>
        </div>

        <div class="meta-box">
          <span class="meta-label">💰 Salary</span>
          <span class="meta-value">{{ job.salary }}</span>
        </div>

        <div class="meta-box">
          <span class="meta-label">🏠 Remote</span>
          <span class="meta-value">{{ job.remote ? 'Yes' : 'No' }}</span>
        </div>
      </div>

      <hr class="divider" />

      <!-- DESCRIPTION -->
      <section class="section">
        <h3 class="section-title">📄 Job Description</h3>
        <p class="section-text">{{ job.description }}</p>
      </section>

      <!-- REQUIREMENTS -->
      <section class="section">
        <h3 class="section-title">📌 Requirements</h3>

        <ul class="requirements-list">
          <li v-for="req in job.requirements" :key="req">• {{ req }}</li>
        </ul>
      </section>

      <!-- SKILLS -->
      <section class="section">
        <h3 class="section-title">🧠 Skills Needed</h3>

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
/* PAGE LAYOUT */
.job-details-container {
  max-width: 800px;
  margin: 3rem auto;
  padding: 1.5rem;
}

/* HEADER */
.job-header {
  background: linear-gradient(135deg, #305669, #4a7e91);
  padding: 2rem;
  border-radius: 16px;
  text-align: center;
  color: white;
  margin-bottom: 2rem;
  box-shadow: 0px 4px 12px rgba(0,0,0,0.12);
}

.job-title {
  font-size: 42px;
  font-weight: 700;
  margin-bottom: 0.4rem;
}

.company-name {
  font-size: 18px;
  opacity: 0.9;
}

/* MAIN CARD */
.job-card {
  background: white;
  padding: 2.5rem;
  border-radius: 16px;
  box-shadow: 0px 2px 10px rgba(0,0,0,0.08);
}

/* META INFO */
.meta-info {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.4rem;
  margin-bottom: 2rem;
}

.meta-box {
  background: #f7fafb;
  padding: 1rem 1.2rem;
  border-radius: 12px;
}

.meta-label {
  font-weight: 600;
  color: #305669;
  display: block;
}

.meta-value {
  font-size: 16px;
  color: #444;
}

/* SECTION */
.section {
  margin-bottom: 2rem;
}

.section-title {
  font-size: 22px;
  color: #305669;
  margin-bottom: 0.6rem;
}

.section-text {
  font-size: 16px;
  color: #444;
}

/* REQUIREMENTS */
.requirements-list li {
  font-size: 16px;
  color: #444;
  margin: 4px 0;
}

/* TAGS */
.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.6rem;
}

.tag {
  background: #305669;
  color: white;
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
}

/* APPLY BUTTON */
.apply-wrapper {
  text-align: center;
  margin-top: 2rem;
}

.apply-btn {
  background: #C1785A;
  color: white;
  padding: 1rem 2.5rem;
  border: none;
  border-radius: 12px;
  font-size: 18px;
  cursor: pointer;
  font-weight: 600;
}

.apply-btn:hover {
  opacity: 0.92;
}

.applied-btn {
  background: gray;
  color: white;
  padding: 1rem 2.5rem;
  border-radius: 12px;
  border: none;
  font-size: 18px;
}
</style>
