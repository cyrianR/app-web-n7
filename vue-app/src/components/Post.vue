<script>
import PostService from '../services/PostService';

export default {
  data() {
    return {
      post : [],
      user : this.$store.state.auth.user,
    };
  },

  created() {
    this.retrievePost();
  },

  methods: {
    retrievePost() {
      PostService.getById(this.$route.params.id)
        .then(response => {
          this.post = response.data;
        })
        .catch(error => {
          console.error("Error retrieving post:", error);
        });
    },

    formatDate(isoString) {
      const date = new Date(isoString);
      return date.toLocaleString('fr-FR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  }
};
</script>

<template>
<div class="d-flex justify-content-center align-items-start">
  <div class="col-12 col-md-8 col-lg-10">
    <div class="card">
      <h3 class="card-title"> {{ post.title }}</h3> 
        <div class="card-body text-start">
          <p class="card-text">{{ post.description }}</p>
        </div>
        <div class="text-muted ">
          {{ post.author.username }} <br>
          {{ formatDate(post.date) }}
        </div>
    </div>
  </div>
</div>
</template>


<style scoped>

</style>