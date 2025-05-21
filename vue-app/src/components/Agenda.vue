<template>
  <h2 class="pt-5">Agenda</h2>
  <FullCalendar :options="calendarOptions" /> 
</template>

<script>
import FullCalendar from '@fullcalendar/vue3'
import bootstrap5Plugin from '@fullcalendar/bootstrap5'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'
import EventService from "../services/EventService"

export default {

  components: {
    FullCalendar
  },

  data() {
    return {
      calendarOptions: {
        plugins: [ bootstrap5Plugin, dayGridPlugin, interactionPlugin],
        initialView: 'dayGridMonth',
        firstDay: 1,
        locale: 'fr',
        editable: false,
        selectable: false,

        themeSystem: 'bootstrap5',

        height: 'auto',
        buttonText: {
          today: "Mois en cours"
        },
        events: (fetchInfo, successCallback, failureCallback) => {
          EventService.getBetween(fetchInfo.startStr, fetchInfo.endStr)
            .then(response => {
              successCallback(response.data.map(event => ({
              title: this.getFormattedEventType(event.eventType),
              start: event.date,
              backgroundColor: this.getColorForEventType(event.eventType),
              textColor: '#333',
              borderColor: this.getColorForEventType(event.eventType)
              })))
            })
            .catch(e => {
              failureCallback("Error retrieving events: ", e)
            })
        },
        eventDisplay: 'block',

        eventClick: (arg) => this.handleEventClick(arg),
        eventDidMount: (info) => {
            info.el.title = 'Cliquez pour plus de détails';
        },
      },
      events: []
    }
  },

  methods: {

    handleEventClick: function(arg) {
      alert(arg.event.title) // TODO : faire un lien vers une page de l'évènement (attribut url de event) et/ou un meilleur pop-up pour l'évènement
    },

    getFormattedEventType(eventType) {
      return EventService.formatEventType(eventType);
    },

    getColorForEventType(eventType) {
      return EventService.colorEvent(eventType);
    }
  }
};

</script>

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

.fc-h-event .fc-event-main-frame {
  flex-direction: row-reverse;
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