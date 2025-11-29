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
import jobsApi from "@/apis/jobApi.js";

export default {
  data() {
    return {
      jobs: [] // dynamic list from backend
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
          "accept": "*/*",
          "content-type": "application/json"
        }
      })
      .then(res => {
        console.log("Jobs from backend:", res.data);

        // Map backend job format to frontend table format
        this.jobs = res.data.map(job => ({
          id: job.id,
          title: job.title,
          applicants: job.applicants ?? 0,   
          status: job.status ?? "Open"       
        }));
      })
      .catch(err => {
        console.error("Failed to load jobs:", err);
        alert("Could not load your jobs.");
      });
    },

    goToJob(id) {
      this.$router.push({ path: "/edit-job", query: { id }});
    },

    viewApplicants(id) {
      this.$router.push(`/recruiter/applicants/${id}`);
    },

    deleteJob(id) {
      const recruiterId = localStorage.getItem("userId");

      if (!confirm("Are you sure you want to delete this job?")) {
        return;
      }

      jobsApi.delete(`/${id}`, {
        headers: {
          "x-user-id": recruiterId,
          "accept": "*/*",
          "content-type": "application/json"
        }
      })
      .then(() => {
        this.jobs = this.jobs.filter(j => j.id !== id);
        alert("Job deleted successfully.");
      })
      .catch(err => {
        console.error("Failed to delete job:", err);
        alert("Could not delete the job.");
      });
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

/* Table row layout */
.table-row {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1.5fr;
  padding: 1rem;
  align-items: center;   
}

/* Header row */
.header {
  background: #FFF9F3; 
  font-weight: 700;
  color: #305669;
  padding: 1rem;         
  border-bottom: 2px solid #e3d7ce;
  margin-bottom: 0.5rem;
  box-shadow: none !important;
  border-radius: 0 !important; 
}

/* Data rows */
.table-row:not(.header) {
  background: white;
  border-radius: 12px;
  margin-bottom: 1rem;
  box-shadow: 0 1px 3px rgba(0,0,0,0.06);
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
