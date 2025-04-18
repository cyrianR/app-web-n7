<script>
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'

export default {
  components: {
    FullCalendar
  },
  data() {
    return {
      calendarOptions: {
        plugins: [ dayGridPlugin, interactionPlugin],
        initialView: 'dayGridMonth',
        firstDay: 1,
        locale: 'fr',
        editable: false,
        selectable: false,

        height: 'auto',
        buttonText: {
          today: "Mois en cours"
        },
        
        eventClick: (arg) => this.handleEventClick(arg),
        eventDidMount: (info) => {
            info.el.title = 'Cliquez pour plus de détails';
        },
        events: [
        { 
          title: 'Cuisine', 
          start: '2025-04-05',
          backgroundColor: '#fff9c4',
          textColor: '#333',
          borderColor: '#fff9c4'
        },
        { 
          title: 'Projo', 
          start: '2025-04-09',
          backgroundColor: '#f6e6fa',
          textColor: '#333',
          borderColor: '#f6e6fa'
        },
        { 
          title: 'Cours', 
          start: '2025-04-09',
          backgroundColor: '#e0f7fa', 
          textColor: '#333',
          borderColor: '#e0f7fa'
        },
        { 
          title: 'Kara', 
          start: '2025-04-14',
          backgroundColor: '#e0ffe0',
          textColor: '#333',
          borderColor: '#e0ffe0'
        },
        { 
          title: 'Cours', 
          start: '2025-04-24',
          backgroundColor: '#e0f7fa',
          textColor: '#333',
          borderColor: '#e0f7fa'
        }
        ]
      }
    }
  },
  methods: {
    handleEventClick: function(arg) {
      alert(arg.event.title) // TODO : faire un lien vers une page de l'évènement (attribut url de event) et/ou un meilleur pop-up pour l'évènement
    }
  }
}
</script>
<template>
    <h2 class="pt-5">Agenda</h2>
    <FullCalendar :options="calendarOptions" /> 
</template>

<style lang="css">
.fc-event {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.fc-event:hover {
  transform: scale(1.02);
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
  z-index: 100;
  text-decoration: underline;
}

.fc .fc-col-header-cell-cushion {
  color: inherit;
  text-decoration: none;
}

.fc .fc-daygrid-day-number {
  color: inherit;
  text-decoration: none;
}
</style>