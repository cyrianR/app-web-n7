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
    }
  }
};
</script>

<template>
<div class="card">
    <div class="card-body">
      <h4 class="card-title">{{ event.name }}</h4>
      <p class="card-text">{{ event.description }}</p>
      <div class="d-flex justify-content-between align-items-center">
        <div class="btn-group">
          <button href="#" class="btn btn-primary">Lire la suite</button>
          <button href="#" class="btn btn-secondary">Voir sur l'agenda</button>
        </div>
        <div class="text-muted ">
          {{ event.date }}
        </div>
      </div>
    </div>
</div>
</template>


<style scoped>

</style>