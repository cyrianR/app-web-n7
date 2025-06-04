<script>
import EventService from '../services/EventService';
import LikeService from '../services/LikeService';

export default {
  name: 'EventDetail',
  data() {
    return {
      event : [],
      newEvent: {},
      user : this.$store.state.auth.user,
      liked : false,
      showUpdateModal : false,
      message : '',
      availableEventTypes: ["LESSON", "KARAOKE", "PROJO", "COOKING"]
    };
  },

  computed: {
    isAdmin() {
      return (
        this.user.roles.includes("ROLE_ADMIN") ||
        (this.user.roles.includes("ROLE_LESSON_ADMIN") && this.event.eventType === "LESSON") ||
        (this.user.roles.includes("ROLE_KARAOKE_ADMIN") && this.event.eventType === "KARAOKE") ||
        (this.user.roles.includes("ROLE_PROJ_ADMIN") && this.event.eventType === "PROJO")
      );
    },

    isTheAdmin() {
      return (this.user.roles.includes("ROLE_ADMIN"));
    },

    // Converts ISO UTC string to local format for input
    localDateTime: {
      get() {
        if (!this.newEvent.date) return '';
        // Convert "2025-06-04T10:34:30Z" to "2025-06-04T10:34"
        const date = new Date(this.newEvent.date);
        const pad = n => n.toString().padStart(2, '0');
        return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
      },
      set(val) {
        // Convert "2025-06-04T10:34" to ISO UTC string
        if (!val) {
          this.newEvent.date = '';
          return;
        }
        // Parse as local time, then convert to UTC ISO string
        const local = new Date(val);
        this.newEvent.date = local.toISOString().slice(0, 19) + 'Z';
      }
    },

    descriptionRows() {
      const desc = this.newEvent.description || '';
      return Math.max(desc.split('\n').length, 3);
    }
  },

  created() {
    this.retrieveEvent();
    this.retrieveUserLike();

    const queryMessage = this.$route.query.created;
    if (queryMessage === 'success') {
      this.message = 'Évènement créé avec succès.';
    }
  },

  methods: {
    retrieveEvent() {
      EventService.getById(this.$route.params.id)
        .then(response => {
          this.event = response.data;
        })
        .catch(error => {
          console.error("Error retrieving event:", error);
        });
    },

    retrieveUserLike() {
      const user = this.$store.state.auth.user;
      if (!user) {
        return;
      }
      LikeService.getLikeByUserAndEvent(user.id, this.$route.params.id)
        .then(response => {
          this.liked = response.data;
        })
       .catch(error => {
          console.error('Error retrieving like:', error);
        });
    },

    changeLike(){
      const user = this.$store.state.auth.user;
      if (!user) {
        return;
      }

      if (this.liked === true) {
        LikeService.deleteLike(this.user.id, this.event.id)
          .then(() => {
            this.event.likes -= 1;
          })
          .catch(error => {
            console.error("Error deleting like:", error);
          });
          this.liked = false;
      } else {
        LikeService.addLike(this.user.id, this.event.id)
          .then(() => {
            this.event.likes += 1;
          })
          .catch(error => {
            console.error("Error creating like:", error);
          });
          this.liked = true;
      }
    },

    openUpdateModal() {
      this.newEvent = { ...this.event };
      this.showUpdateModal = true;
    },

    closeUpdateModal() {
      this.showUpdateModal = false;
      this.newEvent = {};
    },

    updateEvent(){
      EventService.updateEvent(this.event.id, this.newEvent)
        .then(() => {
          this.retrieveEvent();
          this.closeUpdateModal();
        })
        .catch(error => {
          console.error("Error updating event:", error);
        });
    },

    deleteEvent() {
      EventService.delete(this.event.id)
        .then(() => {
          this.$router.push({ name: 'home' });
        })
        .catch(error => {
          console.error("Error deleting event:", error);
        });
    },

    getFormattedEventType(eventType) {
      return EventService.formatEventType(eventType);
    },

    getColorForEventType(eventType) {
      return EventService.colorEvent(eventType);
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
  <div v-if="message" class="alert alert-success col-12 col-md-8 col-lg-10" role="alert">
    {{ this.message }}
  </div>
</div>
<div class="d-flex justify-content-center align-items-start">
  <!-- Event -->
  <div class="col-12 col-md-8 col-lg-10">
    <div class="card">
        <div class="card-header" :style="{ backgroundColor: getColorForEventType(event.eventType) }">
        <h3 class="m-0"> {{ getFormattedEventType(event.eventType) }} </h3>
        <div v-if="isAdmin" class="position-absolute" style="top: 0.5rem; right: 0.5rem;">
          <button @click="openUpdateModal" class="btn btn-primary me-1">
            <i class="bi bi-pencil-square"></i>
          </button>
          <button @click="deleteEvent" class="btn btn-danger">
            <i class="bi bi-trash"></i>
          </button>
        </div>
        </div>
        <div class="card-body text-start">
          <h5 class="card-title"> {{ event.name }}</h5>  
          <p class="card-text" style="white-space: pre-line;">{{ event.description }}</p>
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <button @click="changeLike" class="btn">
                <i class="bi bi-heart-fill text-danger me-1" v-if="liked"></i>
                <i class="bi bi-heart me-1" v-else></i>
                {{ this.event.likes }}
              </button>
            </div>
            <router-link to="/agenda" class="card-link">{{ formatDate(event.date) }}</router-link>
          </div>
        </div>
    </div>
  </div>

  <!-- Event Modal -->
  <div v-if="showUpdateModal" class="modal fade show d-block" tabindex="-1" style="background:rgba(0,0,0,0.4);">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h3 class="modal-title">Modifier l'évènement</h3>
          <button type="button" class="btn-close" @click="closeUpdateModal"></button>
        </div>
        <form @submit.prevent="updateEvent">
          <div class="modal-body">
            <input v-model="newEvent.name" class="form-control mb-2" placeholder="Nom" required />
            <input v-model="localDateTime" class="form-control mb-2" type="datetime-local" required />
            <select v-if="isTheAdmin" v-model="newEvent.eventType" class="form-select mb-2">
              <option disabled value="">-- Choisissez un type d'évènement --</option>
              <option v-for="type in availableEventTypes" :key="type" :value="type">
                {{ type }}
              </option>
            </select>
            <textarea v-model="newEvent.description" class="form-control mb-2" placeholder="Description" :rows="descriptionRows" required></textarea>
          </div>
          <div class="modal-footer">
            <button type="button" @click="closeUpdateModal" class="btn btn-secondary">Annuler</button>
            <button type="submit" class="btn btn-primary">Enregistrer</button>
          </div>
        </form>
      </div>
    </div>
  </div>

</div>
</template>


<style scoped>

</style>