<template>
  <div class="applicants-page">
    <h1 class="title">Job Applicants</h1>

    <div class="applicants-table">

      <!-- Header -->
      <div class="table-row header">
        <div class="col">Applicant</div>
        <div class="col">Resume</div>
        <div class="col">Rating</div>
        <div class="col">Status</div>
      </div>

      <!-- Applicant Rows -->
      <div
        class="table-row"
        v-for="applicant in applicants"
        :key="applicant.id"
      >
        <!-- Name -->
        <div class="col">{{ applicant.name }}</div>

        <!-- Resume -->
        <div class="col">
          <button class="resume-btn" @click="viewResume(applicant)">
            View Resume
          </button>
        </div>

        <!-- Stars -->
        <div class="col stars">
          <span v-for="n in applicant.rating" :key="n">⭐</span>
        </div>

        <!-- Status Dropdown -->
        <div class="col">
          <select class="status-select" v-model="applicant.status" @change="updateStatus(applicant)">
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
export default {
  data() {
    return {
      applicants: [
        { id: 1, name: "Jane Doe", rating: 5, status: "Reviewed" },
        { id: 2, name: "John Smith", rating: 4, status: "Shortlisted" },
        { id: 3, name: "Sarah Brown", rating: 5, status: "Rejected" },
        { id: 4, name: "Michael Johnson", rating: 5, status: "Reviewed" }
      ]
    };
  },

  methods: {
    viewResume(applicant) {
      console.log("Viewing resume for", applicant);

      // FUTURE BACKEND:
      // window.open(applicant.resumeUrl, "_blank");

      alert("Resume popup coming soon! (mock)");
    },

    updateStatus(applicant) {
      console.log("Updated status:", applicant);

      // FUTURE:
      // await axios.post(`/api/applicants/${applicant.id}/status`, { status: applicant.status });
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
