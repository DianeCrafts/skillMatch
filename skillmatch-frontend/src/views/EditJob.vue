<template>
  <div class="edit-page">

    <h1 class="title">Edit Job</h1>

    <!-- Form Card -->
    <div class="form-card" v-if="jobLoaded">
      <JobForm
        :job="job"
        buttonText="Save Changes"
        @formSubmit="updateJob"
      />
    </div>

    <!-- Loading -->
    <div v-else class="loading-text">
      Loading job...
    </div>

  </div>
</template>

<script>
import JobForm from "@/components/JobForm.vue";
import jobsApi from "@/apis/jobApi.js";

export default {
  components: { JobForm },

  data() {
    return {
      jobLoaded: false,
      jobId: null,

      job: {
        title: "",
        description: "",
        requirements: [],
        location: "",
        salary: "",
        experience: "",
        remote: false,
        skills: []
      }
    };
  },

  mounted() {
    this.jobId = this.$route.query.id;
    this.fetchJob();
  },

  methods: {
    fetchJob() {
      jobsApi
        .get(`/${this.jobId}`)
        .then(res => {
          const data = res.data;

          this.job = {
            title: data.title,
            description: data.description,
            requirements: [...data.requirements],
            location: data.location,
            salary: data.salary,
            experience: data.experience,
            remote: data.remote,
            skills: [...data.skills]
          };

          this.jobLoaded = true;
        })
        .catch(err => {
          console.error("Failed to load job:", err);
          this.$toast.error("Could not load the job.");
        });
    },

    updateJob() {
      const recruiterId = localStorage.getItem("userId");

      jobsApi
        .put(`/${this.jobId}`, this.job, {
          headers: { "x-user-id": recruiterId }
        })
        .then(() => {
          this.$toast.success("Job updated successfully!");
          this.$router.push("/recruiter/my-jobs");
        })
        .catch(err => {
          console.error("Failed to update job:", err);
          this.$toast.error("Could not update job.");
        });
    }
  }
};
</script>

<style scoped>
/* ------------------------------
   PAGE STRUCTURE
------------------------------ */
.edit-page {
  min-height: 100vh;
  background: var(--color-bg);

  padding: 3rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;

  animation: fadeIn 0.4s ease;
}

/* ------------------------------
   TITLE
------------------------------ */
.title {
  font-size: 42px;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 2rem;
}

/* ------------------------------
   FORM CARD
------------------------------ */
.form-card {
  width: 700px;

  background: var(--color-white);
  border-radius: 18px;

  padding: 2.8rem 3rem;

  box-shadow: 0px 10px 25px rgba(0, 0, 0, 0.08);
  border: 1px solid var(--color-border);

  animation: fadeInUp 0.45s ease;
}

/* ------------------------------
   LOADING TEXT
------------------------------ */
.loading-text {
  font-size: 20px;
  color: var(--color-primary);
  margin-top: 30px;
  opacity: 0.7;
}

/* ------------------------------
   ANIMATIONS
------------------------------ */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to   { opacity: 1; transform: translateY(0); }
}
</style>
