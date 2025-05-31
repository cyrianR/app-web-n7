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
      newEvent: null,
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

    getFormattedEventType(eventType) {
      return EventService.formatEventType(eventType);
    },

    getColorForEventType(eventType) {
      return EventService.colorEvent(eventType);
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
        <div v-if="isAdmin" class="position-absolute" style="top: 0.5rem; right: 0.5rem;">
          <button @click="openUpdateModal(post)" class="btn btn-primary me-1">
            <i class="bi bi-pencil-square"></i>
          </button>
          <button @click="removePost(post.id)" class="btn btn-danger">
            <i class="bi bi-trash"></i>
          </button>
        </div>
      </div> 
      <div class="card-body text-start">
        <div>
          <h3> Description </h3>
          <p class="card-text mb-2">{{ post.description }}</p>
        </div>
        <h3 v-if="post.events[0]"> Évènements </h3>
        <div v-for="event in post.events">
          <button v-if="event" @click="ToEvent(event.id)" class="btn mt-1" :style="{ backgroundColor: getColorForEventType(event.eventType) }">{{ getFormattedEventType(event.eventType) }} {{ event.name }}</button>
        </div>
        <div class="text-muted mt-2 text-end">
          {{ post.author.username }} <br>
          {{ formatDate(post.date) }}
        </div>
      </div>
        
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
              class="btn btn-primary mt-1 me-1"
            >
              Ajouter l'évènement
            </button>
            <button 
              type = 'button'
              @click="clearEvents"
              class="btn btn-danger mt-1"
            >
              Retirer les évènements
            </button>
          </div>
          <div v-if="submissionError1" class="alert alert-danger mt-1">
            {{ submissionError1 }}
          </div>
          
          <div v-if="submissionError2" class="alert alert-danger mt-1">
            {{ submissionError2 }}
          </div>
          <div v-for="event2 in postToUpdate.events">
            <div v-if="event2">
              <p class="mt-1" :style="{ backgroundColor: getColorForEventType(event2.eventType) }">{{ getFormattedEventType(event2.eventType) }} {{ event2.name }}</p>
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