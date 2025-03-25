<script>
export default {
  name: "app"
};
</script>

<template>
  <div id="app">
    <nav class="navbar fixed-top navbar-light" style="background-color: #ffffff;">
      <div class="container-fluid">
        <router-link to="/" class="navbar-brand">
          <img src="./assets/logo_clean_saisons_rond.png" width="50" height="50" alt="">
        </router-link>
        <div class="flex-row justify-content-end mx-2" id="navbarNav">
          <ul class="navbar-nav flex-row gap-4">
            <li class="nav-item">
              <router-link to="" class="nav-link">Acceuil</router-link>
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

    <div class="container mt-5">
      <router-view />
    </div>

  </div>
</template>

<style scoped>

.navbar-nav > li > a {
  color: black;
  font-weight: 500;
}

</style>

