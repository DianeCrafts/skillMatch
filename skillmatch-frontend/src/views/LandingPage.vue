<template>
  <div class="landing-container">

    <NavBar />

    <section class="landing-content">
      
      <!-- Text Section -->
      <div class="left-section">
        <h1 class="title">
          Find your <br /> perfect match.
        </h1>

        <button class="get-started-btn">
          Get Started
        </button>
      </div>

      <!-- Illustration -->
      <img
        src="/src/assets/landing-illustration.png"
        alt="Landing illustration"
        class="illustration"
      />

    </section>

  </div>
</template>

<script>
import NavBar from "../components/NavBar.vue";

export default {
  components: { NavBar },
};
</script>

<style scoped>
/* Background */
.landing-container {
  min-height: 100vh;
  background-color: #FFF9F3;
}

/* Centered Layout */
.landing-content {
  max-width: 1200px;     /* keeps content centered */
  margin: 0 auto;        /* centers horizontally */
  
  display: flex;
  justify-content: center; /* moves both sections toward the center */
  align-items: center;

  padding: 4rem 2rem;    /* adds nice outer spacing */
  gap: 4rem;             /* space between text and image */
}

/* Left side text */
.left-section {
  max-width: 450px;
}

.title {
  font-size: 64px;
  line-height: 1.1;
  font-weight: 600;
  margin-bottom: 2rem;
  color: #305669; /* main theme color */
}

/* Button */
.get-started-btn {
  padding: 1rem 2.5rem;
  font-size: 20px;
  font-weight: 600;
  color: #ffffff;
  background-color: #8ABEB9; /* teal button */
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.get-started-btn:hover {
  opacity: 0.9;
}

/* Illustration */
.illustration {
  width: 520px;
  max-width: 100%;
}
</style>
