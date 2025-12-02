<template>
  <div class="resume-page">
    <div class="resume-card">
      
      <h2 class="title">
        {{ mode === "edit" ? "Edit Your Resume" : "Complete Your Resume" }}
      </h2>

      <form class="resume-form" @submit.prevent="saveResume">

        <!-- SECTION: Summary -->
        <div class="form-section">
          <h3 class="section-title">Professional Summary</h3>
          <textarea class="input textarea" v-model="form.summary" placeholder="Write a short summary about your background and skills..."></textarea>
        </div>

        <!-- SECTION: Personal Info -->
        <div class="form-section">
          <h3 class="section-title">Personal Information</h3>

          <div class="two-columns">
            <div class="column">
              <label>Name</label>
              <input class="input" v-model="form.name" type="text" />
            </div>

            <div class="column">
              <label>Email</label>
              <input class="input" v-model="form.email" type="email" />
            </div>
          </div>

          <label>Phone</label>
          <input class="input" v-model="form.phone" type="tel" />
        </div>

        <!-- SECTION: Education -->
        <div class="form-section">
          <div class="section-header">
            <h3 class="section-title">Education</h3>
            <button type="button" class="add-btn" @click="addEducation">+ Add Education</button>
          </div>

          <div
            v-for="(edu, index) in form.education"
            :key="'edu-' + index"
            class="block-card"
          >
            <div class="two-columns">
              <div class="column">
                <label>Institution</label>
                <input class="input" v-model="edu.institution" type="text" />
              </div>

              <div class="column">
                <label>Degree</label>
                <input class="input" v-model="edu.degree" type="text" />
              </div>
            </div>

            <label>Field of Study</label>
            <input class="input" v-model="edu.field" type="text" />

            <div class="two-columns">
              <div class="column">
                <label>Start Date</label>
                <input class="input" v-model="edu.startDate" type="date" />
              </div>

              <div class="column">
                <label>End Date</label>
                <input class="input" v-model="edu.endDate" type="date" />
              </div>
            </div>

            <button type="button" class="remove-btn" @click="removeEducation(index)">
              Remove Education
            </button>
          </div>
        </div>

        <!-- SECTION: Experience -->
        <div class="form-section">
          <div class="section-header">
            <h3 class="section-title">Experience</h3>
            <button type="button" class="add-btn" @click="addExperience">+ Add Experience</button>
          </div>

          <div
            v-for="(exp, index) in form.experience"
            :key="'exp-' + index"
            class="block-card"
          >
            <label>Company</label>
            <input class="input" v-model="exp.company" type="text" />

            <label>Position</label>
            <input class="input" v-model="exp.position" type="text" />

            <div class="two-columns">
              <div class="column">
                <label>Start Date</label>
                <input class="input" v-model="exp.startDate" type="date" />
              </div>

              <div class="column">
                <label>End Date</label>
                <input class="input" v-model="exp.endDate" type="date" />
              </div>
            </div>

            <label>Description</label>
            <textarea class="input textarea" v-model="exp.description"></textarea>

            <button type="button" class="remove-btn" @click="removeExperience(index)">
              Remove Experience
            </button>
          </div>
        </div>

        <!-- SECTION: Skills -->
        <div class="form-section">
          <h3 class="section-title">Skills</h3>

          <div class="skill-input-row">
            <input
              class="input"
              type="text"
              placeholder="Type a skill and press Enter"
              v-model="newSkill"
              @keyup.enter.prevent="addSkill"
            />
            <button type="button" class="add-btn small-add" @click="addSkill">Add</button>
          </div>

          <div class="chips-container">
            <div
              v-for="(skill, index) in form.skills"
              :key="'skill-' + index"
              class="skill-chip"
            >
              {{ skill }}
              <span class="remove-chip" @click="removeSkill(index)">✕</span>
            </div>
          </div>
        </div>

        <button class="submit-btn" type="submit">
          {{ mode === "edit" ? "Save Changes" : "Save Resume" }}
        </button>

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
      try {
        const userId = localStorage.getItem("userId");
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
/* GLOBAL BOX MODEL */
*,
*::before,
*::after {
  box-sizing: border-box;
}

/* PAGE LAYOUT */
.resume-page {
  min-height: 100vh;
  background-color: var(--color-bg);
  display: flex;
  justify-content: center;
  padding: 3rem 1.5rem;
}

/* CARD */
.resume-card {
  background: white;
  width: 850px;
  border-radius: 22px;
  padding: 3rem 3.5rem;
  border: 2px solid var(--color-primary);

  box-shadow:
    0 4px 14px rgba(0, 0, 0, 0.06),
    0 2px 3px rgba(0, 0, 0, 0.03);

  animation: fadeIn 0.3s ease-out;
}

/* Fade-in animation */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(6px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* TITLE */
.title {
  text-align: center;
  font-size: 34px;
  color: var(--color-primary);
  margin-bottom: 2.5rem;
  font-weight: 700;
  letter-spacing: -0.5px;
}

/* SECTION TITLES */
.section-title {
  font-size: 20px;
  color: var(--color-primary);
  font-weight: 600;
  margin-bottom: 1.1rem;
  border-left: 4px solid var(--color-accent);
  padding-left: 10px;
}

/* FORM SECTION SPACING */
.form-section {
  margin-bottom: 2.6rem;
}

/* SECTION HEADER (Title + add button) */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

/* BLOCK CARDS (Education/Experience entries) */
.block-card {
  background: #ffffff;
  border: 2px solid var(--color-border);
  border-radius: 14px;
  padding: 1.4rem 1.5rem;
  margin-bottom: 1.5rem;

  box-shadow:
    0 2px 6px rgba(0, 0, 0, 0.06),
    inset 0 0 0 1px rgba(255, 255, 255, 0.6);

  transition: 0.2s ease;
}

.block-card:hover {
  border-color: var(--color-accent);
  box-shadow:
    0 4px 10px rgba(0,0,0,0.08),
    inset 0 0 0 1px rgba(138, 190, 185, 0.3);
}

/* INPUTS */
.input {
  width: 100%;
  padding: 0.85rem 1rem;
  border: 2px solid var(--color-primary);
  border-radius: 10px;
  font-size: 16px;
  outline: none;
  transition: 0.25s;
  background: white;
}

.input:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 4px rgba(138, 190, 185, 0.25);
}

/* TEXTAREA */
.textarea {
  resize: vertical;
  min-height: 110px;
}

/* TWO COLUMNS */
.two-columns {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 0.5rem;
}
.column {
  flex: 1;
}

/* ADD BUTTON */
.add-btn {
  background: var(--color-accent);
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 600;
  transition: 0.2s ease;
}

.add-btn:hover {
  opacity: 0.92;
  transform: translateY(-2px);
}

.small-add {
  padding: 0.6rem 0.9rem;
  margin: 1rem 0 1rem 0;
  
}

/* REMOVE BUTTON */
.remove-btn {
  background: var(--color-red);
  color: white;
  padding: 0.45rem 1rem;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  border: none;
  margin-top: 0.8rem;
  transition: 0.2s ease;
}

.remove-btn:hover {
  background: #b84040;
  transform: translateY(-2px);
}

/* SKILLS CHIPS */
.chips-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.skill-chip {
  background: var(--color-accent);
  color: white;
  padding: 7px 14px;
  border-radius: 22px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;

  box-shadow: 0 2px 5px rgba(0,0,0,0.12);
}

.remove-chip {
  cursor: pointer;
  font-weight: 700;
  font-size: 16px;
  opacity: 0.9;
}

.remove-chip:hover {
  opacity: 1;
}

/* SUBMIT BUTTON */
.submit-btn {
  width: 100%;
  background-color: var(--color-warm);
  color: white;
  padding: 1rem;
  font-size: 18px;
  border-radius: 14px;
  cursor: pointer;
  border: none;
  font-weight: 600;
  margin-top: 2rem;
  transition: 0.25s ease;
}

.submit-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 7px 18px rgba(0, 0, 0, 0.15);
}
</style>
