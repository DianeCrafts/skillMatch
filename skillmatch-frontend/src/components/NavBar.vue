<template>
  <nav class="navbar">
    <div class="navbar-inner">
      <h1 class="logo">SkillMatch</h1>

      <div class="nav-buttons">
        <button class="login-btn" @click="$router.push('/login')">Login</button>
        <button class="register-btn" @click="$router.push('/register')">Register</button>
      </div>
    </div>
  </nav>
</template>

<style scoped>
/* Full-width navbar background */
.navbar {
  width: 100%;
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.06);
}

/* Navbar inner container — matches landing page width */
.navbar-inner {
  max-width: 1200px;   /* keeps everything centered */
  margin: 0 auto;      /* center horizontally */
  padding: 20px 40px;  /* spacing so buttons don't touch edges */

  display: flex;
  align-items: center;
}

/* LOGO on the left */
.logo {
  font-size: 26px;
  font-weight: 600;
  color: #305669;
  margin-right: auto; /* pushes buttons to far right */
}

/* Button wrapper on right side */
.nav-buttons {
  display: flex;
  align-items: center;
  gap: 24px;  /* spacing between login/register buttons */
}

/* LOGIN BUTTON (simple text, no background) */
.login-btn {
  background: none;
  color: #305669;
  font-size: 16px;
  font-weight: 500;
  border: none;
  cursor: pointer;
  padding: 10px 24px;
}

.login-btn:hover {
  text-decoration: underline;
}

/* REGISTER BUTTON (primary button) */
.register-btn {
  background-color: #C1785A;
  color: white;
  font-size: 16px;
  font-weight: 500;
  border: none;
  padding: 10px 28px;
  border-radius: 8px;
  cursor: pointer;
}

.register-btn:hover {
  opacity: 0.95;
}
</style>
