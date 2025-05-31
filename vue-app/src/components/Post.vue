<script>
import PostService from '../services/PostService';
import EventService from '../services/EventService';

export default {
  data() {
    return {
      events : [],
      post : {
        title: '',
        description: '',
        events: [],
        author: '',
        date: ''
      },
      postToUpdate : {
        title: '',
        description: '',
        events: [],
        author: this.$store.state.auth.user,
        date: ''
      },
      newEvent: {
        name: '',
        date: '',
        eventType: '',
        description: ''
      },
      submissionError1:"",
      submissionError2:"",
      user : this.$store.state.auth.user,
      showUpdateModal: false,
    };
  },

  created() {
    this.retrievePost();
    this.retrieveEvents();
  },

  computed: {
    isAdmin() {
      return (
        this.user.roles.includes("ROLE_ADMIN")
      );
    }
  },

  methods: {
    retrievePost() {
      PostService.getById(this.$route.params.id)
        .then(response => {
          this.post = response.data;
          this.postToUpdate = response.data;
        })
        .catch(error => {
          console.error("Error retrieving post:", error);
        });
    },

    retrieveEvents() {
      EventService.getAll()
        .then(response => {
          this.events = response.data;
        })
        .catch(error => {
          console.error("Error retrieving events:", error);
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
    },

    removePost(id) {
      if (confirm("Voulez-vous vraiment supprimer ce post ?")) {
        PostService.delete(id)
          .then(() => {
            this.$router.push("/");
          })
          .catch(() => {
            alert("Erreur lors de la suppression.");
          });
      }
    },

    addEvent(event) {
        if (!event) {
          this.submissionError1 = "Veuillez sélectionner un événement.";
          return;
        }

        const alreadyAdded = this.postToUpdate.events.some(e => e.id === event.id);
        if (alreadyAdded) {
          this.submissionError1 = "Cet événement a déjà été ajouté.";
          return;
        }
      this.postToUpdate.events.push(event);
      this.submissionError1 = "";
    },

    clearEvents() {
      this.postToUpdate.events = [];
      this.submissionError2 = "";
    },

    ToEvent(id) {
      this.$router.push("/event/" + id);
    },

    updatePost() {
      if (!this.postToUpdate.title) {
        alert("Le titre est obligatoire.");
        return;
      }
      if (!this.postToUpdate.description) {
        alert("La description est obligatoire.");
        return;
      }
      PostService.updatePost(this.postToUpdate.id, this.postToUpdate)
        .then(response => {
          this.post = response.data;
          this.postToUpdate = response.data;
          this.closeUpdateModal();
        })
        .catch(() => {
          alert("Erreur lors de la mise à jour du post.");
        });
    },

    openUpdateModal(post) {
      this.postToUpdate = JSON.parse(JSON.stringify(post));
      this.showUpdateModal = true;
    },
    closeUpdateModal() {
      this.showUpdateModal = false;
    },
  }
};
</script>

<template>
<div class="d-flex justify-content-center align-items-start">
  <div class="col-12 col-md-8 col-lg-10">
    <div class="card">
      <div class="card-header">
        <h3 class="card-title"> {{ post.title }}</h3> 
      </div> 
      <div class="card-body">
      <h3> Evènements </h3>
      <div v-for="event in post.events">
        <button v-if="event" @click="ToEvent(event.id)">{{ event.name }}</button>
      </div>
      <div>
        <h3> Description </h3>
        <p class="card-text">{{ post.description }}</p>
      </div> 
      </div>
      <div class="text-muted ">
        {{ post.author.username }} <br>
        {{ formatDate(post.date) }}
      </div>
    </div>
    <div v-if="isAdmin" class="d-flex justify-content-end">
      <button @click="openUpdateModal(post)" class="btn btn-primary btn-sm">Modifier</button>
      <button @click="removePost(post.id)" class="btn btn-danger btn-sm">Supprimer</button>
    </div>
  </div>
</div>

<div v-if="showUpdateModal" class="modal fade show d-block" tabindex="-1" style="background:rgba(0,0,0,0.4);">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title">Modifier le Post</h3>
        <button type="button" class="btn-close" @click="closeUpdateModal"></button>
      </div>
      <form @submit.prevent="updatePost">
        <div class="modal-body">
          <div class="mb-3">
            <select 
            class="form-select"
            v-model="newEvent"
          >
            <option 
              v-for="newevent in events" 
              :key="newevent" 
              :value="newevent"
            >
              {{ newevent.name }}
            </option>
          </select>
          <button
              type = 'button'
              @click="addEvent(newEvent)"  
              class="btn btn-primary mt-1"
            >
              ajouter l'évènement
            </button>
          </div>
          <div v-if="submissionError1" class="alert alert-danger mt-1">
            {{ submissionError1 }}
          </div>
          <button 
            type = 'button'
            @click="clearEvents"
            class="btn btn-primary mt-1"
          >
            retirer les évènements
          </button>
          <div v-if="submissionError2" class="alert alert-danger mt-1">
            {{ submissionError2 }}
          </div>
          <div v-for="event2 in postToUpdate.events">
            <div v-if="event2">
              <p>{{ event2.name }}</p>
            </div>
          </div>
          <input v-model="postToUpdate.title" class="form-control mb-2" placeholder="Titre" required />
          <textarea
              class="form-control"
              v-model="postToUpdate.description"
              rows="4"
              cols="50"
              placeholder="Description"
              required
            ></textarea>
        </div>
        <div class="modal-footer">
          <button type="button" @click="closeUpdateModal" class="btn btn-secondary">Annuler</button>
          <button type="submit" class="btn btn-primary">Enregistrer</button>
        </div>
      </form>
    </div>
  </div>
</div>

</template>


<style scoped>

</style>