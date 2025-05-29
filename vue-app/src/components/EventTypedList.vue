<script>
import EventService from '../services/EventService';

export default {
  name: 'EventTypedList',
  props: {
    eventType: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      events: [],
      loading: false,
      error: null
    };
  },
  watch: {
    eventType: {
      immediate: true,
      handler(newType) {
        this.fetchEvents(newType);
      }
    }
  },
  methods: {
    fetchEvents(type) {
      this.loading = true;
      this.error = null;
      EventService.getFutureEvents(type)
        .then(response => {
          this.events = response.data;
        })
        .catch(err => {
          this.error = 'Erreur lors du chargement des événements.';
        })
        .finally(() => {
          this.loading = false;
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
  <div>
    <h2 class="mb-3" :style="{ backgroundColor: getColorForEventType(eventType), padding: '0.5em' }">
      {{ getFormattedEventType(eventType) }} à venir
    </h2>
    <div v-if="loading">Chargement...</div>
    <div v-else-if="error" class="text-danger">{{ error }}</div>
    <div v-else>
      <div v-if="events.length === 0">Aucun événement à venir.</div>
      <ul class="list-group">
        <li v-for="event in events" :key="event.id" class="list-group-item mb-2">
          <div class="d-flex align-items-center justify-content-between">
            <div class="flex-grow-1">
              <div class="event-text">
                <strong class="d-block text-start">{{ event.name }}</strong>
                <div class="text-start">{{ event.description }}</div>
              </div>
              <div class="text-muted text-start">{{ formatDate(event.date) }}</div>
            </div>
            <router-link :to="`/event/${event.id}`" class="btn btn-outline-primary ms-3">Voir</router-link>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.list-group-item {
  border-radius: 8px;
  border: 1px solid #eee;
}
.event-text {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  max-height: 3em;
  line-height: 1.5em;
}
h2.mb-3 {
  border-radius: 8px;
}
</style>
