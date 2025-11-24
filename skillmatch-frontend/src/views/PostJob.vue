<template>
  <div class="post-page">

    <h1 class="title">Post a Job</h1>

    <div class="form-card">

      <form class="form">

        <!-- Job Title -->
        <label>Job Title</label>
        <input type="text" class="input" v-model="job.title" />

        <!-- Description -->
        <label>Description</label>
        <textarea class="textarea" v-model="job.description"></textarea>

        <!-- Requirements -->
        <label>Requirements</label>
        <textarea class="textarea" v-model="job.requirements"></textarea>

        <!-- Location + Salary -->
        <div class="two-columns">
          <div>
            <label>Location</label>
            <input type="text" class="input" v-model="job.location" />
          </div>

          <div>
            <label>Salary</label>
            <input type="text" class="input" v-model="job.salary" />
          </div>
        </div>

        <!-- Experience + Skills -->
        <div>
          <label>Experience Level</label>
          <input type="text" class="input" v-model="job.experience" />
        </div>

        <div>
          <label>Skills</label>
          <input type="text" class="input" v-model="job.skills" />
        </div>

        <!-- Submit -->
        <button class="submit-btn" @click.prevent="submitJob">
          Post Job
        </button>

      </form>

    </div>

  </div>
</template>

<script>
export default {
  data() {
    return {
      job: {
        title: "",
        description: "",
        requirements: "",
        location: "",
        salary: "",
        experience: "",
        skills: ""
      }
    };
  },

  methods: {
    submitJob() {
      console.log("Job submitted:", this.job);

      // ⭐ FUTURE BACKEND CALL:
      // await axios.post("/api/recruiter/jobs", this.job);

      alert("Job posted! (Mock)");

      this.$router.push("/recruiter-dashboard");
    }
  }
};
</script>

<style scoped>
/* Page background */
.post-page {
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

/* Card container */
.form-card {
  width: 700px;
  background: white;
  border-radius: 16px;
  padding: 2.5rem 3rem;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}

/* Labels */
.form label {
  color: #305669;
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 6px;
  display: block;
}

/* Text input */
.input {
  width: 100%;
  border: 2px solid #E0DAD4;
  border-radius: 10px;
  padding: 0.7rem 1rem;
  margin-bottom: 1.2rem;
  font-size: 16px;
  outline: none;
}

.textarea {
  width: 100%;
  height: 90px;
  border: 2px solid #E0DAD4;
  border-radius: 10px;
  padding: 0.8rem 1rem;
  margin-bottom: 1.2rem;
  font-size: 16px;
  outline: none;
  resize: vertical;
}

/* Two columns layout */
.two-columns {
  display: flex;
  gap: 1.5rem;
}

/* Submit button */
.submit-btn {
  width: 100%;
  background-color: #C1785A;
  color: white;
  font-size: 18px;
  font-weight: 600;
  padding: 0.9rem;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  margin-top: 1.5rem;
}

.submit-btn:hover {
  opacity: 0.9;
}
</style>
