<script>
import EventService from '../services/EventService';

export default {
  data() {
    return {
      event: []
    };
  },

  created() {
    this.retrieveEvent();
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
  <div class="col-12 col-md-8 col-lg-10">
    <div class="card">
        <h3 class="card-header" :style="{ backgroundColor: getColorForEventType(event.eventType) }">
          {{ getFormattedEventType(event.eventType) }} 
        </h3>
        <div class="card-body text-start">
          <h5 class="card-title"> {{ event.name }}</h5>  
          <p class="card-text">{{ event.description }}</p>
          <div class="d-flex justify-content-between align-items-center">
            <div class="btn-group">
              <!-- TODO Votes : juste des likes ça suffira et un compteur de likes -->
            </div>
            <router-link to="/agenda" class="card-link">{{ formatDate(event.date) }}</router-link>
          </div>
        </div>
    </div>
  </div>
</div>
</template>


<style scoped>

</style>