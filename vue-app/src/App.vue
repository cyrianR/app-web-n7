<script>
export default {
  name: "app"
};
</script>

<template>
  <div id="app">
    <nav class="navbar fixed-top navbar-light bg-light bg-opacity-75">
      <div class="container-fluid">
        <router-link to="/" class="navbar-brand">
          <img src="./assets/logo_clean_saisons_rond.png" width="50" height="50" alt="">
        </router-link>
        <div class="flex-row justify-content-end mx-2" id="navbarNav">
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
              <router-link to="" class="nav-link">Photos</router-link>
            </li>
            <li class="nav-item">
              <router-link to="" class="nav-link">Agenda</router-link>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container pt-5">
      <router-view />
    </div>

    <footer class="bg-light text-dark py-3 mt-4">
    <div class="container-fluid d-flex justify-content-between">
        <div class="d-flex flex-column justify-content-between ms-3">
            <span>© Japan7 2025</span>
            <div class="d-flex">
              <a href="https://discord.gg/mwC7jKKK4j" class="me-2"><img src="./assets/discord-outline.svg" alt="Discord" width="24"></a>
              <a href="https://www.instagram.com/japan7_enseeiht"><img src="./assets/instagram.svg" alt="Instagram" width="24"></a>
          </div>
        </div>
        
        <div class="text-end d-flex flex-column">
            <a href="#" class="text-dark text-decoration-none me-3">Cookies</a>
            <a href="#" class="text-dark text-decoration-none me-3">Contactez-nous</a>
            <a href="#" class="text-dark text-decoration-none me-3">Mentions légales</a>
        </div>
    </div>
</footer>
  </div>
</template>

<style scoped>

.navbar-nav > li > a {
  color: black;
  font-weight: 500;
}
footer{
  width:100%;
  position:absolute;
  left:0;
}

</style>

