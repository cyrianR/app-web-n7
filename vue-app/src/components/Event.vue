<script>
import EventService from '../services/EventService';
import VoteService from '../services/VoteService';

export default {
  data() {
    return {
      event : [],
      vote : [],
      voted : 0,
      myvote : {
        event: [],
        user: []
      },
    };
  },

  created() {
    this.retrieveEvent();
    this.retrieveUserVote();
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

    retrieveUserVote() {
      const user = this.$store.state.auth.user;
      if (!user) {
        return;
      }
      VoteService.getVoteByUserAndEvent(user.id, this.$route.params.id)
        .then(response => {
          // user has voted
          this.vote = response.data;
          this.voted = 1;
        })
       .catch(error => {
          // user has not voted
        });
    },

    changeVote(){
      const user = this.$store.state.auth.user;
      if (!user) {
        return;
      }
      this.myvote.event = this.event;
      this.myvote.user = this.$store.state.auth.user;

      if (this.voted === 1) {
        VoteService.deleteVote(myvote)
          .then(() => {
            this.retrieveEvent();
          })
          .catch(error => {
            console.error("Error deleting vote:", error);
          });
          this.voted = 0;
      } else {
        VoteService.addVote(this.myvote)
          .then(() => {
            this.retrieveEvent();
          })
          .catch(error => {
            console.error("Error creating vote:", error);
          });
          this.voted = 1;
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
              
              <button @click="changeVote" class="btn">
                <i class="bi bi-heart-fill text-danger me-1" v-if="voted"></i>
                <i class="bi bi-heart me-1" v-else></i>
                {{ event.note }}
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