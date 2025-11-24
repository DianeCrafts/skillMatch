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

          <div class="tags">
            <span 
              v-for="tag in job.tags" 
              :key="tag" 
              class="tag"
            >
              {{ tag }}
            </span>
          </div>
        </div>

        <div class="job-actions">
          <button class="apply-btn" @click="applyToJob(job)">
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
export default {
  data() {
    return {
      activeTab: "all",

      // Replace with backend value later
      resumeUploaded: true,

      // Mock job lists (replace with real backend later)
      allJobs: [
        { id: 1, title: "Software Engineer", company: "Tech Solutions", location: "$120,000 – 140,000", tags: [] },
        { id: 2, title: "Marketing Manager", company: "Creative Agency", location: "San Francisco, CA", tags: [] },
        { id: 3, title: "Data Analyst", company: "Finance Corp", location: "$90,000", tags: ["Azure", "AWS"] },
        { id: 4, title: "Product Designer", company: "Innovate Ltd.", location: "Austin, TX", tags: ["Ruby", "Rails"] },
      ],

      recommendedJobs: [
        { id: 5, title: "Machine Learning Engineer", company: "AI Labs", location: "Remote", tags: ["Python", "TensorFlow"] },
        { id: 6, title: "Backend Developer", company: "Cloud Corp", location: "$100,000", tags: ["Go", "Docker"] }
      ],

      savedJobs: [
        { id: 7, title: "UI/UX Designer", company: "Designify", location: "New York, NY", tags: ["Figma", "UX"] }
      ],

      appliedJobs: [
        { id: 8, title: "Frontend Developer", company: "WebStars", location: "$85,000", tags: ["Vue", "CSS"] }
      ]
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

  methods: {

    /* ==============================
       TAB SWITCHING (FUTURE DYNAMIC)
    =============================== */
    switchTab(tab) {
      this.activeTab = tab;

      // Example backend logic:
      // if (tab === "all") this.fetchAllJobs();
      // if (tab === "recommended") this.fetchRecommendedJobs();
      // if (tab === "saved") this.fetchSavedJobs();
      // if (tab === "applied") this.fetchAppliedJobs();
    },


    /* ==============================
       APPLY TO JOB (FUTURE BACKEND)
    =============================== */
    applyToJob(job) {
      console.log("Applying to job:", job);

      // Future backend call:
      // await axios.post("/api/jobs/apply", { jobId: job.id });
      // this.fetchAppliedJobs();
    },


    /* =====================================
       SAVE / UNSAVE JOB (FUTURE BACKEND)
    ===================================== */
    toggleSave(job) {
      // If job is already saved → remove it
      if (this.isSaved(job)) {
        this.savedJobs = this.savedJobs.filter(j => j.id !== job.id);

        // Future backend call:
        // await axios.delete(`/api/jobs/saved/${job.id}`);
      }

      // Otherwise → add it
      else {
        this.savedJobs.push(job);

        // Future backend call:
        // await axios.post(`/api/jobs/saved`, { jobId: job.id });
      }
    },

    isSaved(job) {
      return this.savedJobs.some(j => j.id === job.id);
    },


    /* =============================================
       BACKEND FETCH FUNCTIONS (PLACEHOLDERS)
    ============================================= */
    fetchAllJobs() {
      // Future:
      // this.allJobs = await axios.get('/api/jobs/all')
    },

    fetchRecommendedJobs() {
      // Future:
      // this.recommendedJobs = await axios.get('/api/jobs/recommended')
    },

    fetchSavedJobs() {
      // Future:
      // this.savedJobs = await axios.get('/api/jobs/saved')
    },

    fetchAppliedJobs() {
      // Future:
      // this.appliedJobs = await axios.get('/api/jobs/applied')
    },

    fetchResumeStatus() {
      // Future:
      // const res = await axios.get('/api/user/resume-status')
      // this.resumeUploaded = res.data.uploaded
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
