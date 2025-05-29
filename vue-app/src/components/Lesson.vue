<template>
  <div class="container my-4">
    <EventTypedList eventType="LESSON" class="mb-3" />
    <h2 class="mb-4">Liste des Leçons</h2>

    <!-- Add Lesson Form (Admin Only) -->
    <div v-if="isAdmin" class="card mb-4">
      <div class="card-header">
        <h3 class="mb-0">Ajouter une leçon</h3>
      </div>
      <div class="card-body">
        <form @submit.prevent="addLesson" class="row g-2 align-items-end">
          <div class="col-md">
            <input v-model="newLesson.title" class="form-control" placeholder="Titre" required />
          </div>
          <div class="col-md">
            <input v-model="newLesson.file" class="form-control" placeholder="URL fichier principal" />
          </div>
          <div class="col-md">
            <input v-model="newLesson.vocabFile" class="form-control" placeholder="URL fichier vocabulaire" />
          </div>
          <div class="col-md">
            <input v-model="newLesson.exFile" class="form-control" placeholder="URL fichier exercices" />
          </div>
          <div class="col-md">
            <input v-model="newLesson.culturalFile" class="form-control" placeholder="URL fichier culturel" />
          </div>
          <div class="col-auto">
            <button type="submit" class="btn btn-success">Ajouter</button>
          </div>
        </form>
      </div>
    </div>

    <!-- No Lessons Message -->
    <div v-if="lessons.length === 0" class="alert alert-info">
      Aucune leçon trouvée.
    </div>

    <!-- Lessons List -->
    <ul v-else class="list-group">
      <li
        v-for="(lesson, idx) in lessons"
        :key="lesson.id"
        class="list-group-item d-flex flex-column flex-md-row align-items-center justify-content-between"
        :draggable="isAdmin"
        @dragstart="onDragStart(idx)"
        @dragover.prevent="onDragOver(idx)"
        @drop="onDrop(idx)"
        :class="{ 'opacity-50': dragIndex === idx }"
      >
        <div class="d-flex align-items-center mb-2 mb-md-0 w-100">
          <span v-if="isAdmin" class="me-3 fs-4 text-secondary" style="cursor: grab;" title="Déplacer">&#9776;</span>
          <div>
            <strong>{{ lesson.title }} : </strong>
            <template v-if="lesson.file">
              <a :href="lesson.file" target="_blank" rel="noopener">Cours</a>
            </template>
            <template v-if="lesson.vocabFile">
              | <a :href="lesson.vocabFile" target="_blank" rel="noopener">Vocabulaire</a>
            </template>
            <template v-if="lesson.exFile">
              | <a :href="lesson.exFile" target="_blank" rel="noopener">Exercices</a>
            </template>
            <template v-if="lesson.culturalFile">
              | <a :href="lesson.culturalFile" target="_blank" rel="noopener">Culture</a>
            </template>
          </div>
        </div>
        <div v-if="isAdmin" class="d-flex gap-2 mt-2 mt-md-0">
          <button @click="openUpdateModal(lesson)" class="btn btn-primary btn-sm">Modifier</button>
          <button @click="removeLesson(lesson.id)" class="btn btn-danger btn-sm">Supprimer</button>
        </div>
      </li>
    </ul>

    <!-- Update Modal -->
    <div v-if="showUpdateModal" class="modal fade show d-block" tabindex="-1" style="background:rgba(0,0,0,0.4);">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h3 class="modal-title">Modifier la leçon</h3>
            <button type="button" class="btn-close" @click="closeUpdateModal"></button>
          </div>
          <form @submit.prevent="updateLesson">
            <div class="modal-body">
              <input v-model="lessonToUpdate.title" class="form-control mb-2" placeholder="Titre" required />
              <input v-model="lessonToUpdate.file" class="form-control mb-2" placeholder="URL fichier principal" required />
              <input v-model="lessonToUpdate.vocabFile" class="form-control mb-2" placeholder="URL fichier vocabulaire" />
              <input v-model="lessonToUpdate.exFile" class="form-control mb-2" placeholder="URL fichier exercices" />
              <input v-model="lessonToUpdate.culturalFile" class="form-control mb-2" placeholder="URL fichier culturel" required />
            </div>
            <div class="modal-footer">
              <button type="submit" class="btn btn-primary">Enregistrer</button>
              <button type="button" @click="closeUpdateModal" class="btn btn-secondary">Annuler</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LessonService from "../services/LessonService";
import EventTypedList from './EventTypedList.vue';

export default {
  name: "LessonList",
  components: {
    EventTypedList
  },
  data() {
    return {
      lessons: [],
      userRoles: [],
      newLesson: {
        title: "",
        file: "",
        vocabFile: "",
        exFile: "",
        culturalFile: ""
      },
      showUpdateModal: false,
      lessonToUpdate: {
        id: null,
        title: "",
        file: "",
        vocabFile: "",
        exFile: "",
        culturalFile: ""
      },
      dragIndex: null,
      dragOverIndex: null
    };
  },
  computed: {
    isAdmin() {
      return (
        this.userRoles.includes("ROLE_ADMIN") ||
        this.userRoles.includes("ROLE_LESSON_ADMIN")
      );
    }
  },
  methods: {
    fetchLessons() {
      if (!this.$store.state.auth.user) {
        return;
      }
      LessonService.getAll()
        .then(response => {
          this.lessons = response.data.sort((a, b) => (a.orderNum ?? 9999) - (b.orderNum ?? 9999));
        })
        .catch(() => {
          this.lessons = [];
        });
    },
    removeLesson(id) {
      if (confirm("Voulez-vous vraiment supprimer cette leçon ?")) {
        LessonService.delete(id)
          .then(() => {
            this.lessons = this.lessons.filter(lesson => lesson.id !== id);
          })
          .catch(() => {
            alert("Erreur lors de la suppression.");
          });
      }
    },
    fetchUserRoles() {
      const user = this.$store.state.auth.user;
      if (!user) {
        return;
      }
      this.userRoles = user.roles;
    },
    addLesson() {
      if (!this.newLesson.title) {
        alert("Le titre est obligatoire.");
        return;
      }
      if (!this.newLesson.file) {
        alert("Le fichier principal est obligatoire.");
        return;
      }
      if (!this.newLesson.culturalFile) {
        alert("Le fichier culturel est obligatoire.");
        return;
      }
      LessonService.createFull(this.newLesson)
        .then(response => {
          this.lessons.push(response.data);
          this.newLesson = {
            title: "",
            file: "",
            vocabFile: "",
            exFile: "",
            culturalFile: ""
          };
          this.fetchLessons();
        })
        .catch(() => {
          alert("Erreur lors de l'ajout de la leçon.");
        });
    },
    openUpdateModal(lesson) {
      this.lessonToUpdate = { ...lesson };
      this.showUpdateModal = true;
    },
    closeUpdateModal() {
      this.showUpdateModal = false;
      this.lessonToUpdate = {
        id: null,
        title: "",
        file: "",
        vocabFile: "",
        exFile: "",
        culturalFile: ""
      };
    },
    updateLesson() {
      if (!this.lessonToUpdate.title) {
        alert("Le titre est obligatoire.");
        return;
      }
      if (!this.lessonToUpdate.file) {
        alert("Le fichier principal est obligatoire.");
        return;
      }
      if (!this.lessonToUpdate.culturalFile) {
        alert("Le fichier culturel est obligatoire.");
        return;
      }
      LessonService.update(this.lessonToUpdate.id, this.lessonToUpdate)
        .then(response => {
          const idx = this.lessons.findIndex(l => l.id === this.lessonToUpdate.id);
          if (idx !== -1) {
            this.lessons[idx] = response.data;
          }
          this.closeUpdateModal();
          this.fetchLessons();
        })
        .catch(() => {
          alert("Erreur lors de la mise à jour.");
        });
    },
    onDragStart(idx) {
      if (!this.isAdmin) return;
      this.dragIndex = idx;
    },
    onDragOver(idx) {
      if (!this.isAdmin) return;
      this.dragOverIndex = idx;
    },
    onDrop(idx) {
      if (!this.isAdmin || this.dragIndex === null || this.dragIndex === idx) return;
      const movedLesson = this.lessons[this.dragIndex];
      this.lessons.splice(this.dragIndex, 1);
      this.lessons.splice(idx, 0, movedLesson);

      // Met à jour les orderNum pour chaque leçon
      this.lessons.forEach((lesson, i) => {
        lesson.orderNum = i + 1;
      });

      // Envoie la nouvelle position au backend pour toutes les leçons
      this.lessons.forEach(lesson => {
        LessonService.update(lesson.id, lesson);
      });

      this.dragIndex = null;
      this.dragOverIndex = null;
      // Assure que le DOM est mis à jour avant de rafraîchir les leçons
      this.$nextTick(() => {
        setTimeout(() => {
          this.fetchLessons();
        }, 500);
      });
    }
  },
  mounted() {
    this.fetchUserRoles();
    this.fetchLessons();
  }
};
</script>

<style scoped>
.opacity-50 {
  opacity: 0.5;
}
</style>