<template>
  <div class="post-page">
    <h1 class="title">Edit Job</h1>

    <div class="form-card" v-if="jobLoaded">
      <JobForm
        :job="job"
        buttonText="Save Changes"
        @formSubmit="updateJob"
      />
    </div>

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

          // fill job object
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
          alert("Could not load the job.");
        });
    },

    updateJob() {
      const recruiterId = localStorage.getItem("userId");

      jobsApi
        .put(`/${this.jobId}`, this.job, {
          headers: { "x-user-id": recruiterId }
        })
        .then(() => {
          alert("Job updated successfully!");
          this.$router.push("/recruiter/my-jobs");
        })
        .catch(err => {
          console.error("Failed to update job:", err);
          alert("Could not update job.");
        });
    }
  }
};
</script>

<style scoped>
.post-page {
  min-height: 100vh;
  background-color: var(--color-bg);
  padding: 3rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.title {
  font-size: 42px;
  color: var(--color-primary);
  margin-bottom: 2rem;
}
.form-card {
  width: 700px;
  background: white;
  border: 2px solid var(--color-primary);
  border-radius: 16px;
  padding: 2.5rem 3rem;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}
.loading-text {
  font-size: 20px;
  color: #666;
  margin-top: 30px;
}
</style>
