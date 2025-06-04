<template>
  <div class="contact-page">
    <h1>Contact Us</h1>
    <ul class="contact-list">
      <li>
        <a href="https://www.instagram.com/japan7_enseeiht" target="_blank" class="contact-link">
          <i class="bi bi-instagram"></i> Instagram
        </a>
      </li>
      <li>
        <a href="https://discord.gg/mwC7jKKK4j" target="_blank" class="contact-link">
          <i class="bi bi-discord"></i> Discord
        </a>
      </li>
      <li>
        <a href="mailto:japan7@list.bde.enseeiht.fr" class="contact-link">
          <i class="bi bi-envelope"></i> Email
        </a>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "ContactsPage",
};
</script>

<style scoped>
.contact-page {
  text-align: center;
  margin-top: 50px;
}

.contact-page h1 {
  margin-bottom: 40px;
}

.contact-list {
  list-style-type: none;
  padding: 0;
}

.contact-list li {
  margin: 10px 0;
}

.contact-link {
  color: #3498db;
  text-decoration: none;
  font-weight: bold;
}

.contact-link:hover {
  text-decoration: underline;
}
</style>