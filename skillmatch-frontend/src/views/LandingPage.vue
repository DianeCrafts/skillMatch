<template>
  <div class="landing-wrapper">

    <NavBar />

    <section class="hero">

      <!-- Left Text -->
      <div class="hero-text">
        <h1 class="hero-title">
          Find your <br /> perfect match.
        </h1>

        <button class="cta-btn" @click="$router.push('/login')">
          Get Started
        </button>
      </div>

      <!-- Right Illustration -->
      <div class="hero-image-wrapper">
        <img
          src="/src/assets/landing-illustration.png"
          alt="Landing illustration"
          class="hero-image"
        />
      </div>

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
/* -----------------------------
   Overall Wrapper
------------------------------ */
.landing-wrapper {
  min-height: 100vh;
  background: var(--color-bg);
  display: flex;
  flex-direction: column;
}

/* -----------------------------
   HERO SECTION
------------------------------ */
.hero {
  max-width: 1200px;
  margin: 0 auto;
  padding: 5rem 2rem;

  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 4rem;

  animation: fadeIn 0.6s ease-in-out;
}

/* Text */
.hero-text {
  max-width: 480px;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.hero-title {
  font-size: clamp(42px, 5vw, 70px);
  line-height: 1.1;
  font-weight: 700;
  color: var(--color-primary);
}

/* CTA Button */
.cta-btn {
  padding: 1rem 2.8rem;
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--color-white);
  background-color: var(--color-accent);

  border: none;
  border-radius: 12px;
  cursor: pointer;

  transition: all 0.2s ease-in-out;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.15);
}

.cta-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
}

/* -----------------------------
   HERO IMAGE
------------------------------ */
.hero-image-wrapper {
  flex-shrink: 0;
}

.hero-image {
  width: 540px;
  max-width: 100%;
  object-fit: contain;
  animation: float 4s ease-in-out infinite;
}

/* -----------------------------
   Animations
------------------------------ */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to   { opacity: 1; transform: translateY(0); }
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

/* -----------------------------
   Mobile Responsive
------------------------------ */
@media (max-width: 880px) {
  .hero {
    flex-direction: column-reverse;
    text-align: center;
    padding-top: 3rem;
  }

  .hero-text {
    align-items: center;
  }
}
</style>
