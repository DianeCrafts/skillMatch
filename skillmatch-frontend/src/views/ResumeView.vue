<template>
  <div class="resume-page">

    <h1 class="title">Applicant Resume</h1>

    <div v-if="loading" class="loading">Loading resume...</div>

    <div v-else class="resume-card">

      <!-- Header -->
      <div class="resume-header">
        <h2 class="name">{{ resume.name }}</h2>
        <p class="info">{{ resume.email }} • {{ resume.phone }}</p>
      </div>

      <!-- Summary -->
      <section class="section">
        <h3 class="section-title">Summary</h3>
        <p class="summary-text">{{ resume.summary }}</p>
      </section>

      <!-- Education -->
      <section class="section" v-if="resume.education?.length">
        <h3 class="section-title">Education</h3>

        <div v-for="(edu, i) in resume.education" :key="i" class="item-card">
          <strong>{{ edu.degree }} - {{ edu.field }}</strong>
          <p>{{ edu.institution }}</p>
          <span class="date">{{ edu.startDate }} → {{ edu.endDate || "Present" }}</span>
        </div>
      </section>

      <!-- Experience -->
      <section class="section" v-if="resume.experience?.length">
        <h3 class="section-title">Experience</h3>

        <div v-for="(exp, i) in resume.experience" :key="i" class="item-card">
          <strong>{{ exp.position }} - {{ exp.company }}</strong>
          <p>{{ exp.description }}</p>
          <span class="date">{{ exp.startDate }} → {{ exp.endDate || "Present" }}</span>
        </div>
      </section>

      <!-- Skills -->
      <section class="section" v-if="resume.skills?.length">
        <h3 class="section-title">Skills</h3>
        <div class="skills">
          <span class="skill-chip" v-for="(skill, i) in resume.skills" :key="i">
            {{ skill }}
          </span>
        </div>
      </section>

    </div>

  </div>
</template>

<script>
import resumeApi from "@/apis/resumeApi.js";

export default {
  data() {
    return {
      loading: true,
      resume: {}
    };
  },

  mounted() {
    const resumeId = this.$route.params.id;
    this.fetchResume(resumeId);
  },

  methods: {
    fetchResume(id) {
      resumeApi.get(`/resumes/${id}`)
        .then(res => {
          this.resume = res.data;
          this.loading = false;
        })
        .catch(err => {
          console.error("Failed to load resume:", err);
          alert("Could not load resume.");
        });
    }
  }
};
</script>

<style scoped>
.resume-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 3rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.title {
  font-size: 42px;
  color: var(--color-primary);
  margin-bottom: 2.2rem;
  font-weight: 700;
}

.loading {
  font-size: 20px;
  color: var(--color-primary);
  margin-top: 2rem;
}

.resume-card {
  width: 750px;
  background: white;
  border-radius: 16px;
  padding: 2.5rem 3rem;
  box-shadow: 0px 10px 25px rgba(0,0,0,0.08);
  border: 1px solid var(--color-border);
}

.resume-header {
  text-align: center;
  margin-bottom: 2rem;
}

.name {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-primary);
}

.info {
  color: #5f7b89;
  margin-top: 6px;
}

.section {
  margin-bottom: 2rem;
}

.section-title {
  font-size: 22px;
  font-weight: 600;
  color: var(--color-primary);
  margin-bottom: 1rem;
}

.summary-text {
  line-height: 1.5;
  color: var(--color-primary);
}

.item-card {
  background: #f7f4f2;
  padding: 1rem 1.2rem;
  border-radius: 12px;
  margin-bottom: 1rem;
  border: 1px solid var(--color-border);
}

.date {
  color: #6b818c;
  font-size: 14px;
}

.skills {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.skill-chip {
  background: var(--color-accent);
  padding: 6px 12px;
  border-radius: 20px;
  color: var(--color-primary);
  font-weight: 600;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}
</style>
