<script>
import SongService from "../services/SongService";
import EventTypedList from './EventTypedList.vue';
export default {
    name: "Song",
    components: {
        EventTypedList
    },
    data() {
        return {
            songs: [],
            search: "",
            newSong: {
                title: "",
                songwriter: "",
                url: ""
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
        },
        isAdmin() {
            return (
                this.$store.state.auth.user.roles.includes("ROLE_ADMIN") ||
                this.$store.state.auth.user.roles.includes("ROLE_KARAOKE_ADMIN")
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
            if (!newSong.title || !newSong.songwriter) return;
            console.log("Adding song:", newSong);
            SongService.create(newSong)
                .then(response => {
                    this.songs.push(response.data);
                    this.newSong.title = "";
                    this.newSong.songwriter = "";
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
    <EventTypedList eventType="KARAOKE" class="mb-3" />
    <div class="container my-5 p-4 bg-light rounded shadow">
        <div v-if="isAdmin">
            <h2 class="mt-4">Ajouter une nouvelle chanson</h2>  
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
                        v-model="newSong.songwriter"
                        type="text"
                        placeholder="Artiste"
                        class="form-control"
                    />
                </div>
                <div class="mb-3">
                    <input
                        v-model="newSong.url"
                        type="text"
                        placeholder="Lien vers la chanson"
                        class="form-control"
                    />
                </div>
                <button type="submit" class="btn btn-primary w-100 mb-4">Ajouter</button>
            </form>
        </div>
        <h1 class="text-center mb-4">🎵 Liste des chansons</h1>
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
                    <a :href="song.url"> <strong>{{ song.title }}</strong> </a> - {{ song.songwriter }}
                </div>
                <button
                    v-if="isAdmin"
                    class="btn btn-danger btn-sm"
                    @click="deleteSong(song.id)"
                >
                    Supprimer
                </button>
            </li>
        </ul>
        
        <p v-if="filteredSongs.length === 0" class="text-center text-secondary mt-3">Aucune chanson trouvée.</p>
        <p v-if="songs.length > 0" class="text-center text-secondary mt-3">Total des chansons: {{ songs.length }}</p>
    </div>
</template>