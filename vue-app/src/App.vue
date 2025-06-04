<script>
import { mapActions } from 'vuex';
import WebSocketService from './services/WebSocketService';

export default {
  name: "app",
  computed: {
    isLoggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
    isAdmin() {
      return this.$store.state.auth.user.roles.includes("ROLE_ADMIN");
    }
  },
  mounted() {
    // Connect to WebSocket and listen for role updates
    WebSocketService.connect();
    WebSocketService.subscribeToRoleUpdates((message) => {
      if (message.userId === this.$store.state.auth.user.id) {
        // Update roles
        this.updateRoles(message.roles);
      }
    });
  },
  beforeDestroy() {
    // Disconnect WebSocket when the app is destroyed
    WebSocketService.disconnect();
  },
  methods: {
    ...mapActions('auth', ['updateRoles']),
    logout() {
      this.$store.dispatch("auth/logout");
      this.$router.push({ path: '/login', query: { message: 'logout-success' } });
    },
  },
};
</script>

<template>
  <div id="app p-0" class="d-flex flex-column min-vh-100">

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light bg-opacity-75">
      <div class="container-fluid mx-3">
        <router-link to="/" class="navbar-brand">
          <img src="/img/logo_clean_saisons_rond.png" width="50" height="50" alt="">
        </router-link>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent"
      aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarContent">
          <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <router-link to="/" class="nav-link">Accueil</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/cooking" class="nav-link">Cuisine</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/karaoke" class="nav-link">Karaoke</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/lesson" class="nav-link">Cours</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/proj" class="nav-link">Projection</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/photos" class="nav-link">Photos</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/agenda" class="nav-link">Agenda</router-link>
            </li>
            <li class="nav-item dropdown ms-lg-3">
              <a
                class="nav-link"
                href="#"
                id="userDropdown"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                <i class="bi bi-person-fill" style="font-size: 1.5rem;"></i>
              </a>
              <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                <li v-if="isLoggedIn">
                  <router-link to="/profile" class="dropdown-item">Mon profil</router-link>
                </li>
                <li v-if="isLoggedIn && isAdmin">
                  <router-link to="/adminboard" class="dropdown-item">Zone admin</router-link>
                </li>
                <li v-if="isLoggedIn">
                  <button @click="logout" class="dropdown-item">Se déconnecter</button>
                </li>
                <div v-else>
                  <li class="nav-item">
                    <router-link to="/register" class="dropdown-item">S'inscrire</router-link>
                  </li>
                  <li class="nav-item">
                    <router-link to="/login" class="dropdown-item">Se connecter</router-link>
                  </li>
                </div>
              </ul>
            </li>
            <li v-if="isLoggedIn && isAdmin" class="nav-item dropdown ms-lg-3">
              <a
                class="nav-link"
                href="#"
                id="userDropdown"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                <i class="bi bi-plus-circle" style="font-size: 1.5rem;"></i>
              </a>
              <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                <li>
                  <router-link to="" class="dropdown-item">Post</router-link>
                </li>
                <li>
                  <router-link to="" class="dropdown-item">Évènement</router-link>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <!-- Main content -->
    <div class="page-content container-fluid flex-grow-1">
      <router-view />
    </div>

    <!-- Footer -->
    <footer class="bg-light text-dark py-3 mt-4">
      <div class="container-fluid d-flex justify-content-between">
        <div class="d-flex flex-column justify-content-between ms-3">
          <span>© Japan7 2025</span>
          <div class="d-flex align-items-end">
            <a href="https://discord.gg/mwC7jKKK4j" class="me-2">
              <i class="bi bi-discord"></i>
            </a>
            <a href="https://www.instagram.com/japan7_enseeiht">
              <i class="bi bi-instagram"></i>
            </a>
          </div>
        </div>
        <div class="text-end d-flex flex-column me-3">
          <a href="#" class="text-dark text-decoration-none">Cookies</a>
          <a href="#" class="text-dark text-decoration-none">Contactez-nous</a>
          <a href="#" class="text-dark text-decoration-none">Mentions légales</a>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>

.navbar-nav {
  align-items: center;
}

.navbar-nav > li > a {
  color: black;
  font-weight: 500;
}

.bi {
  color: rgba(var(--bs-dark-rgb),var(--bs-text-opacity)) !important;
  font-size: 22px !important;
}

.page-content {
  padding-top: 6rem;
  max-width: 1200px;
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
</style>
