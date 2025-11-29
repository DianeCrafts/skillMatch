<template>
  <form class="form" @submit.prevent="$emit('formSubmit')">

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

    <!-- Skills -->
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

    <button type="submit" class="submit-btn">{{ buttonText }}</button>

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
.form {
  width: 100%;
  display: flex;
  flex-direction: column;
}

.form * {
  box-sizing: border-box;
}

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
  background: white;
  outline: none;
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
}

/* TWO COLUMNS */
.two-columns {
  display: flex;
  gap: 2rem;
  margin-bottom: 1.2rem;
}
.column {
  flex: 1;
}

/* CHIP ROW */
.list-input-row {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 1rem;
}

.chip-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 1.2rem;
}

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
}

/* BUTTONS */
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

.submit-btn {
  width: 100%;
  background-color: var(--color-warm);
  color: white;
  font-size: 18px;
  padding: 0.9rem;
  border-radius: 10px;
  border: none;
  cursor: pointer;
  margin-top: 1rem;
}
</style>
