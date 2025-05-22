<template>
  <div>
    <h2>Liste des Leçons</h2>
    <div v-if="isAdmin" class="add-lesson-form">
      <h3>Ajouter une leçon</h3>
      <form @submit.prevent="addLesson">
        <input v-model="newLesson.title" placeholder="Titre" required />
        <input v-model="newLesson.file" placeholder="URL fichier principal" />
        <input v-model="newLesson.vocabFile" placeholder="URL fichier vocabulaire" />
        <input v-model="newLesson.exFile" placeholder="URL fichier exercices" />
        <input v-model="newLesson.culturalFile" placeholder="URL fichier culturel" />
        <button type="submit">Ajouter</button>
      </form>
    </div>
    <div v-if="lessons.length === 0">
      <p>Aucune leçon trouvée.</p>
    </div>
    <ul v-else class="lesson-list">
      <li
        v-for="(lesson, idx) in lessons"
        :key="lesson.id"
        class="lesson-item"
        :draggable="isAdmin"
        @dragstart="onDragStart(idx)"
        @dragover.prevent="onDragOver(idx)"
        @drop="onDrop(idx)"
        :class="{ dragging: dragIndex === idx }"
      >
        <div class="drag-handle" v-if="isAdmin" title="Déplacer">&#9776;</div>
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
        <div class="lesson-actions" v-if="isAdmin">
          <button
            @click="openUpdateModal(lesson)"
            class="update-btn small-btn"
          >
            Modifier
          </button>
          <button
            @click="removeLesson(lesson.id)"
            class="remove-btn small-btn"
          >
            Supprimer
          </button>
        </div>
      </li>
    </ul>

    <!-- Modal de modification -->
    <div v-if="showUpdateModal" class="modal-overlay">
      <div class="modal-content">
        <h3>Modifier la leçon</h3>
        <form @submit.prevent="updateLesson">
          <input v-model="lessonToUpdate.title" placeholder="Titre" required />
          <input v-model="lessonToUpdate.file" placeholder="URL fichier principal" required/>
          <input v-model="lessonToUpdate.vocabFile" placeholder="URL fichier vocabulaire" />
          <input v-model="lessonToUpdate.exFile" placeholder="URL fichier exercices" />
          <input v-model="lessonToUpdate.culturalFile" placeholder="URL fichier culturel" required/>
          <div class="modal-actions">
            <button type="submit" class="update-btn small-btn">Enregistrer</button>
            <button type="button" @click="closeUpdateModal" class="remove-btn small-btn">Annuler</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import LessonService from "../services/LessonService";

export default {
  name: "LessonList",
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
      LessonService.create(this.newLesson)
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
.lesson-list {
  list-style-type: none;
  padding: 0;
}
.lesson-item {
  border: 1px solid #ccc;
  padding: 1em;
  margin-bottom: 1em;
  border-radius: 5px;
  background: #fff;
  display: flex;
  flex-direction: column;
  cursor: grab;
  position: relative;
}
.lesson-item.dragging {
  opacity: 0.5;
}
.drag-handle {
  cursor: grab;
  font-size: 1.5em;
  margin-right: 0.7em;
  user-select: none;
  display: inline-block;
}
.lesson-actions {
  display: flex;
  gap: 0.5em;
  margin-top: 0.5em;
  justify-content: center;
}
.small-btn {
  padding: 0.2em 0.7em;
  font-size: 1em;
  border-radius: 3px;
  min-width: 90px;
}
.remove-btn {
  background: #e74c3c;
  color: white;
  border: none;
  cursor: pointer;
}
.remove-btn:hover {
  background: #c0392b;
}
.update-btn {
  background: #2980b9;
  color: white;
  border: none;
  cursor: pointer;
}
.update-btn:hover {
  background: #1c5d8c;
}
.add-lesson-form {
  margin-bottom: 2em;
  padding: 1em;
  border: 1px solid #b3b3b3;
  border-radius: 5px;
  background: #f8f8f8;
}
.add-lesson-form input {
  margin-right: 0.5em;
  margin-bottom: 0.5em;
  padding: 0.3em;
}
.add-lesson-form button {
  background: #27ae60;
  color: white;
  border: none;
  padding: 0.5em 1em;
  border-radius: 3px;
  cursor: pointer;
}
.add-lesson-form button:hover {
  background: #219150;
}
.modal-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background: white;
  padding: 2em;
  border-radius: 8px;
  min-width: 300px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.2);
}
.modal-content input {
  margin-bottom: 1em;
  width: 100%;
  padding: 0.5em;
}
.modal-actions {
  display: flex;
  gap: 0.5em;
  justify-content: center;
}
</style>