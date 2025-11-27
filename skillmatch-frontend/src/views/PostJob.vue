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
        <label>Requirements (press Enter to add)</label>
        <div class="list-input-row">
          <input
            type="text"
            class="input"
            placeholder="e.g. Strong communication"
            v-model="newRequirement"
            @keyup.enter.prevent="addRequirement"
          />
          <button type="button" class="add-mini-btn" @click="addRequirement">+</button>
        </div>

        <div class="chip-container">
          <div v-for="(req, index) in job.requirements" :key="'req-' + index" class="chip">
            {{ req }}
            <span class="remove-chip" @click="removeRequirement(index)">✕</span>
          </div>
        </div>

        <!-- Location + Salary -->
        <div class="two-columns">
          <div class="column">
            <label>Location</label>
            <input type="text" class="input" v-model="job.location" />
          </div>

          <div class="column">
            <label>Salary</label>
            <input type="text" class="input" v-model="job.salary" />
          </div>
        </div>

        <!-- Experience Level -->
        <label>Experience Level</label>
        <select class="input" v-model="job.experience">
          <option disabled value="">Select experience level</option>
          <option>Internship</option>
          <option>Entry Level</option>
          <option>Junior</option>
          <option>Mid-Level</option>
          <option>Senior</option>
          <option>Lead</option>
        </select>

        <!-- Remote Option -->
        <label class="checkbox-label">
          <input type="checkbox" v-model="job.remote" />
          Remote Position
        </label>

        <!-- Skills Required -->
        <label>Skills (press Enter to add)</label>
        <div class="list-input-row">
          <input
            type="text"
            class="input"
            placeholder="e.g. JavaScript"
            v-model="newSkill"
            @keyup.enter.prevent="addSkill"
          />
          <button type="button" class="add-mini-btn" @click="addSkill">+</button>
        </div>

        <div class="chip-container">
          <div v-for="(skill, index) in job.skillsRequired" :key="'skill-' + index" class="chip">
            {{ skill }}
            <span class="remove-chip" @click="removeSkill(index)">✕</span>
          </div>
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
import jobsApi from "@/apis/jobApi.js"; 
export default {
  
  data() {
    return {
      newSkill: "",
      newRequirement: "",
      job: {
        title: "",
        description: "",
        requirements: [],
        location: "",
        salary: "",
        experience: "",
        remote: false,
        skillsRequired: []  // must match backend DTO
      }
    };
  },

  methods: {
    addRequirement() {
      if (!this.newRequirement.trim()) return;
      this.job.requirements.push(this.newRequirement.trim());
      this.newRequirement = "";
    },
    removeRequirement(index) {
      this.job.requirements.splice(index, 1);
    },
    addSkill() {
      if (!this.newSkill.trim()) return;
      this.job.skillsRequired.push(this.newSkill.trim());
      this.newSkill = "";
    },
    removeSkill(index) {
      this.job.skillsRequired.splice(index, 1);
    },
    submitJob() {
      const recruiterId = localStorage.getItem("userId");

      if (!recruiterId) {
        alert("Error: recruiter not logged in.");
        return;
      }

      const payload = {
        title: this.job.title,
        description: this.job.description,
        skillsRequired: this.job.skillsRequired,
        location: this.job.location,
        remote: this.job.remote
      };

      jobsApi.post("/", payload, {
        headers: {
          "X-User-Id": recruiterId
        }
      })
        .then(res => {
          console.log("Job created:", res.data);
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
  box-sizing: border-box;
}

/* TITLE */
.title {
  font-size: 42px;
  color: var(--color-primary);
  margin-bottom: 2rem;
  text-align: center;
}

/* FORM CARD */
.form-card {
  width: 700px;
  background: white;
  border: 2px solid var(--color-primary);
  border-radius: 16px;
  padding: 2.5rem 3rem;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
  box-sizing: border-box;

  /* FIX center alignment of everything */
  display: flex;
  justify-content: center;
}

/* FORM */
.form {
  width: 100%;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
}

/* LABELS */
.form label {
  color: var(--color-primary);
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 6px;
}

/* INPUTS */
.input,
select.input {
  width: 100%;
  border: 2px solid var(--color-primary);
  border-radius: 10px;
  padding: 0.7rem 1rem;
  margin-bottom: 1.2rem;
  font-size: 16px;
  outline: none;
  box-sizing: border-box;
  background: white;
}

.input:focus,
select.input:focus {
  border-color: var(--color-accent);
}

/* TEXTAREA */
.textarea {
  width: 100%;
  height: 120px;
  border: 2px solid var(--color-primary);
  border-radius: 10px;
  padding: 0.8rem 1rem;
  margin-bottom: 1.2rem;
  resize: vertical;
  box-sizing: border-box;
}

/* TWO COLUMNS */
.two-columns {
  display: flex;
  gap: 2rem;
  margin-bottom: 1.2rem; /* consistency fix */
}

.column {
  flex: 1;
}

/* CHIP INPUT ROW */
.list-input-row {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 1rem; /* FIX spacing */
}

/* CHIP CONTAINER */
.chip-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 1.2rem;
}

/* CHIP */
.chip {
  background: var(--color-accent);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 14px;
  display: flex;
  align-items: center;
}

.remove-chip {
  cursor: pointer;
  margin-left: 8px;
  font-weight: bold;
}

/* SMALL ADD BUTTON */
.add-mini-btn {
  background: var(--color-accent);
  color: white;
  border: none;
  padding: 0.6rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  font-size: 18px;
  font-weight: bold;
}

.add-mini-btn:hover {
  opacity: 0.9;
}

/* SUBMIT BUTTON */
.submit-btn {
  width: 100%;
  background-color: var(--color-warm);
  color: white;
  font-size: 18px;
  font-weight: 600;
  padding: 0.9rem;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  margin-top: 1.4rem;
}

.submit-btn:hover {
  opacity: 0.9;
}
</style>
