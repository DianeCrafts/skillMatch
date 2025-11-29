<template>
  <div class="post-page">
    <h1 class="title">Post a Job</h1>

    <div class="form-card">
      <JobForm
        :job="job"
        buttonText="Post Job"
        @submit="submitJob"
      />
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
    console.log("POST JOB MOUNTED");
  },
  methods: {
    submitJob() {
      const recruiterId = localStorage.getItem("userId");

      if (!recruiterId) {
        alert("You must be logged in as recruiter.");
        return;
      }

      jobsApi.post("", this.job, {
        headers: { "x-user-id": recruiterId }
      })
      .then(() => {
        alert("Job posted successfully!");
        this.$router.push("/recruiter-dashboard");
      })
      .catch(err => {
        console.error("Error posting job:", err);
        alert("Failed to post job.");
      });
    }
  }
};
</script>


<style scoped>
/* PAGE BACKGROUND */
.post-page {
  min-height: 100vh;
  background-color: var(--color-bg);
  padding: 3rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* TITLE */
.title {
  font-size: 42px;
  color: var(--color-primary);
  margin-bottom: 2rem;
}

/* FORM CARD */
.form-card {
  width: 700px;
  background: white;
  border: 2px solid var(--color-primary);
  border-radius: 16px;
  padding: 2.5rem 3rem;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}
</style>
