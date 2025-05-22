<template>
  <div>
    <h2>Liste des Leçons</h2>
    <div v-if="lessons.length === 0">
      <p>Aucune leçon trouvée.</p>
    </div>
    <ul v-else style="list-style-type: none; padding: 0;">
      <li v-for="lesson in lessons" :key="lesson.id" class="lesson-item">
        <h3>{{ lesson.title }}</h3>
        <div>
          <strong>Fichier principal :</strong>
          <span v-if="lesson.file">
            <a :href="lesson.file" target="_blank" rel="noopener">{{ lesson.file }}</a>
          </span>
          <span v-else>Non disponible</span>
        </div>
        <div>
          <strong>Fichier vocabulaire :</strong>
          <span v-if="lesson.vocabFile">
            <a :href="lesson.vocabFile" target="_blank" rel="noopener">{{ lesson.vocabFile }}</a>
          </span>
          <span v-else>Non disponible</span>
        </div>
        <div>
          <strong>Fichier exercices :</strong>
          <span v-if="lesson.exFile">
            <a :href="lesson.exFile" target="_blank" rel="noopener">{{ lesson.exFile }}</a>
          </span>
          <span v-else>Non disponible</span>
        </div>
        <div>
          <strong>Fichier culturel :</strong>
          <span v-if="lesson.culturalFile">
            <a :href="lesson.culturalFile" target="_blank" rel="noopener">{{ lesson.culturalFile }}</a>
          </span>
          <span v-else>Non disponible</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import LessonService from "../services/LessonService";

export default {
  name: "LessonList",
  data() {
    return {
      lessons: []
    };
  },
  mounted() {
    LessonService.getAll()
      .then(response => {
        this.lessons = response.data;
      })
      .catch(() => {
        this.lessons = [];
      });
  }
};
</script>

<style scoped>
.lesson-item {
  border: 1px solid #ccc;
  padding: 1em;
  margin-bottom: 1em;
  border-radius: 5px;
}
</style>