<template>
  <div class="jobs-page">

    <div class="jobs-card">

      <h2 class="title">Jobs</h2>

      <!-- Tabs -->
      <div class="tabs">
        <button 
          class="tab" 
          :class="{ active: activeTab === 'all' }"
          @click="switchTab('all')"
        >
          All Jobs
        </button>

        <button 
          class="tab"
          :class="{ disabled: !resumeUploaded, active: activeTab === 'recommended' }"
          @click="resumeUploaded && switchTab('recommended')"
        >
          Recommended Jobs
        </button>

        <button 
          class="tab"
          :class="{ active: activeTab === 'saved' }"
          @click="switchTab('saved')"
        >
          Saved Jobs
        </button>

        <button 
          class="tab"
          :class="{ active: activeTab === 'applied' }"
          @click="switchTab('applied')"
        >
          Applied Jobs
        </button>
      </div>

      <div class="divider"></div>

      <!-- JOB LIST -->
      <div
        v-for="job in filteredJobs"
        :key="job.id"
        class="job-item"
      >

        <div class="job-info">
          <h3 class="job-title clickable" @click="$router.push('/jobs/' + job.id)">
            {{ job.title }}
          </h3>

          <p class="company">{{ job.company }}</p>
          <p class="location">{{ job.location }}</p>
        </div>

        <div class="job-actions">
          <button v-if="activeTab !== 'applied'" class="apply-btn" @click="applyToJob(job)">
            Apply Now
          </button>

          <button class="save-btn" @click="toggleSave(job)">
            {{ isSaved(job) ? '♥' : '♡' }}
          </button>
        </div>

      </div>

    </div>

  </div>
</template>
<script>
import jobsApi from "@/apis/jobApi.js";
import applicationApi from "@/apis/applicationApi.js";

export default {
  data() {
    return {
      activeTab: "all",
      resumeUploaded: true,

      allJobs: [],
      recommendedJobs: [],
      savedJobs: [],
      appliedJobs: []
    };
  },

  computed: {
    filteredJobs() {
      switch (this.activeTab) {
        case "all": return this.allJobs;
        case "recommended": return this.recommendedJobs;
        case "saved": return this.savedJobs;
        case "applied": return this.appliedJobs;
        default: return [];
      }
    }
  },

  mounted() {
    this.fetchAllJobs();
    this.fetchAppliedJobs();
  },

  methods: {
    switchTab(tab) {
      this.activeTab = tab;

      if (tab === "all") this.fetchAllJobs();
      if (tab === "applied") this.fetchAppliedJobs();
    },

    fetchAllJobs() {
      jobsApi.get("")
        .then(res => {
          this.allJobs = res.data.map(job => ({
            id: job.id,
            title: job.title,
            company: "Unknown Company",
            location: job.location
          }));

          this.activeTab = "all";
        })
        .catch(err => console.error("Failed to load all jobs:", err));
    },

    /* ---------------------------
       APPLY TO JOB (BACKEND CALL)
    ----------------------------*/
    applyToJob(job) {
      const userId = localStorage.getItem("userId");

      if (!userId) {
        alert("You must be logged in to apply.");
        return;
      }

      // FRONTEND PREVENTION: already applied?
      if (this.hasApplied(job)) {
        alert("You already applied to this job.");
        return;
      }

      applicationApi.post(`/${job.id}/apply`, null, {
        headers: { "x-user-id": userId }
      })
        .then(() => {
          alert("Applied successfully!");
          this.fetchAppliedJobs(); // refresh applied list
        })
        .catch(err => {
          if (err.response?.status === 400) {
            alert("You already applied to this job.");
          } else if (err.response?.status === 403) {
            alert("You must upload a resume first.");
          } else {
            alert("Failed to apply to job.");
          }
          console.error("Apply failed:", err);
        });
    },
    hasApplied(job) {
      return this.appliedJobs.some(applied => applied.id === job.id);
    },

    toggleSave(job) {
      if (this.isSaved(job)) {
        this.savedJobs = this.savedJobs.filter(j => j.id !== job.id);
      } else {
        this.savedJobs.push(job);
      }
    },

    isSaved(job) {
      return this.savedJobs.some(j => j.id === job.id);
    },

    fetchAppliedJobs() {
      const userId = localStorage.getItem("userId");

      applicationApi.get("/applied", {
        headers: { "x-user-id": userId }
      })
        .then(res => {
          this.appliedJobs = res.data.map(job => ({
            id: job.id,
            title: job.title,
            company: "Unknown Company",
            location: job.location
          }));
        })
        .catch(err => console.error("Failed to load applied jobs:", err));
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

/* PAGE */
.jobs-page {
  min-height: 100vh;
  background-color: var(--color-bg);
  padding: 3.5rem 1rem;
  display: flex;
  justify-content: center;
  animation: fadeIn 0.3s ease;
}

/* MAIN CARD */
.jobs-card {
  width: 760px;
  background: white;
  border-radius: 20px;
  padding: 2.6rem 3rem;
  border: 1px solid var(--color-border);

  box-shadow:
    0 4px 12px rgba(0, 0, 0, 0.08),
    inset 0 0 0 1px rgba(255,255,255,0.4);
}

/* TITLE */
.title {
  font-size: 34px;
  color: var(--color-primary);
  margin-bottom: 1.6rem;
  font-weight: 700;
  letter-spacing: -0.5px;
}

/* TABS */
.tabs {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.tab {
  background: transparent;
  border: none;
  padding: 0.55rem 1.2rem;
  font-size: 15px;
  color: var(--color-primary);
  cursor: pointer;
  border-radius: 10px;
  transition: 0.18s;
  font-weight: 500;
}

.tab:hover:not(.disabled) {
  background: rgba(48, 86, 105, 0.08);
}

.tab.active {
  background: var(--color-primary);
  color: white;
  font-weight: 600;
}

.tab.disabled {
  opacity: 0.45;
  cursor: not-allowed;
}

/* DIVIDER */
.divider {
  height: 1px;
  background: var(--color-border);
  margin-bottom: 1.5rem;
}

/* JOB ITEM */
.job-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;

  padding: 1.3rem 0;
  border-bottom: 1px solid rgba(0,0,0,0.05);

  transition: 0.18s;
}

.job-item:hover {
  background: rgba(48, 86, 105, 0.03);
  border-radius: 10px;
  padding-left: 0.6rem;
}

/* JOB INFO */
.job-info {
  max-width: 70%;
}

.job-title {
  font-size: 21px;
  color: var(--color-primary);
  margin-bottom: 6px;
  font-weight: 600;
  cursor: pointer;
  transition: 0.18s;
}

.job-title:hover {
  text-decoration: underline;
}

.company,
.location {
  font-size: 15px;
  color: var(--color-primary);
  opacity: 0.85;
}

/* ACTIONS */
.job-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 0.6rem;
}

/* APPLY BUTTON */
.apply-btn {
  background-color: var(--color-warm);
  color: white;
  padding: 0.55rem 1.4rem;
  border-radius: 10px;
  border: none;
  cursor: pointer;
  font-weight: 600;
  font-size: 15px;
  transition: 0.18s;
}

.apply-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0,0,0,0.12);
}

/* SAVE BUTTON */
.save-btn {
  background: white;
  border: 2px solid var(--color-primary);
  font-size: 20px;
  width: 36px;
  height: 36px;
  border-radius: 50%;

  display: flex;
  justify-content: center;
  align-items: center;

  cursor: pointer;
  transition: 0.18s;
  color: var(--color-primary);
}

.save-btn:hover {
  background: var(--color-primary);
  color: white;
}

/* FADE-IN ANIMATION */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(6px); }
  to   { opacity: 1; transform: translateY(0); }
}
</style>
