<template>
  <form class="form" @submit.prevent="$emit('formSubmit')">

    <!-- Job Title -->
    <div class="form-group">
      <label>Job Title</label>
      <input type="text" class="input" v-model="job.title" />
    </div>

    <!-- Description -->
    <div class="form-group">
      <label>Description</label>
      <textarea class="textarea" v-model="job.description"></textarea>
    </div>

    <!-- Requirements -->
    <div class="form-group">
      <label>Requirements (press Enter to add)</label>

      <div class="list-input-row">
        <input
          type="text"
          class="input"
          v-model="newRequirement"
          @keyup.enter.prevent="addRequirement"
        />
        <button type="button" class="add-mini-btn" @click="addRequirement">+</button>
      </div>

      <div class="chip-container">
        <div
          v-for="(req, i) in job.requirements"
          :key="'req-' + i"
          class="chip"
        >
          {{ req }}
          <span class="remove-chip" @click="removeRequirement(i)">✕</span>
        </div>
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
    <div class="form-group">
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
    </div>

    <!-- Remote Option -->
    <label class="checkbox-label">
      <input type="checkbox" v-model="job.remote" />
      Remote Position
    </label>

    <!-- Skills -->
    <div class="form-group">
      <label>Skills (press Enter to add)</label>

      <div class="list-input-row">
        <input
          type="text"
          class="input"
          v-model="newSkill"
          @keyup.enter.prevent="addSkill"
        />
        <button type="button" class="add-mini-btn" @click="addSkill">+</button>
      </div>

      <div class="chip-container">
        <div
          v-for="(skill, i) in job.skills"
          :key="'skill-' + i"
          class="chip"
        >
          {{ skill }}
          <span class="remove-chip" @click="removeSkill(i)">✕</span>
        </div>
      </div>
    </div>

    <button type="submit" class="submit-btn">
      {{ buttonText }}
    </button>

  </form>
</template>

<script>
export default {
  props: {
    job: Object,
    buttonText: String
  },
  data() {
    return {
      newSkill: "",
      newRequirement: ""
    };
  },

  methods: {
    addRequirement() {
      if (!this.newRequirement.trim()) return;
      this.job.requirements.push(this.newRequirement.trim());
      this.newRequirement = "";
    },
    removeRequirement(i) {
      this.job.requirements.splice(i, 1);
    },

    addSkill() {
      if (!this.newSkill.trim()) return;
      this.job.skills.push(this.newSkill.trim());
      this.newSkill = "";
    },
    removeSkill(i) {
      this.job.skills.splice(i, 1);
    }
  }
};
</script>

<style scoped>
/* GLOBAL FIX: border-box everywhere */
*,
*::before,
*::after {
  box-sizing: border-box;
}

/* -----------------------------
   FORM LAYOUT
------------------------------ */
.form {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 1.6rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

/* -----------------------------
   LABELS
------------------------------ */
label {
  color: var(--color-primary);
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 6px;
}

/* -----------------------------
   INPUTS + SELECT
------------------------------ */
.input,
select.input {
  width: 100%;
  border: 2px solid var(--color-primary);
  border-radius: 10px;
  padding: 0.75rem 1rem;
  font-size: 16px;
  background: white;
  outline: none;
  transition: 0.2s ease;
}

.input:focus,
select.input:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px rgb(138 190 185 / 35%);
}

/* -----------------------------
   TEXTAREA
------------------------------ */
.textarea {
  width: 100%;
  height: 120px;
  resize: vertical;

  border: 2px solid var(--color-primary);
  border-radius: 10px;
  padding: 0.8rem 1rem;
  font-size: 16px;
  transition: 0.2s ease;
}

.textarea:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px rgb(138 190 185 / 35%);
}

/* -----------------------------
   TWO-COLUMN LAYOUT
------------------------------ */
.two-columns {
  display: flex;
  gap: 2rem;
}
.column {
  flex: 1;
}

/* -----------------------------
   LIST INPUT ROW
------------------------------ */
.list-input-row {
  display: flex;
  gap: 10px;
  align-items: center;
}

/* -----------------------------
   CHIPS
------------------------------ */
.chip-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.chip {
  background: var(--color-accent);
  color: var(--color-primary);
  font-weight: 600;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 14px;

  display: flex;
  align-items: center;
  gap: 6px;

  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}

.remove-chip {
  cursor: pointer;
  font-weight: bold;
  color: var(--color-primary);
}

/* -----------------------------
   ADD-MINI BUTTON
------------------------------ */
.add-mini-btn {
  background: var(--color-accent);
  color: white;

  border: none;
  padding: 0.6rem 1rem;
  border-radius: 10px;

  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.15s ease;
}

.add-mini-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

/* -----------------------------
   SUBMIT BUTTON
------------------------------ */
.submit-btn {
  width: 100%;
  background-color: var(--color-warm);
  color: white;

  font-size: 18px;
  padding: 1rem;
  border-radius: 12px;
  border: none;

  cursor: pointer;
  transition: 0.2s ease;
  margin-top: 0.6rem;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0,0,0,0.12);
}
</style>
