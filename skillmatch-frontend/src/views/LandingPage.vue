<template>
  <div class="landing-container">

    <NavBar />

    <section class="landing-content">
      
      <!-- Text Section -->
      <div class="left-section">
        <h1 class="title">
          Find your <br /> perfect match.
        </h1>

        <button class="get-started-btn" @click="$router.push('/login')">
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
  background-color: var(--color-bg);
}

/* Centered Layout */
.landing-content {
  max-width: 1200px;
  margin: 0 auto;

  display: flex;
  justify-content: center;
  align-items: center;

  padding: 4rem 2rem;
  gap: 4rem;
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
  color: var(--color-primary); /* main theme color */
}

/* Button */
.get-started-btn {
  padding: 1rem 2.5rem;
  font-size: 20px;
  font-weight: 600;
  color: var(--color-white);
  background-color: var(--color-accent);
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.get-started-btn:hover {
  opacity: 0.8;
}

/* Illustration */
.illustration {
  width: 520px;
  max-width: 100%;
}
</style>
