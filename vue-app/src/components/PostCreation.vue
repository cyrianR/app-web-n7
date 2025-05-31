<script>
import PostService from '../services/PostService';

export default {
  name: 'PostCreation',
  data() {
    return {
      newPost: {
        title: "",
        description: "",
        date: "",
        author: {
          id: "",
          username: "",
          roles: []
        }
      },
      user : this.$store.state.auth.user
    };
  },

  methods: {
    CreatePost() {
      if (!this.newPost.title) {
        alert("Le titre est obligatoire.");
        return;
      }
      if (!this.newPost.description) {
        alert("La description est obligatoire.");
        return;
      }
      this.newPost.author.username = this.user.username;
      this.newPost.author.id = this.user.id;
      PostService.createPost(this.newPost)
        .then(() => {
          this.newPost = {
            title: "",
            description: "",
            date: "",
            author: {
              id: "",
              username: "",
              roles: []
        }
          };
        })
        .catch(() => {
          alert("Erreur lors de l'ajout du post.");
        });
    },
  }
};
</script>

<template>
  <div class="d-flex justify-content-center align-items-start">
    <div class="col-12 col-md-8 col-lg-10">
      <div class="card">
        <div class="card-header">
          <h3>Nouveau Post</h3>
        </div>
        <div class="card-body text-start">
          <form @submit.prevent="CreatePost">
            <input class="form-control" v-model="newPost.title" placeholder="Titre" required /> <br>
            <textarea
              class="form-control"
              v-model="newPost.description"
              rows="4"
              cols="50"
              placeholder="description"
              required
            ></textarea> <br>
            <div class="d-flex justify-content-end">
              <button type="submit" class="btn btn-primary">Créer le post</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.card {
  margin-top: 2rem;
}
.form-label {
  font-weight: 500;
  color: var(--bs-gray-700);
}
</style>
