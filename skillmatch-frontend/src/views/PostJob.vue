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
/* ------------------------------
   PAGE LAYOUT
------------------------------ */
.post-page {
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

  box-shadow: 0px 10px 25px rgba(0,0,0,0.08);
  border: 1px solid var(--color-border);

  animation: fadeInUp 0.45s ease;
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
