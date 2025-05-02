<template>
  <div>
    <p>Déconnection...</p>
  </div>
</template>

<script>
export default {
  mounted() {
    this.$store.dispatch('auth/logout');
    this.$router.push('/login');
  }
};
</script>