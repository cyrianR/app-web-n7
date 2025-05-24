<template>
  <div class="anime-gallery">
    <h2>Anime en cours</h2>
    <div class="gallery-section">
      <div v-if="notFinishedAnimes.length === 0" class="empty-msg">No ongoing anime.</div>
      <div v-else class="gallery-grid">
        <div v-if="isAdmin" class="anime-card add-anime-card" @click="openAddModal" title="Ajouter un anime">
          <span class="add-plus">+</span>
        </div>
        <div v-for="anime in notFinishedAnimes" :key="anime.id" class="anime-card" :style="anime.coverUrl ? { backgroundImage: `url(${anime.coverUrl})` } : {}">
          <div class="anime-title">{{ anime.name }}</div>
          <div class="anime-info">Episodes: {{ anime.currentEpisode }} / {{ anime.nbEpisodes }}</div>
          <div v-if="anime.malScore !== null" class="anime-info">MAL Score: {{ anime.malScore }}</div>
          <a :href="anime.malLink" target="_blank" class="mal-link"><strong>View on MAL</strong></a>
          <button v-if="isAdmin" class="update-arrow" @click.stop="openUpdateModal(anime)" title="Modifier">↑</button>
          <button v-if="isAdmin" class="delete-cross" @click.stop="deleteAnime(anime.id)" title="Supprimer">✕</button>
        </div>
      </div>
    </div>
    <h2>Anime finis</h2>
    <div class="gallery-section">
      <div v-if="finishedAnimes.length === 0" class="empty-msg">No finished anime.</div>
      <div v-else class="gallery-grid">
        <div v-for="anime in finishedAnimes" :key="anime.id" class="anime-card" :style="anime.coverUrl ? { backgroundImage: `url(${anime.coverUrl})` } : {}">
          <div class="anime-title">{{ anime.name }}</div>
          <div v-if="anime.malScore !== null" class="anime-info">MAL Score: {{ anime.malScore }}</div>
          <a :href="anime.malLink" target="_blank" class="mal-link"><strong>View on MAL</strong></a>
          <button v-if="isAdmin" class="update-arrow" @click="openUpdateModal(anime)" title="Modifier">↑</button>
          <button v-if="isAdmin" class="delete-cross" @click="deleteAnime(anime.id)" title="Supprimer">✕</button>
        </div>
      </div>
    </div>
    <!-- Update Anime Modal -->
    <div v-if="showUpdateModal" class="modal-overlay">
      <div class="modal-content">
        <h3>Modifier l'anime</h3>
        <form @submit.prevent="updateAnime">
          <input v-model="animeToUpdate.name" placeholder="Nom" required />
          <input v-model.number="animeToUpdate.nbEpisodes" placeholder="Nombre d'épisodes" type="number" min="1" required />
          <input v-model.number="animeToUpdate.currentEpisode" placeholder="Épisode actuel" type="number" min="0" required />
          <input v-model="animeToUpdate.malLink" placeholder="Lien MAL" required />
          <div class="modal-actions">
            <button type="submit" class="update-btn btn">Enregistrer</button>
            <button type="button" @click="closeUpdateModal" class="remove-btn btn">Annuler</button>
          </div>
        </form>
      </div>
    </div>
    <!-- Add Anime Modal -->
    <div v-if="showAddModal" class="modal-overlay">
      <div class="modal-content">
        <h3>Ajouter un anime</h3>
        <form @submit.prevent="addAnime">
          <input v-model="newAnime.name" placeholder="Nom" required @input="searchMalAnime" autocomplete="off" />
          <ul v-if="malSuggestions.length > 0" class="mal-suggestions">
            <li v-for="suggestion in malSuggestions" :key="suggestion.mal_id" @click.prevent="selectMalSuggestion(suggestion)">
              <img :src="suggestion.images.jpg.image_url" alt="cover" class="mal-suggestion-img" />
              <span>{{ suggestion.title }} <span v-if="suggestion.episodes">({{ suggestion.episodes }} ep)</span></span>
            </li>
          </ul>
          <input v-model.number="newAnime.nbEpisodes" placeholder="Nombre d'épisodes" type="number" min="1" required />
          <input v-model.number="newAnime.currentEpisode" placeholder="Épisode actuel" type="number" min="0" required />
          <input v-model="newAnime.malLink" placeholder="Lien MAL" required />
          <div class="modal-actions">
            <button type="submit" class="update-btn btn">Ajouter</button>
            <button type="button" @click="closeAddModal" class="remove-btn btn">Annuler</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import ProjService from '../services/ProjService';

export default {
  name: 'Proj',
  data() {
    return {
      finishedAnimes: [],
      notFinishedAnimes: [],
      showUpdateModal: false,
      showAddModal: false,
      animeToUpdate: {
        id: null,
        name: '',
        nbEpisodes: 1,
        currentEpisode: 0,
        malLink: ''
      },
      newAnime: {
        name: '',
        nbEpisodes: 1,
        currentEpisode: 0,
        malLink: ''
      },
      malSuggestions: [],
      malSearchTimeout: null,
    };
  },
  computed: {
    isAdmin() {
      const user = this.$store?.state?.auth?.user;
      return user && (user.roles.includes('ROLE_ADMIN'));
    }
  },
  mounted() {
    this.fetchAnimes();
  },
  methods: {
    async getAnimeCoverAndScore(malLink) {
      const match = malLink.match(/anime\/(\d+)/);
      if (!match) return { coverUrl: '', score: null };
      const animeId = match[1];
      try {
        const res = await fetch(`https://api.jikan.moe/v4/anime/${animeId}`);
        const data = await res.json();
        return {
          coverUrl: data.data.images.jpg.large_image_url,
          score: data.data.score
        };
      } catch {
        return { coverUrl: '', score: null };
      }
    },
    async fetchCovers(animeList) {
      for (const anime of animeList) {
        const { coverUrl, score } = await this.getAnimeCoverAndScore(anime.malLink);
        anime.coverUrl = coverUrl;
        anime.malScore = score;
      }
    },
    async fetchAnimes() {
      try {
        const finishedRes = await ProjService.getAllFinishedAnimes();
        this.finishedAnimes = finishedRes.data;
        await this.fetchCovers(this.finishedAnimes);
      } catch {
        this.finishedAnimes = [];
      }
      try {
        const notFinishedRes = await ProjService.getAllNotFinishedAnimes();
        this.notFinishedAnimes = notFinishedRes.data;
        await this.fetchCovers(this.notFinishedAnimes);
      } catch {
        this.notFinishedAnimes = [];
      }
    },
    deleteAnime(id) {
      if (confirm('Voulez-vous vraiment supprimer cet anime ?')) {
        this.$options.$_ProjService.deleteAnime(id)
          .then(() => {
            this.finishedAnimes = this.finishedAnimes.filter(anime => anime.id !== id);
            this.notFinishedAnimes = this.notFinishedAnimes.filter(anime => anime.id !== id);
          })
          .catch(() => {
            alert('Erreur lors de la suppression.');
          });
      }
    },
    openUpdateModal(anime) {
      this.animeToUpdate = { ...anime };
      this.showUpdateModal = true;
    },
    closeUpdateModal() {
      this.showUpdateModal = false;
      this.animeToUpdate = {
        id: null,
        name: '',
        nbEpisodes: 1,
        currentEpisode: 0,
        malLink: ''
      };
    },
    updateAnime() {
      if (!this.animeToUpdate.name) {
        alert("Le nom est obligatoire.");
        return;
      }
      if (!this.animeToUpdate.nbEpisodes || this.animeToUpdate.nbEpisodes < 1) {
        alert("Le nombre d'épisodes est obligatoire et doit être supérieur à 0.");
        return;
      }
      if (this.animeToUpdate.currentEpisode > this.animeToUpdate.nbEpisodes) {
        alert("L'épisode actuel ne peut pas être supérieur au nombre total d'épisodes.");
        return;
      }
      if (this.animeToUpdate.currentEpisode < 0) {
        alert("L'épisode actuel doit être positif.");
        return;
      }
      if (!this.animeToUpdate.malLink) {
        alert("Le lien MAL est obligatoire.");
        return;
      }
      this.$options.$_ProjService.updateAnime(this.animeToUpdate.id, this.animeToUpdate)
        .then(response => {
          // Replace in both lists if present
          const updateList = list => {
            const idx = list.findIndex(a => a.id === this.animeToUpdate.id);
            if (idx !== -1) list[idx] = response.data;
          };
          updateList(this.finishedAnimes);
          updateList(this.notFinishedAnimes);
          this.closeUpdateModal();
          this.fetchAnimes();
        })
        .catch(() => {
          alert("Erreur lors de la mise à jour.");
        });
    },
    openAddModal() {
      this.showAddModal = true;
      this.newAnime = {
        name: '',
        nbEpisodes: 1,
        currentEpisode: 0,
        malLink: ''
      };
    },
    closeAddModal() {
      this.showAddModal = false;
      this.newAnime = {
        name: '',
        nbEpisodes: 1,
        currentEpisode: 0,
        malLink: ''
      };
    },
    async searchMalAnime() {
      clearTimeout(this.malSearchTimeout);
      if (!this.newAnime.name || this.newAnime.name.length < 2) {
        this.malSuggestions = [];
        return;
      }
      this.malSearchTimeout = setTimeout(async () => {
        try {
          const res = await fetch(`https://api.jikan.moe/v4/anime?q=${encodeURIComponent(this.newAnime.name)}&limit=5`);
          const data = await res.json();
          this.malSuggestions = data.data || [];
        } catch {
          this.malSuggestions = [];
        }
      }, 350);
    },
    selectMalSuggestion(suggestion) {
      this.newAnime.name = suggestion.title;
      this.newAnime.nbEpisodes = suggestion.episodes || 1;
      this.newAnime.malLink = `https://myanimelist.net/anime/${suggestion.mal_id}`;
      this.malSuggestions = [];
    },
    addAnime() {
      if (!this.newAnime.name) {
        alert("Le nom est obligatoire.");
        return;
      }
      if (!this.newAnime.nbEpisodes || this.newAnime.nbEpisodes < 1) {
        alert("Le nombre d'épisodes est obligatoire et doit être supérieur à 0.");
        return;
      }
      if (this.newAnime.currentEpisode > this.newAnime.nbEpisodes) {
        alert("L'épisode actuel ne peut pas être supérieur au nombre total d'épisodes.");
        return;
      }
      if (this.newAnime.currentEpisode < 0) {
        alert("L'épisode actuel doit être positif.");
        return;
      }
      if (!this.newAnime.malLink) {
        alert("Le lien MAL est obligatoire.");
        return;
      }
      this.$options.$_ProjService.createAnime(this.newAnime)
        .then(() => {
          this.closeAddModal();
          this.fetchAnimes();
        })
        .catch(() => {
          alert("Erreur lors de l'ajout de l'anime.");
        });
    },
  },
  // Provide ProjService for deleteAnime method
  $_ProjService: ProjService,
}
</script>

<style scoped>
.anime-gallery {
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem 1rem;
}
.gallery-section {
  margin-bottom: 2.5rem;
}
.gallery-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
}
.anime-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  padding: 1.2rem 1.5rem;
  min-width: 220px;
  max-width: 220px;
  min-height: 280px;
  max-height: 280px;
  flex: 1 1 220px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  transition: box-shadow 0.2s;
  background-size: cover;
  background-position: center;
  color: #fff;
  position: relative;
  z-index: 1;
  overflow: hidden;
}
.anime-card::before {
  content: '';
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.55);
  z-index: 0;
}
.anime-title, .anime-info, .mal-link {
  position: relative;
  z-index: 1;
}
.anime-card:hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.15);
}
.anime-title {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  text-shadow: 0 4px 2px rgba(0,0,0,0.5);
}
.anime-info {
  font-size: 1rem;
  margin-bottom: 0.5rem;
}
.mal-link {
  color: #1c7ed6;
  text-decoration: underline;
  font-size: 0.95rem;
}
.empty-msg {
  color: #888;
  font-style: italic;
  margin: 1rem 0;
}
.update-arrow {
  position: absolute;
  right: 48px;
  bottom: 10px;
  background: rgba(10, 130, 210, 0.75);
  color: #fff;
  border: none;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  font-size: 1.2em;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 2;
  transition: background 0.2s;
}
.update-arrow:hover {
  background: #5ea7db;
}
.delete-cross {
  position: absolute;
  right: 10px;
  bottom: 10px;
  background: rgba(185, 4, 4, 0.75);
  color: #fff;
  border: none;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  font-size: 1.2em;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 2;
  transition: background 0.2s;
}
.delete-cross:hover {
  background: #c0392b;
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
.add-anime-card {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f3f3f3;
  color: #2980b9;
  border: 2px dashed #2980b9;
  cursor: pointer;
  min-width: 220px;
  max-width: 220px;
  min-height: 280px;
  max-height: 280px;
  font-size: 3em;
  font-weight: bold;
  position: relative;
  transition: background 0.2s, border 0.2s;
}
.add-anime-card:hover {
  background: #eaf6fb;
  border-color: #1c5d8c;
}
.add-plus {
  font-size: 3em;
  line-height: 1;
  user-select: none;
}
.mal-suggestions {
  list-style: none;
  margin: 0 0 0.5em 0;
  padding: 0;
  background: #fff;
  border: 1px solid #b3b3b3;
  border-radius: 5px;
  max-height: 200px;
  overflow-y: auto;
  position: absolute;
  z-index: 10;
  width: 90%;
  left: 5%;
}
.mal-suggestions li {
  display: flex;
  align-items: center;
  gap: 0.7em;
  padding: 0.4em 0.7em;
  cursor: pointer;
  transition: background 0.15s;
}
.mal-suggestions li:hover {
  background: #eaf6fb;
}
.mal-suggestion-img {
  width: 32px;
  height: 45px;
  object-fit: cover;
  border-radius: 3px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}
</style>
