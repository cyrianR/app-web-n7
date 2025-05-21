<script>
import PostService from "../services/PostService";

export default {
  name: "posts-list",
  data() {
    return {
      posts: [],
      currentPost: null,
      currentIndex: -1,
      title: ""
    };
  },

  created() {
    this.retrievePosts();
  },

  methods: {
    retrievePosts() {
      PostService.getAll()
        .then(response => {
          this.posts = response.data;
        })
        .catch(error => {
          console.error("Error retrieving posts:", error);
        });
    },

    refreshList() {
      this.retrievePosts();
      this.currentPost = null;
      this.currentIndex = -1;
    },

  },
};
</script>

<template>
  <div class="row">
    <div class="col-lg-8 tas">
      <h1 class="fw-bold">Japan7</h1>
      <p class="fs-6 text-justify">
        Japan7 est un club de l’ENSEEIHT centré sur la culture japonaise. On se retrouve lors de projection d’animes, de
        karaoké ou de simples discussions autour d’une partie de mahjong japonais.
        <br> On apprend aussi les bases de la langue nippone, et un peu de calligraphie.
      </p>
    </div>
  </div>

  <img src="/img/welcome.jpg" class="img-fluid rounded mx-auto d-block" alt="Image du club">

  <h2 class="fw-bold tas mt-4 pb-2">Posts récents</h2>
  <section>
    <div class="tas row row-cols-1 row-cols-lg-2 g-4">
      <div class="col" v-for="post in posts" :key="post.id">
          <div class="card">
            <div class="card-body">
              <h4 class="card-title">{{ post.author.username }}</h4>
              <p class="card-text">{{ post.description }}</p>
              <div class="d-flex justify-content-between align-items-center">
                <div class="btn-group">
                  <button href="#" class="btn btn-primary">Lire la suite</button>
                  <button href="#" class="btn btn-secondary">Voir sur l'agenda</button>
                </div>
                <div class="text-muted ">
                  {{ post.date }}
                </div>
              </div>
            </div>
          </div>
        </div>
    </div>

  </section>
</template>


<style scoped>
.tas {
  text-align: start;
}
</style>
