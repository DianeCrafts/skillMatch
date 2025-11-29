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

      jobsApi.post(`/${job.id}/apply`, null, {
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

      jobsApi.get("/applied", {
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
/* SAME CSS AS BEFORE */
.jobs-page {
  min-height: 100vh;
  background-color: #FFF9F3;
  padding: 3rem 0;
  display: flex;
  justify-content: center;
}

.jobs-card {
  width: 700px;
  background: white;
  border-radius: 16px;
  padding: 2rem 2.5rem;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}

.title {
  font-size: 32px;
  color: #305669;
  margin-bottom: 1rem;
}

.tabs {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1rem;
}

.tab {
  background: none;
  border: none;
  color: #305669;
  padding-bottom: 6px;
  cursor: pointer;
  font-size: 16px;
}

.tab.active {
  border-bottom: 2px solid #305669;
  font-weight: 600;
}

.tab.disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.divider {
  height: 1px;
  background: #e4e4e4;
  margin-bottom: 1rem;
}

.job-item {
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #eee;
  padding: 1rem 0;
}

.job-title {
  font-size: 20px;
  color: #305669;
  margin-bottom: 4px;
}

.company,
.location {
  font-size: 14px;
  color: #305669;
}

.tags {
  display: flex;
  gap: 0.5rem;
  margin-top: 6px;
}

.tag {
  background-color: #305669;
  color: white;
  padding: 2px 8px;
  border-radius: 6px;
  font-size: 12px;
}

.job-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 0.6rem;
}

.apply-btn {
  background-color: #C1785A;
  color: white;
  padding: 0.5rem 1.3rem;
  border-radius: 8px;
  border: none;
  cursor: pointer;
}

.apply-btn:hover {
  opacity: 0.9;
}

.save-btn {
  background: none;
  border: none;
  font-size: 22px;
  cursor: pointer;
  color: #305669;
}
.save-btn:hover {
  opacity: 0.6;
}

.clickable {
  cursor: pointer;
}
.clickable:hover {
  text-decoration: underline;
}

</style>
