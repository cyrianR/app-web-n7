<script>
import { mapActions } from 'vuex';
import WebSocketService from './services/WebSocketService';

export default {
  name: "app",
  computed: {
    isLoggedIn() {
      return this.$store.state.auth.status.loggedIn;
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
  },
};
</script>

<template>
  <div id="app p-0" class="d-flex flex-column min-vh-100">

    <!-- Navbar -->
    <nav class="navbar fixed-top navbar-light bg-light bg-opacity-75">
      <div class="container-fluid mx-3">
        <router-link to="/" class="navbar-brand">
          <img src="/img/logo_clean_saisons_rond.png" width="50" height="50" alt="">
        </router-link>
        <!--  <div class="flex-row justify-content-end" id="navbarNav">-->
            <ul class="navbar-nav flex-row gap-4">
              <li class="nav-item">
                <router-link to="/" class="nav-link">Accueil</router-link>
              </li>
              <li class="nav-item">
                <router-link to="" class="nav-link">Cuisine</router-link>
              </li>
              <li class="nav-item">
                <router-link to="" class="nav-link">Karaoke</router-link>
              </li>
              <li class="nav-item">
                <router-link to="" class="nav-link">Cours</router-link>
              </li>
              <li class="nav-item">
                <router-link to="" class="nav-link">Projection</router-link>
              </li>
              <li class="nav-item">
                <router-link to="/photos" class="nav-link">Photos</router-link>
              </li>
              <li class="nav-item">
                <router-link to="/agenda" class="nav-link">Agenda</router-link>
              </li>
              <li class="nav-item dropdown">
                <a
                  class="nav-link dropdown-toggle "
                  href="#"
                  id="userDropdown"
                  role="button"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  <i class="bi bi-person-fill"></i>
                </a>
                <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdown">
                  <li v-if="isLoggedIn">
                    <router-link to="/profile" class="dropdown-item">Mon profil</router-link>
                  </li>
                  <li v-if="isLoggedIn">
                    <button @click="logout" class="dropdown-item">Se déconnecter</button>
                  </li>
                  <li v-else>
                    <router-link to="/register" class="dropdown-item">S'inscrire</router-link>
                  </li>
                  <li v-else>
                    <router-link to="/login" class="dropdown-item">Se connecter</router-link>
                  </li>
                </ul>
              </li>
              
              <!-- <div class="d-flex flex-column justify-content-center">
                <li v-if="isLoggedIn" class="nav-item">
                  <router-link to="/profile" class="btn btn-primary">Compte</router-link>
                </li>
                <div v-else class="d-flex flex-row gap-3">
                  <li class="nav-item">
                    <router-link to="/register" class="btn btn-primary">S'inscrire</router-link>
                  </li>
                  <li class="nav-item">
                    <router-link to="/login" class="btn btn-primary">Se connecter</router-link>
                  </li>
                </div>
              </div> -->
            </ul>
        <!--  </div>-->
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
          <div class="d-flex">
            <a href="https://discord.gg/mwC7jKKK4j" class="me-2"><img src="/svg/discord-outline.svg"
                alt="Discord" width="24"></a>
            <a href="https://www.instagram.com/japan7_enseeiht"><img src="/svg/instagram.svg" alt="Instagram"
                width="24"></a>
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
.navbar {
  overflow: visible; /* Prevent content overflow */
}

.navbar > .container-fluid {
  height: 60px;
}



.dropdown-menu {
  position: absolute; /* Position the dropdown menu relative to the dropdown button */
  top: calc(100% + 5px); /* Position the dropdown slightly below the button */
  left: auto; /* Align dropdown menu correctly */
  right: 0; /* Align dropdown menu to the right of the button */
  z-index: 1050; /* Ensure the dropdown is above other elements */
  min-width: 200px; /* Set a minimum width for the dropdown menu */
  width: auto; /* Allow the dropdown menu to expand based on content */
}

.navbar-nav {
  height: 40px; /* Match the height of the navbar */
  display: flex;
  /* align-items: center; /* Center items vertically */
}

.navbar-nav > li > a {
  color: black;
  font-weight: 500;
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
