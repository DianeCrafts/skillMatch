<template>
  <div class="details-page">

    <div class="details-card">

      <!-- JOB TITLE + COMPANY -->
      <h1 class="job-title">{{ job.title }}</h1>
      <h3 class="company">{{ job.company }}</h3>

      <div class="divider"></div>

      <!-- Description -->
      <h2 class="section-title">Description</h2>
      <p class="description-text">
        {{ job.description }}
      </p>

      <!-- Skills -->
      <h2 class="section-title">Required Skills</h2>

      <div class="skills">
        <span v-for="tag in job.skills" :key="tag" class="skill-tag">
          {{ tag }}
        </span>
      </div>

      <!-- Upload resume link -->
      <p class="upload-resume" @click="$router.push('/upload-resume')">
        Upload Resume
      </p>

      <!-- Apply Button -->
      <button class="apply-btn" @click="applyToJob">
        Apply Now
      </button>

      <!-- Save job -->
      <button class="save-btn" @click="toggleSave">
        {{ saved ? "♥ Saved" : "♡ Save" }}
      </button>

    </div>

  </div>
</template>

<script>
export default {
  props: ["id"],

  data() {
    return {
      saved: false,

      // Example job — will be replaced by backend later
      job: {
        id: null,
        title: "",
        company: "",
        description: "",
        skills: []
      }
    };
  },

  created() {
    this.loadJobDetails();
  },

  methods: {
    loadJobDetails() {
      const jobId = this.id;

      // FUTURE BACKEND CALL:
      // const res = await axios.get(`/api/jobs/${jobId}`);
      // this.job = res.data;

      // Temporary mock data:
      const mockJobs = [
        {
          id: 1,
          title: "Software Engineer",
          company: "Tech Solutions",
          description:
            "Design, develop, and implement software applications. Collaborate with cross-functional teams; ensure code quality, performance, and scalability. Troubleshoot issues and debug applications.",
          skills: ["Python", "SQL"]
        },
        {
          id: 2,
          title: "Marketing Manager",
          company: "Creative Agency",
          description:
            "Lead marketing campaigns. Develop strategies to increase engagement and brand reach.",
          skills: ["SEO", "Communication"]
        }
      ];

      this.job = mockJobs.find(j => j.id == jobId) || mockJobs[0];
    },

    applyToJob() {
      console.log("Applying to job", this.job);

      // FUTURE:
      // await axios.post("/api/jobs/apply", { jobId: this.job.id });

      alert("Applied! (mock)");
    },

    toggleSave() {
      this.saved = !this.saved;

      // FUTURE BACKEND:
      // if (this.saved) await axios.post('/api/jobs/save', { jobId: this.job.id });
      // else await axios.delete('/api/jobs/save/' + this.job.id);
    }
  }
};
</script>

<style scoped>
.details-page {
  min-height: 100vh;
  background-color: #FFF9F3;
  display: flex;
  justify-content: center;
  padding: 3rem 2rem;
}

.details-card {
  width: 650px;
  background: white;
  border-radius: 16px;
  padding: 2.5rem 3rem;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}

.job-title {
  font-size: 36px;
  color: #305669;
  margin-bottom: 0.5rem;
}

.company {
  font-size: 20px;
  color: #305669;
  margin-bottom: 1.5rem;
}

.divider {
  height: 1px;
  background: #e4e4e4;
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 20px;
  color: #305669;
  font-weight: 600;
  margin-bottom: 0.6rem;
}

.description-text {
  color: #305669;
  line-height: 1.5;
  margin-bottom: 1.8rem;
}

.skills {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}

.skill-tag {
  background-color: #305669;
  color: white;
  padding: 3px 10px;
  border-radius: 6px;
  font-size: 14px;
}

.upload-resume {
  color: #305669;
  text-decoration: underline;
  cursor: pointer;
  margin-bottom: 1.8rem;
}

.apply-btn {
  width: 100%;
  background-color: #C1785A;
  color: white;
  padding: 0.9rem;
  border: none;
  border-radius: 10px;
  font-size: 18px;
  cursor: pointer;
  margin-bottom: 1rem;
}

.apply-btn:hover {
  opacity: 0.9;
}

.save-btn {
  background: none;
  border: none;
  color: #305669;
  font-size: 18px;
  cursor: pointer;
}

.save-btn:hover {
  opacity: 0.6;
}
</style>
