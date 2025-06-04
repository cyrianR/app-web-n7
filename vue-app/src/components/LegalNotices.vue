<template>
  <div class="legal-notices-page">
    <h1>Mentions Légales</h1>
    <p>
      Ce site web est exploité par Japan7 ENSEEIHT. Tous droits réservés. Pour toute demande, veuillez nous contacter à 
      <a href="mailto:japan7@list.bde.enseeiht.fr" class="contact-link">japan7@list.bde.enseeiht.fr</a>.
    </p>
    <p>
      Des informations légales supplémentaires seront fournies ici.
    </p>
  </div>
</template>

<script>
export default {
  name: "LegalNoticesPage",
};
</script>

<style scoped>
.legal-notices-page {
  text-align: center;
  margin: 50px auto;
  max-width: 800px;
}

.legal-notices-page h1 {
  margin-bottom: 20px;
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
