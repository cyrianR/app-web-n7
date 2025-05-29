<script>
import EventService from '../services/EventService';
import LikeService from '../services/LikeService';

export default {
  data() {
    return {
      event : [],
      user : this.$store.state.auth.user,
      liked : false
    };
  },

  created() {
    this.retrieveEvent();
    this.retrieveUserLike();
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
</div>
</template>


<style scoped>

</style>