<template>
  <div class="upload-page">

    <div class="upload-card">

      <h2 class="title">Resume Options</h2>

      <!-- OPTION 1 — Upload Resume -->
      <div 
        class="drop-area"
        @dragover.prevent
        @drop.prevent="handleDrop"
      >
        <div class="upload-icon">⬆</div>
        <p>Drag and drop a file here or</p>

        <label class="choose-file-btn">
          Choose File
          <input type="file" class="file-input" @change="handleFile" />
        </label>
      </div>

      <p class="formats">PDF, DOC, DOCX</p>
      <div v-if="uploadedFileName" class="uploaded-section">
        <label>Selected File:</label>
        <input
          type="text"
          class="uploaded-input"
          :value="uploadedFileName"
          disabled
        />
      </div>

      <button
        class="parse-btn"
        :disabled="!selectedFile"
        @click="parseResume"
      >
        Parse Resume
      </button>

      <div class="or-box">OR</div>

      <!-- OPTION 2 — Manual entry -->
      <button
        class="manual-btn"
        @click="goToManualForm"
      >
        Enter Resume Manually
      </button>

    </div>

  </div>
</template>

<script>
import resumeApi from "@/apis/resumeApi.js";

export default {
  data() {
    return {
      selectedFile: null,
      uploadedFileName: "" 
    };
  },

  methods: {
    handleFile(event) {
      this.selectedFile = event.target.files[0];
      this.uploadedFileName = this.selectedFile?.name || "";
    },

    handleDrop(event) {
      this.selectedFile = event.dataTransfer.files[0];
      this.uploadedFileName = this.selectedFile?.name || "";
    },

    async parseResume() {
      if (!this.selectedFile) return;

      const userId = localStorage.getItem("userId");

      const formData = new FormData();
      formData.append("file", this.selectedFile);
      // formData.append("userId", userId);   // ✅ REQUIRED

      try {
        const res = await resumeApi.post("/resumes/parse", formData, {
          headers: { "Content-Type": "multipart/form-data" }
        });

        this.$router.push({
          path: "/resume-form",
          query: { parsed: JSON.stringify(res.data) }
        });

      } catch (err) {
        alert("Error parsing resume");
        console.error(err);
      }
    },


    goToManualForm() {
      this.$router.push({
        path: "/resume-form",
        query: { parsed: JSON.stringify(null) }
      });
    }
  }
};
</script>

<style scoped>
.upload-page {
  min-height: 100vh;
  background-color: #FFF9F3;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

.upload-card {
  background: white;
  border-radius: 16px;
  padding: 2.5rem 3rem;
  width: 500px;
  text-align: center;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}

.title {
  font-size: 32px;
  color: #305669;
  margin-bottom: 1.8rem;
}

.drop-area {
  border: 2px dashed #8ABEB9;
  border-radius: 12px;
  padding: 2rem;
  margin-bottom: 1.2rem;
}

.upload-icon {
  font-size: 32px;
  color: #305669;
  margin-bottom: 0.8rem;
}

.choose-file-btn {
  background-color: #305669;
  color: white;
  padding: 10px 20px;
  border-radius: 8px;
  display: inline-block;
  cursor: pointer;
  margin-top: 0.8rem;
  font-size: 16px;
}

.file-input {
  display: none;
}

.formats {
  color: #305669;
  font-size: 14px;
  margin-bottom: 1.6rem;
}

.parse-btn,
.manual-btn {
  width: 100%;
  font-size: 18px;
  padding: 0.9rem;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  margin-top: 0.8rem;
}

.parse-btn {
  background: #8ABEB9;
  color: white;
}
.parse-btn:disabled {
  background: #aacfcf;
  cursor: not-allowed;
}

.manual-btn {
  background: #C1785A;
  color: white;
}

.or-box {
  margin: 1.4rem 0;
  font-weight: bold;
  color: #305669;
}

.uploaded-section {
  text-align: left;
  margin-top: 1rem;
  margin-bottom: 1rem;
}

.uploaded-input {
  width: 100%;
  background: #FFF4E8;
  border: 1px solid #E0C7B8;
  padding: 0.7rem;
  border-radius: 8px;
  margin-top: 0.3rem;
}

</style>
