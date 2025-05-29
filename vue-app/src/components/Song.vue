
<script>
import SongService from "../services/SongService";
export default {
    name: "Song",
    data() {
        return {
            songs: [],
            search: "",
            newSong: {
                title: "",
                artist: ""
            }
        };
    },
    computed: {
        filteredSongs() {
            const term = this.search.trim().toLowerCase();
            if (!term) return this.songs;
            return this.songs.filter(song =>
                song.title.toLowerCase().includes(term)
            );
        }
    },
    mounted() {
        this.fetchSongs();
    },
    methods: {
        fetchSongs() {
            // Simulate fetching songs from an API
            SongService.getAll()
                .then(response => {
                    this.songs = response.data;
                })
                .catch(error => {
                    console.error("Error fetching songs:", error);
                });
        },
        addSong(newSong) {
            if (!newSong.title || !newSong.artist) return;
            console.log("Adding song:", newSong);
            SongService.create(newSong)
                .then(response => {
                    this.songs.push(response.data);
                    this.newSong.title = "";
                    this.newSong.artist = "";
                })
                .catch(error => {
                    console.error("Error adding song:", error);
                });
        },
        deleteSong(songId) {
            SongService.delete(songId)
                .then(() => {
                    this.songs = this.songs.filter(song => song.id !== songId);
                })
                .catch(error => {
                    console.error("Error deleting song:", error);
                });
        },
        getSongsByTitle(title) {
            return this.songs.filter(song =>
                song.title.toLowerCase().includes(title.toLowerCase())
            );
        }
    }
};

    ;

</script>

<template>
    <div class="song-container">
        <h1>🎵 Liste des chansons</h1>
        <input
            v-model="search"
            type="text"
            placeholder="Rechercher une chanson..."
            class="form-control mb-3"
        />
        <ul class="list-group mb-3">
            <li
                v-for="song in filteredSongs"
                :key="song.id"
                class="list-group-item d-flex justify-content-between align-items-center"
            >
                <div>
                    <strong>{{ song.title }}</strong> - {{ song.artist }}
                </div>
                <button
                    class="btn btn-danger btn-sm"
                    @click="deleteSong(song.id)"
                >
                    Supprimer
                </button>
            </li>
        </ul>
        <h2>Ajouter une nouvelle chanson</h2>  
        <form @submit.prevent="addSong(newSong)">
            <div class="mb-3">
                <input
                    v-model="newSong.title"
                    type="text"
                    placeholder="Titre de la chanson"
                    class="form-control"
                />
            </div>
            <div class="mb-3">
                <input
                    v-model="newSong.artist"
                    type="text"
                    placeholder="Artiste"
                    class="form-control"
                />
            </div>
            <button type="submit" class="btn btn-primary">Ajouter</button>
        </form>
        <h3> Liste des chansons </h3>
        <ul class="list-group mt-3">
            <li
                v-for="song in getSongsByTitle(search)"
                :key="song.id"
                class="list-group-item d-flex justify-content-between align-items-center"
            >
                <div>
                    <strong>{{ song.title }}</strong> - {{ song.artist }}
                </div>
                <button
                    class="btn btn-danger btn-sm"
                    @click="deleteSong(song.id)"
                >
                    Supprimer
                </button>
            </li>
        </ul>
        <p v-if="filteredSongs.length === 0" class="text-center">Aucune chanson trouvée.</p>
        <p v-if="songs.length === 0" class="text-center">Aucune chanson disponible.</p>
        <p v-if="songs.length > 0" class="text-center">Total des chansons: {{ songs.length }}</p>
    </div>
</template>
<style scoped>
.song-container {
    max-width: 800px;
    margin: 30px auto;
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 8px;
}
.song-container h1 {
    text-align: center;
    margin-bottom: 20px;
}
.song-container input[type="text"] {
    width: 100%;
    padding: 10px;
    border-radius: 4px;
    border: 1px solid #ced4da;
}
.song-container .list-group-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.song-container .btn {
    margin-left: 10px;
}
.song-container form {
    margin-top: 20px;
}
.song-container form input[type="text"] {
    margin-bottom: 10px;
}
.song-container form button {
    width: 100%;
}
.song-container .form-control {
    margin-bottom: 10px;
}
.song-container .btn-primary {
    width: 100%;
}
.song-container .btn-danger {
    width: 100px;
}
.song-container p {
    text-align: center;
    color: #6c757d;
}
</style>