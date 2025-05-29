<template>
    <EventTypedList eventType="COOKING" class="mb-3" />
</template>
<script>
import EventTypedList from './EventTypedList.vue';

export default {
  name: "LessonList",
  components: {
    EventTypedList
  }
};
</script>