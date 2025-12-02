<template>
  <nav class="navbar">
    <div class="navbar-inner">

      <h1 class="logo" @click="$router.push('/')">SkillMatch</h1>

      <div class="nav-buttons">
        <button class="login-btn" @click="$router.push('/login')">Login</button>
        <button class="register-btn" @click="$router.push('/register')">Register</button>
      </div>

    </div>
  </nav>
</template>

<style scoped>
/* -----------------------------
   NAVBAR WRAPPER
------------------------------ */
.navbar {
  width: 100%;
  background-color: var(--color-white);
  box-shadow: 0 2px 6px rgba(0,0,0,0.06);
  position: sticky;
  top: 0;
  z-index: 50;
}

/* -----------------------------
   NAVBAR INNER LAYOUT
------------------------------ */
.navbar-inner {
  max-width: 1200px;
  margin: 0 auto;

  padding: 18px 40px;
  display: flex;
  align-items: center;
}

/* -----------------------------
   LOGO
------------------------------ */
.logo {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-primary);
  margin-right: auto;
  cursor: pointer;
  transition: 0.2s ease;
}

.logo:hover {
  opacity: 0.75;
}

/* -----------------------------
   NAV BUTTONS
------------------------------ */
.nav-buttons {
  display: flex;
  align-items: center;
  gap: 22px;
}

/* -----------------------------
   LOGIN BUTTON (simple link-style)
------------------------------ */
.login-btn {
  background: none;
  border: none;

  color: var(--color-primary);
  font-size: 16px;
  font-weight: 500;

  cursor: pointer;
  padding: 8px 16px;
  transition: 0.2s ease;
}

.login-btn:hover {
  text-decoration: underline;
}

/* -----------------------------
   REGISTER BUTTON
------------------------------ */
.register-btn {
  background-color: var(--color-warm);
  color: var(--color-white);

  padding: 10px 26px;
  font-size: 16px;
  font-weight: 600;

  border: none;
  border-radius: 10px;

  cursor: pointer;
  transition: 0.2s ease;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 14px rgba(0,0,0,0.12);
}
</style>
