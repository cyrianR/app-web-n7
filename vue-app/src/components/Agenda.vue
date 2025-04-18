<script>
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
// import interactionPlugin from '@fullcalendar/interaction'

export default {
  components: {
    FullCalendar
  },
  data() {
    return {
      calendarOptions: {
        plugins: [ dayGridPlugin],
        initialView: 'dayGridMonth',
        firstDay: 1,
        locale: 'fr',
        height: 'auto',
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
  }
}
</script>
<template>
  <FullCalendar :options="calendarOptions" />
</template>