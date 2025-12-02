<template>
  <div class="applicants-page">

    <h1 class="title">Job Applicants</h1>

    <div class="applicants-table">

      <!-- Header -->
      <div class="table-row header">
        <div class="col">Applicant</div>
        <div class="col">Resume</div>
        <div class="col">Match</div>
        <div class="col">Status</div>
      </div>

      <!-- Applicant Rows -->
      <div
        class="table-row applicant-row"
        v-for="applicant in applicants"
        :key="applicant.applicationId"
      >
        <!-- Name -->
        <div class="col name-col">
          {{ applicant.userName }}
        </div>

        <!-- Resume Button -->
        <div class="col">
          <button class="resume-btn" @click="viewResume(applicant.resumeId)">
            View Resume
          </button>
        </div>

        <!-- Stars -->
        <div class="col stars">
          <span v-for="n in convertScoreToStars(applicant.matchScore)" :key="n">⭐</span>
        </div>

        <!-- Status Select -->
        <div class="col">
          <select
            class="status-select"
            v-model="applicant.status"
            @change="updateStatus(applicant)"
          >
            <option value="Reviewed">Reviewed</option>
            <option value="Shortlisted">Shortlisted</option>
            <option value="Rejected">Rejected</option>
          </select>
        </div>

      </div>

    </div>

  </div>
</template>

<script>
import applicationApi from "@/apis/applicationApi.js";

export default {
  data() {
    return {
      applicants: []
    };
  },

  mounted() {
    this.fetchApplicants();
  },

  methods: {
    fetchApplicants() {
      const jobId = this.$route.params.jobId;

      applicationApi.get(`/job/${jobId}`)
        .then(res => {
          this.applicants = res.data;
        })
        .catch(err => {
          console.error("Failed to load applicants:", err);
          alert("Could not load applicants.");
        });
    },

    convertScoreToStars(score) {
      return Math.round(score / 20);
    },

    viewResume(resumeId) {
      this.$router.push(`/resume-view/${resumeId}`);
    },

    updateStatus(applicant) {
      applicationApi.put(`/${applicant.applicationId}/status`, {
        status: applicant.status
      })
      .catch(err => {
        console.error("Failed to update status:", err);
        alert("Could not update applicant status.");
      });
    }
  }
};
</script>

<style scoped>
/* --------------------------------
   PAGE
----------------------------------- */
.applicants-page {
  min-height: 100vh;
  background: var(--color-bg);

  padding: 3rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;

  animation: fadeIn 0.4s ease;
}

/* Title */
.title {
  font-size: 42px;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 2rem;
}

/* --------------------------------
   TABLE
----------------------------------- */
.applicants-table {
  width: 850px;
}

.table-row {
  display: grid;
  grid-template-columns: 2fr 1.2fr 1fr 1.2fr;

  padding: 1.1rem 1rem;
  align-items: center;

  border-radius: 14px;
}

.header {
  font-weight: 700;
  font-size: 16px;
  color: var(--color-primary);

  border-bottom: 2px solid var(--color-border);

  margin-bottom: 0.8rem;
  background: none !important;
}

/* Applicant rows */
.applicant-row {
  background: white;

  margin-bottom: 1rem;

  box-shadow: 0 4px 12px rgba(0,0,0,0.06);
  transition: 0.2s ease;
}

.applicant-row:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 18px rgba(0,0,0,0.1);
}

.col {
  color: var(--color-primary);
  display: flex;
  align-items: center;
}

/* --------------------------------
   STARS
----------------------------------- */
.stars span {
  font-size: 20px;
  color: #E0A800;
}

/* --------------------------------
   RESUME BUTTON
----------------------------------- */
.resume-btn {
  padding: 8px 18px;

  background-color: white;
  border: 2px solid var(--color-border);

  font-size: 15px;
  font-weight: 600;
  color: var(--color-primary);

  border-radius: 10px;
  cursor: pointer;

  transition: 0.2s ease;
}

.resume-btn:hover {
  background-color: #F8F4F1;
  border-color: var(--color-accent);
  transform: translateY(-2px);
}

/* --------------------------------
   STATUS SELECT
----------------------------------- */
.status-select {
  width: 100%;
  padding: 8px 10px;

  border-radius: 10px;
  border: 2px solid var(--color-border);

  color: var(--color-primary);
  font-size: 15px;

  background: white;
  cursor: pointer;
  transition: 0.2s ease;
}

.status-select:hover,
.status-select:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px rgba(138,190,185,0.25);
}

/* --------------------------------
   ANIMATIONS
----------------------------------- */
@keyframes fadeIn {
  from { opacity: 0; }
  to   { opacity: 1; }
}
</style>
