<template>
  <div class="resume-page">
    <div class="resume-card">

      <h2 class="title">Complete Your Resume</h2>

      <form @submit.prevent="saveResume">

        <!-- Summary -->
        <label>Summary</label>
        <textarea class="input" v-model="form.summary"></textarea>

        <!-- Personal Info -->
        <label>Name</label>
        <input class="input" v-model="form.name" type="text" />

        <label>Email</label>
        <input class="input" v-model="form.email" type="email" />

        <label>Phone</label>
        <input class="input" v-model="form.phone" type="tel" />

        <!-- Education -->
        <h3 class="section-title">Education</h3>

        <div
          v-for="(edu, index) in form.education"
          :key="'edu-' + index"
          class="edu-block"
        >
          <label>Institution</label>
          <input class="input" v-model="edu.institution" type="text"/>

          <label>Degree</label>
          <input class="input" v-model="edu.degree" type="text"/>

          <label>Field</label>
          <input class="input" v-model="edu.field" type="text"/>

          <label>Start Date</label>
          <input class="input" v-model="edu.startDate" type="date"/>

          <label>End Date</label>
          <input class="input" v-model="edu.endDate" type="date"/>

          <button type="button" class="remove-btn" @click="removeEducation(index)">
            Remove Education
          </button>
        </div>

        <button type="button" class="add-btn" @click="addEducation">
          + Add Education
        </button>


        <!-- Experience -->
        <h3 class="section-title">Experience</h3>

        <div
          v-for="(exp, index) in form.experience"
          :key="'exp-' + index"
          class="exp-block"
        >
          <label>Company</label>
          <input class="input" v-model="exp.company" type="text"/>

          <label>Position</label>
          <input class="input" v-model="exp.position" type="text"/>

          <label>Start Date</label>
          <input class="input" v-model="exp.startDate" type="date"/>

          <label>End Date</label>
          <input class="input" v-model="exp.endDate" type="date"/>

          <label>Description</label>
          <textarea class="input" v-model="exp.description"></textarea>

          <button type="button" class="remove-btn" @click="removeExperience(index)">
            Remove Experience
          </button>
        </div>

        <button type="button" class="add-btn" @click="addExperience">
          + Add Experience
        </button>


        <!-- Skills -->
        <h3 class="section-title">Skills</h3>

        <div class="skill-input-area">
          <input
            class="input"
            type="text"
            placeholder="Type a skill and press Enter"
            v-model="newSkill"
            @keyup.enter.prevent="addSkill"
          />
          <button type="button" class="add-btn small-add" @click="addSkill">
            Add Skill
          </button>
        </div>

        <div class="skills-list">
          <div
            v-for="(skill, index) in form.skills"
            :key="'skill-' + index"
            class="skill-chip"
          >
            {{ skill }}
            <span class="remove-skill" @click="removeSkill(index)">✕</span>
          </div>
        </div>


        <button class="submit-btn" type="submit">Save Resume</button>

      </form>

    </div>
  </div>
</template>

<script>
import resumeApi from "@/apis/resumeApi.js";

export default {
  data() {
    return {
      newSkill: "",
      form: {
        summary: "",
        name: "",
        email: "",
        phone: "",
        education: [],
        experience: [],
        skills: []
      }
    };
  },

  created() {
    const parsed = this.$route.query.parsed;
    const resumeId = this.$route.query.resumeId;

    // CASE 1 — Parsed resume from upload
    if (parsed) {
      const data = JSON.parse(parsed);
      this.mode = "create";
      this.form = { ...this.form, ...data };
      return;
    }

    // CASE 2 — Editing existing resume
    if (resumeId) {
      this.mode = "edit";
      this.loadExistingResume(resumeId);
      return;
    }

    // CASE 3 — Manual new resume
    this.mode = "create";
  },

  methods: {
    async loadExistingResume(userId) {
      try {
        const res = await resumeApi.get(`/resumes/user/${userId}`);
        const data = res.data;

        this.form = {
          summary: data.summary || "",
          name: data.name || "",
          email: data.email || "",
          phone: data.phone || "",
          education: data.education || [],
          experience: data.experience || [],
          skills: data.skills || []
        };

        this.existingResumeId = data.id;

      } catch (err) {
        console.error("Error loading resume", err);
        alert("Could not load resume to edit.");
      }
    },
    // Add sections
    addEducation() {
      this.form.education.push({
        institution: "",
        degree: "",
        field: "",
        startDate: "",
        endDate: ""
      });
    },

    addExperience() {
      this.form.experience.push({
        company: "",
        position: "",
        startDate: "",
        endDate: "",
        description: ""
      });
    },

    addSkill() {
      if (!this.newSkill.trim()) return;
      this.form.skills.push(this.newSkill.trim());
      this.newSkill = ""; // reset input
    },

    // Remove
    removeEducation(i) {
      this.form.education.splice(i, 1);
    },
    removeExperience(i) {
      this.form.experience.splice(i, 1);
    },
    removeSkill(i) {
      this.form.skills.splice(i, 1);
    },

    async saveResume() {
      console.log("@@@@@@@@@@@@@@@@@@@@@@")
      try {
        const userId = localStorage.getItem("userId");
        console.log(this.form)
        if (this.mode === "edit") {
          await resumeApi.put(`/resumes/${this.existingResumeId}`, this.form);
          alert("Resume updated!");
        } else {
          await resumeApi.post(`/resumes/save?userId=${userId}`, this.form);
          alert("Resume created!");
        }

        this.$router.push("/dashboard");

      } catch (err) {
        console.error(err);
        alert("Error saving resume");
      }
    }

  }
};
</script>

<style scoped>
.resume-page {
  min-height: 100vh;
  background-color: var(--color-bg);
  display: flex;
  justify-content: center;
  padding: 3rem;
}

.resume-card {
  background: white;
  padding: 2rem 3rem;
  width: 750px;
  border-radius: 20px;
  border: 2px solid var(--color-primary);
}

.title {
  text-align: center;
  color: var(--color-primary);
  font-size: 30px;
  margin-bottom: 1.5rem;
}

.section-title {
  color: var(--color-primary);
  margin-top: 1rem;
}

.input {
  width: 100%;
  border: 2px solid var(--color-primary);
  border-radius: 10px;
  padding: 0.7rem;
  margin-bottom: 1rem;
  font-size: 16px;
}

.add-btn {
  background: var(--color-accent);
  color: white;
  padding: 0.6rem 1rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  cursor: pointer;
  border: none;
}

.small-add {
  margin-left: 0.7rem;
  padding: 0.6rem 0.8rem;
}

.remove-btn {
  background: #c94e4e;
  color: white;
  padding: 0.4rem 0.8rem;
  border-radius: 6px;
  margin-top: 0.5rem;
  cursor: pointer;
  border: none;
}

.skill-input-area {
  display: flex;
  align-items: center;
}

.skills-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 1rem;
}

.skill-chip {
  background: var(--color-accent);
  color: white;
  padding: 6px 10px;
  border-radius: 20px;
  display: flex;
  align-items: center;
}

.remove-skill {
  margin-left: 8px;
  cursor: pointer;
  font-weight: bold;
}

.submit-btn {
  width: 100%;
  background-color: var(--color-warm);
  color: white;
  padding: 1rem;
  border-radius: 12px;
  font-size: 18px;
  margin-top: 2rem;
  cursor: pointer;
  border: none;
}
</style>
