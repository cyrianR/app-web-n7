<script>
import EventService from '../services/EventService';

export default {
  name: 'EventCreation',
  data() {
    return {
      newEvent: {
        name: '',
        date: '',
        eventType: '',
        description: ''
      },
      user: this.$store.state.auth.user,
      availableEventTypes: [],
      submissionError: null
    };
  },

  created() {
    // Add available event types based on user roles
    if (this.user.roles.includes("ROLE_ADMIN")) {
      this.availableEventTypes = ["LESSON", "KARAOKE", "PROJO", "COOKING"];
      this.newEvent.eventType = "LESSON";
    }
    else {
      if (this.user.roles.includes("ROLE_KARAOKE_ADMIN")) {
        this.availableEventTypes.push("KARAOKE");
        this.newEvent.eventType = "KARAOKE";
      }
      if (this.user.roles.includes("ROLE_LESSON_ADMIN")) {
        this.availableEventTypes.push("LESSON");
        this.newEvent.eventType = "LESSON";
      }
      if (this.user.roles.includes("ROLE_PROJ_ADMIN")) {
        this.availableEventTypes.push("PROJO");
        this.newEvent.eventType = "PROJO";
      }
      if (this.user.roles.includes("ROLE_COOKING_ADMIN")) {
        this.availableEventTypes.push("COOKING");
        this.newEvent.eventType = "COOKING";
      }
    }
  },
  
  computed: {

    localDateTime: {
      get() {
        if (!this.newEvent.date) return '';
        const date = new Date(this.newEvent.date);
        const pad = n => n.toString().padStart(2, '0');
        return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
      },
      set(val) {
        if (!val) {
          this.newEvent.date = '';
          return;
        }
        const local = new Date(val);
        this.newEvent.date = local.toISOString().slice(0, 19) + 'Z';
      }
    },

    descriptionRows() {
      const desc = this.newEvent.description || '';
      return Math.max(desc.split('\n').length, 3);
    }
  },

  methods: {
    createEvent() {
      EventService.addEvent(this.newEvent)
        .then(response => {
          this.$router.push({ 
            name: 'event-details', 
            params: { id: response.data.id },
            query: { created: 'success' }
          });
        })
        .catch(error => {
          console.error("Error creating event:", error);
          this.submissionError = "Erreur lors de la création de l'évènement";
        });
    },

    getColorForEventType(eventType) {
      return EventService.colorEvent(eventType);
    },

    getFormattedEventType(eventType) {
      return EventService.formatEventType(eventType);
    }
  }
};
</script>

<template>
<div class="d-flex justify-content-center align-items-start">
  <div class="col-12 col-md-8 col-lg-10">
    <div class="card">
      <div class="card-header" :style="{ backgroundColor: getColorForEventType(newEvent.eventType) }">
        <h3 class="m-0">Nouvel évènement : {{ getFormattedEventType(newEvent.eventType) }}</h3>
      </div>
      
      <div class="card-body text-start">
        <form @submit.prevent="createEvent">
          <div class="mb-3">
            <label class="form-label">Nom de l'évènement</label>
            <input 
              v-model="newEvent.name" 
              class="form-control" 
              placeholder="Nom" 
              required
            >
          </div>

          <div class="mb-3">
            <label class="form-label">Date et heure</label>
            <input 
              v-model="localDateTime" 
              class="form-control" 
              type="datetime-local" 
              required
            >
          </div>

          <div class="mb-3">
            <label class="form-label">Type d'évènement</label>
            <select 
              v-model="newEvent.eventType" 
              class="form-select"
            >
              <option 
                v-for="type in availableEventTypes" 
                :key="type" 
                :value="type"
              >
                {{ getFormattedEventType(type) }}
              </option>
            </select>
          </div>

          <div class="mb-3">
            <label class="form-label">Description</label>
            <textarea
              v-model="newEvent.description"
              class="form-control"
              placeholder="Description"
              :rows="descriptionRows"
              style="min-height: 120px; resize: vertical;"
              required
            ></textarea>
          </div>

          <div class="d-flex justify-content-end">
            <button 
              type="submit" 
              class="btn btn-primary"
            >
              Créer l'évènement
            </button>
          </div>

          <div v-if="submissionError" class="alert alert-danger mt-3">
            {{ submissionError }}
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
