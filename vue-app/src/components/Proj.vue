<template>
  <div class="anime-gallery">
    <h2>Ongoing Anime</h2>
    <div class="gallery-section">
      <div v-if="notFinishedAnimes.length === 0" class="empty-msg">No ongoing anime.</div>
      <div v-else class="gallery-grid">
        <div v-for="anime in notFinishedAnimes" :key="anime.id" class="anime-card" :style="anime.coverUrl ? { backgroundImage: `url(${anime.coverUrl})` } : {}">
          <div class="anime-title">{{ anime.name }}</div>
          <div class="anime-info">Episodes: {{ anime.currentEpisode }} / {{ anime.nbEpisodes }}</div>
          <a :href="anime.malLink" target="_blank" class="mal-link"><strong>View on MAL</strong></a>
        </div>
      </div>
    </div>
    <h2>Finished Anime</h2>
    <div class="gallery-section">
      <div v-if="finishedAnimes.length === 0" class="empty-msg">No finished anime.</div>
      <div v-else class="gallery-grid">
        <div v-for="anime in finishedAnimes" :key="anime.id" class="anime-card" :style="anime.coverUrl ? { backgroundImage: `url(${anime.coverUrl})` } : {}">
          <div class="anime-title">{{ anime.name }}</div>
          <a :href="anime.malLink" target="_blank" class="mal-link"><strong>View on MAL</strong></a>
        </div>
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
    };
  },
  mounted() {
    this.fetchAnimes();
  },
  methods: {
    async getAnimeCover(malLink) {
      const match = malLink.match(/anime\/(\d+)/);
      if (!match) return '';
      const animeId = match[1];
      try {
        const res = await fetch(`https://api.jikan.moe/v4/anime/${animeId}`);
        const data = await res.json();
        return data.data.images.jpg.large_image_url;
      } catch {
        return '';
      }
    },
    async fetchCovers(animeList) {
      for (const anime of animeList) {
        anime.coverUrl = await this.getAnimeCover(anime.malLink);
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
    }
  }
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
</style>
