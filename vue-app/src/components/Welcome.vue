<script>
import PostService from "../services/PostService";

export default {
  name: "posts-list",
  data() {
    return {
      posts: [],
      currentPost: null,
      currentIndex: -1,
      title: "",
      userRoles: [],
      showAll: false
    };
  },

  computed: {
    isMember() {
      return (
        this.$store.state.auth.user && 
        this.$store.state.auth.user.roles.includes("ROLE_MEMBER")
        );
    }
  },

  created() {
    if (this.$store.state.auth.user && 
        this.$store.state.auth.user.roles.includes("ROLE_MEMBER")) {
      this.retrieve10Posts();
    }
  },

  methods: {
    retrieve10Posts() {
      PostService.getLast10()
        .then(response => {
          this.posts = response.data;
          this.showAll = false;
        })
        .catch(error => {
          console.error("Error retrieving posts:", error);
        });
    },

    fetchAllPosts() {
      PostService.getAll()
        .then(response => {
          this.posts = response.data;
          this.showAll = true;
        })
        .catch(error => {
          console.error("Error retrieving posts:", error);
        });
    },

    refreshList() {
      this.retrieve10Posts();
      this.currentPost = null;
      this.currentIndex = -1;
    },

    postPage(id) {
      this.$router.push("/post/" + id);
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
    },

    fetchUser() {
      const user = this.$store.state.auth.user;
      if (!user) {
        return;
      }
      this.userRoles = user.roles;
    },

  },
};
</script>

<template>
  <div class="row">
    <div class="col-lg-8 text-start">
      <h1 class="fw-bold">Japan7</h1>
      <p class="fs-6 text-justify">
        Japan7 est un club de l’ENSEEIHT centré sur la culture japonaise. On se retrouve lors de projection d’animes, de
        karaoké ou de simples discussions autour d’une partie de mahjong japonais.
        <br> On apprend aussi les bases de la langue nippone, et un peu de calligraphie.
      </p>
    </div>
  </div>

  <img src="/img/welcome.jpg" class="img-fluid rounded mx-auto d-block" alt="Image du club">
  <div v-if="isMember">
    <h2 class="fw-bold text-start mt-4 pb-2">Posts récents</h2>
    <section>
      <div class="text-start row row-cols-1 row-cols-lg-2 g-4">
        <div class="col" v-for="post in posts" :key="post.id">
            <div class="card">
              <div class="card-body">
                <h4 class="card-title">{{ post.title }}</h4>
                <p class="card-text multiline-truncate">{{ post.description }}</p>
                <div class="d-flex justify-content-between align-items-center">
                  <div class="btn-group">
                    <button @click="postPage(post.id)" class="btn btn-primary">Lire la suite</button>
                  </div>
                  <div class="text-muted ">
                    {{ post.author.username }} <br>
                    {{ formatDate(post.date) }}
                  </div>
                </div>
              </div>
            </div>
          </div>
      </div>
      <div v-if="!showAll">
        <button class="btn btn-secondary mt-3" @click="fetchAllPosts">Voir tous les posts</button>
      </div>
      <div v-if="showAll">
        <button class="btn btn-secondary mt-3" @click="retrieve10Posts">Réduire</button>
      </div>
    </section>
  </div>
</template>


<style>
.multiline-truncate {
  display: -webkit-box;
  -webkit-line-clamp: 3;       /* Nombre de lignes à afficher */
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.add-post-form {
  margin-top: 2em;
  margin-bottom: 2em;
  padding: 1em;
  border: 1px solid #b3b3b3;
  border-radius: 5px;
  background: #f8f8f8;
}
.add-post-form input {
  margin-right: 0.5em;
  margin-bottom: 0.5em;
  padding: 0.3em;
}
.add-post-form button {
  background: #27ae60;
  color: white;
  border: none;
  padding: 0.5em 1em;
  border-radius: 7px;
  cursor: pointer;
}
.add-post-form button:hover {
  background: #219150;
}

</style>
