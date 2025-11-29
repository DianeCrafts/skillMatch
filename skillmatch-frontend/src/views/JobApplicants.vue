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
        class="table-row"
        v-for="applicant in applicants"
        :key="applicant.applicationId"
      >
        <!-- Name -->
        <div class="col">{{ applicant.userName }}</div>

        <!-- Resume -->
        <div class="col">
          <button class="resume-btn" @click="viewResume(applicant.resumeId)">
            View Resume
          </button>
        </div>

        <!-- Match Score as stars -->
        <div class="col stars">
          <span v-for="n in convertScoreToStars(applicant.matchScore)" :key="n">⭐</span>
        </div>

        <!-- Status Dropdown -->
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
    /* --------------------------------------------
       FETCH APPLICANTS FOR THIS JOB
    --------------------------------------------- */
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

    /* Convert 0–100 score to 0–5 stars */
    convertScoreToStars(score) {
      return Math.round(score / 20);
    },

    /* --------------------------------------------
       VIEW RESUME (opens resume-view/:id page)
    --------------------------------------------- */
    viewResume(resumeId) {
      this.$router.push(`/resume-view/${resumeId}`);
    },

    /* --------------------------------------------
       UPDATE STATUS (PUT to backend)
    --------------------------------------------- */
    updateStatus(applicant) {
      applicationApi.put(`/${applicant.applicationId}/status`, {
        status: applicant.status
      })
        .then(() => {
          console.log("Status updated:", applicant);
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
.applicants-page {
  min-height: 100vh;
  background-color: #FFF9F3;
  padding: 3rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* Title */
.title {
  font-size: 42px;
  color: #305669;
  margin-bottom: 2rem;
}

/* Table container */
.applicants-table {
  width: 850px;
}

/* Row layout */
.table-row {
  display: grid;
  grid-template-columns: 2fr 1.5fr 1fr 1.3fr;
  padding: 1.1rem 1rem;
  background: white;
  border-radius: 12px;
  margin-bottom: 1rem;
  box-shadow: 0 1px 3px rgba(0,0,0,0.06);
}

.header {
  font-weight: 600;
  color: #305669;
  background: none;
  box-shadow: none;
  margin-bottom: 0.3rem;
}

/* Stars */
.stars span {
  font-size: 20px;
}

/* Resume Button */
.resume-btn {
  padding: 6px 14px;
  background-color: #F5F1EE;
  border: 2px solid #E5DFDA;
  border-radius: 10px;
  cursor: pointer;
  color: #305669;
  font-size: 14px;
}

.resume-btn:hover {
  background-color: #EDE7E4;
}

/* Status dropdown */
.status-select {
  padding: 6px 10px;
  border-radius: 10px;
  border: 2px solid #E5DFDA;
  font-size: 14px;
  color: #305669;
  cursor: pointer;
}
</style>
