<script>
import axios from 'axios';

export default {
  data() {
    return {
      files: []
    };
  },
  mounted() {
    this.fetchFiles();
  },
  methods: {
    fetchFiles() {
      axios.get('http://localhost:8080/api/files')
        .then(response => {
          this.files = response.data.reverse(); // Affiche les plus récentes d'abord
        })
        .catch(error => {
          console.error('Erreur lors de la récupération des fichiers:', error);
        });
    },
    uploadFile(event) {
      const file = event.target.files[0];
      if (!file) return;

      const formData = new FormData();
      formData.append('file', file);

      axios.post('http://localhost:8080/api/file/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      .then(() => {
        this.fetchFiles();
        this.$refs.fileInput.value = null;
      })
      .catch(error => {
        console.error("Erreur lors de l'upload :", error);
      });
    },
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    getFileUrl(id) {
      return `http://localhost:8080/api/getfile/${id}`;
    },
    deleteFile(id) {
    axios.delete(`http://localhost:8080/api/file/delete/${id}`)
      .then(() => {
        this.fetchFiles(); // Refresh after deletion
      })
      .catch(error => {
        console.error("Erreur lors de la suppression :", error);
      });
    }
  }
};
</script>

<template>
  <div class="photos-container">
    <h1>📷 Galerie de Photos</h1>

    <!-- Upload Button -->
    <div class="upload-section">
      <input type="file" ref="fileInput" accept="image/*" @change="uploadFile" style="display: none;" />
      <button class="upload-btn" @click="triggerFileInput">Ajouter une photo</button>
    </div>

    <!-- Galerie -->
    <div v-if="files.length === 0" class="no-photos">Aucune photo pour le moment.</div>
    <div v-else class="photo-grid">
      <div class="photo-item" v-for="file in files" :key="file.id">
      <img :src="getFileUrl(file.id)" alt="photo" />
      <button class="delete-icon" @click="deleteFile(file.id)">×</button>
      </div>
    </div>
  </div>
</template>


<style scoped>
.photos-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
}

.upload-section {
  margin-bottom: 20px;
}

.upload-btn {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #4f46e5;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.2s ease;
}

.upload-btn:hover {
  background-color: #4338ca;
}

.photo-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 3 colonnes */
  gap: 20px;
  justify-content: center; /* centre la grille dans son conteneur */
  margin-top: 20px;
}

.photo-card img {
  width: 100%;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.15);
  object-fit: cover;
}
.photo-item {
  position: relative;
  width: 100%;
  max-width: 250px;
}

.photo-item img {
  width: 100%;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
  display: block;
}

.no-photos {
  font-style: italic;
  color: gray;
}
@media (max-width: 768px) {
  .photo-grid {
    grid-template-columns: repeat(2, 1fr); /* 2 colonnes en tablette/mobile */
  }
}

@media (max-width: 480px) {
  .photo-grid {
    grid-template-columns: 1fr; /* 1 colonne sur petit mobile */
  }
}

.delete-icon {
  position: absolute;
  top: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  border: none;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  font-size: 16px;
  line-height: 24px;
  text-align: center;
  cursor: pointer;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.photo-item:hover .delete-icon {
  opacity: 1;
}


</style>