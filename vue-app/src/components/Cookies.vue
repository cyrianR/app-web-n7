<template>
    <div class="cookies-page">
      <h1>We don't use cookies</h1>
      <p>But you can bake some cookies!</p>
      <a href="https://orteil.dashnet.org/cookieclicker/" target="_blank" class="cookie-link">
        Make your cookies.
      </a>
    </div>
  </template>
  
  <script>
  export default {
    name: "CookiesPage",
  };
  </script>
  
  <style scoped>
  .cookies-page {
    text-align: center;
    margin-top: 50px;
  }
  
  .cookie-link {
    color: #3498db;
    text-decoration: none;
    font-weight: bold;
  }
  
  .cookie-link:hover {
    text-decoration: underline;
  }
  </style>