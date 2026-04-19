<template>
  <div class="myjobs-page">

    <h1 class="title">My Jobs</h1>

    <div class="jobs-table">

      <!-- TABLE HEADER -->
      <div class="table-row header">
        <div class="col">Job Title</div>
        <div class="col">Status</div>
        <div class="col">Action</div>
      </div>

      <!-- JOB ROWS -->
      <div
        class="table-row job-row"
        v-for="job in jobs"
        :key="job.id"
      >
        <div class="col">{{ job.title }}</div>
        <div class="col">{{ job.status }}</div>

        <div class="col action-buttons">

          <button 
            class="btn applicants"
            @click="viewApplicants(job.id)"
          >
            Applicants
          </button>

          <button
            class="btn edit-btn"
            :class="job.status === 'Open' ? 'edit' : 'view'"
            @click="goToJob(job.id)"
          >
            {{ job.status === 'Closed' ? 'View' : 'Edit' }}
          </button>

          <button class="btn delete" @click="deleteJob(job.id)">
            Delete
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
      jobs: []
    };
  },

  mounted() {
    this.fetchJobs();
  },

  methods: {
    fetchJobs() {
      const recruiterId = localStorage.getItem("userId");

      jobsApi.get("/recruiter", {
        headers: {
          "x-user-id": recruiterId,
          accept: "*/*",
          "content-type": "application/json"
        }
      })
      .then(res => {
        this.jobs = res.data.map(job => ({
          id: job.id,
          title: job.title,
          applicants: job.applicants ?? 0,
          status: job.status ?? "Open"
        }));
      })
      .catch(err => {
        console.error("Failed to load jobs:", err);
        this.$toast.error("Could not load your jobs.");
      });
    },

    goToJob(id) {
      this.$router.push({ path: "/edit-job", query: { id } });
    },

    viewApplicants(id) {
      this.$router.push(`/recruiter/applicants/${id}`);
    },

    deleteJob(id) {
      const recruiterId = localStorage.getItem("userId");

      if (!confirm("Are you sure you want to delete this job?")) return;

      jobsApi.delete(`/${id}`, {
        headers: {
          "x-user-id": recruiterId,
          accept: "*/*",
          "content-type": "application/json"
        }
      })
      .then(() => {
        this.jobs = this.jobs.filter(j => j.id !== id);
        this.$toast.success("Job deleted successfully.");
      })
      .catch(err => {
        console.error("Failed to delete job:", err);
        this.$toast.error("Could not delete the job.");
      });
    }
  }
};
</script>

<style scoped>
/* ------------------------------
   PAGE LAYOUT
--------------------------------*/
.myjobs-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 3rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* Title */
.title {
  font-size: 42px;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 2.5rem;
}

/* ------------------------------
   TABLE WRAPPER
--------------------------------*/
.jobs-table {
  width: 750px;
}

/* Base row layout */
.table-row {
  display: grid;
  grid-template-columns: 1.3fr 0.9fr 1.3fr;
  padding: 1.3rem 1.5rem;
  align-items: center;
}

/* Header */
.header {
  background: var(--color-bg);
  color: var(--color-primary);
  font-weight: 700;
  border-bottom: 2px solid var(--color-border);
  margin-bottom: 1rem;
}

/* Individual job rows */
.job-row {
  background: white;
  color: var(--color-primary);
  border-radius: 14px;
  margin-bottom: 1.2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  transition: 0.2s ease;
}

.job-row:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
}

.col {
  display: flex;
  align-items: center;
}

/* ------------------------------
   ACTION BUTTON GROUP
--------------------------------*/
.action-buttons {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-start;
}

/* Button base */
.btn {
  padding: 0.55rem 1.2rem;
  border-radius: 8px;
  border: none;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  color: var(--color-white);
  transition: 0.2s ease;
}

/* Edit / View */
.edit {
  background: var(--color-warm);
}

.view {
  background: var(--color-primary);
}

/* Applicants */
.applicants {
  background: var(--color-accent);
  color: var(--color-primary);
}

/* Delete */
.delete {
  background: var(--color-red);
}

/* Hover effect */
.btn:hover {
  opacity: 0.92;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
</style>
