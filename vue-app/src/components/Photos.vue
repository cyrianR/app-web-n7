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
          this.files = response.data;
        })
        .catch(error => {
          console.error('Erreur lors de la récupération des fichiers:', error);
        });
    },
    uploadFile() {
      const fileInput = this.$refs.fileInput;
      const formData = new FormData();
      formData.append('file', fileInput.files[0]);

      axios.post('http://localhost:8080/api/file/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      .then(() => {
        this.fetchFiles();
        fileInput.value = null; // Reset the file input
      })
      .catch(error => {
        console.error('Erreur lors de l’upload du fichier:', error);
      });
    }
    getFileUrl(id) {
      return `http://localhost:8080/api/getfile/${id}`;
    }
  }
};
</script>

<template>
  <div>
    <h1>Photos</h1>

    <!-- Bouton d’upload -->
    <form @submit.prevent="uploadFile">
      <input type="file" ref="fileInput" accept="image/*" />
      <button type="submit">Upload</button>
    </form>

    <!-- Affichage des photos -->
    <div v-if="files.length === 0">Aucune photo pour le moment.</div>
    <div v-else>
        <div v-for="file in files" :key="file.id">
            <img :src="getFileUrl(file.id)" width="200" alt="photo" />
        </div>
    </div>
  </div>
</template>

<style scoped>
h1 {
  text-align: center;
}
h2 {
  margin-top: 20px;
}
img {
  margin: 10px;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.2);
}
</style>