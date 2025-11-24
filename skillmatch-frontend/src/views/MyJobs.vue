<template>
  <div class="myjobs-page">

    <h1 class="title">My Jobs</h1>

    <div class="jobs-table">

      <!-- TABLE HEADER -->
      <div class="table-row header">
        <div class="col">Job Title</div>
        <div class="col"># Applicants</div>
        <div class="col">Status</div>
        <div class="col">Action</div>
      </div>

      <!-- JOB ROWS -->
      <div
        class="table-row"
        v-for="job in jobs"
        :key="job.id"
        >
        <div class="col">{{ job.title }}</div>
        <div class="col">{{ job.applicants }}</div>
        <div class="col">{{ job.status }}</div>

        <div class="col action-buttons">

            <!-- Applicants button -->
            <button 
            class="btn applicants"
            @click="viewApplicants(job.id)"
            >
            Applicants
            </button>

            <!-- Edit/View -->
            <button
            class="btn"
            :class="job.status === 'Open' ? 'edit' : 'view'"
            @click="goToJob(job.id)"
            >
            {{ job.status === 'Closed' ? 'View' : 'Edit' }}
            </button>

            <!-- Delete -->
            <button class="btn delete" @click="deleteJob(job.id)">
            Delete
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
      jobs: [
        { id: 1, title: "Software Engineer", applicants: 12, status: "Open" },
        { id: 2, title: "Marketing Manager", applicants: 8, status: "Open" },
        { id: 3, title: "UI/UX Designer", applicants: 5, status: "Closed" },
        { id: 4, title: "Data Analyst", applicants: 15, status: "Open" }
      ]
    };
  },

  methods: {
    goToJob(id) {
      // Future edit page
      alert("Edit job " + id + " (mock)");
    },

    viewApplicants(id) {
      this.$router.push(`/recruiter/applicants/${id}`);
    },

    deleteJob(id) {
      this.jobs = this.jobs.filter(j => j.id !== id);
      alert("Job deleted (mock)");
    }
  }
};
</script>


<style scoped>
.myjobs-page {
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
.jobs-table {
  width: 750px;
}

/* Row layout */
.table-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1.5fr;
  padding: 1rem;
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
  padding-bottom: 0.5rem;
  margin-bottom: 0.3rem;
}

.col {
  display: flex;
  align-items: center;
  color: #305669;
}

/* Action buttons inside last column */
.action-buttons {
  display: flex;
  gap: 0.6rem;
}

/* Buttons */
.btn {
  padding: 0.4rem 1rem;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  font-weight: 500;
  color: white;
}

/* Edit/View button */
.edit {
  background-color: #C1785A;
}

.view {
  background-color: #305669;
}

/* Delete button */
.delete {
  background-color: #a33c3c;
}

.applicants {
  background-color: #305669;
}

</style>
