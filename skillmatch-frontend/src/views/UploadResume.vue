<template>
  <div class="upload-page">

    <div class="upload-card">

      <h2 class="title">Upload Resume</h2>

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
        <label>Uploaded resume:</label>
        <input type="text" :value="uploadedFileName" class="uploaded-input" disabled />
      </div>

      <button class="replace-btn" @click="replaceFile">Replace Resume</button>

    </div>

  </div>
</template>

<script>
export default {
  data() {
    return {
      uploadedFileName: "resume.pdf" // Example placeholder
    };
  },

  methods: {
    handleFile(event) {
      const file = event.target.files[0];
      if (file) this.uploadedFileName = file.name;
    },

    handleDrop(event) {
      const file = event.dataTransfer.files[0];
      if (file) this.uploadedFileName = file.name;
    },

    replaceFile() {
      alert("Resume replaced (placeholder)");
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
  width: 480px;
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

.uploaded-section {
  text-align: left;
  margin-bottom: 1.4rem;
}

.uploaded-input {
  width: 100%;
  background: #FFF4E8;
  border: 1px solid #E0C7B8;
  padding: 0.7rem;
  border-radius: 8px;
  margin-top: 0.3rem;
}

.replace-btn {
  width: 100%;
  background-color: #C1785A;
  color: white;
  font-size: 18px;
  padding: 0.9rem;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.replace-btn:hover {
  opacity: 0.95;
}
</style>
