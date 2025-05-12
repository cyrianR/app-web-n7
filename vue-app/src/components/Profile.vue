<template>
  <div v-if="user" class="profile-page container mt-5">
    <h1 class="mb-4">Mon Profil</h1>

    <!-- User Information -->
    <div class="card mb-4">
      <div class="card-body">
        <h5 class="card-title">Informations du compte</h5>
        <p><strong>Nom d'utilisateur :</strong> {{ user.username }}</p>
        <p><strong>Email :</strong> {{ user.email }}</p>
        <p><strong>Rôles : </strong>
          <span v-for="role in user.roles" :key="role" class="badge bg-primary me-1">
            {{ role }}
          </span>
        </p>
      </div>
    </div>

    <!-- Edit User Information -->
    <div class="card mb-4">
      <div class="card-body">
        <h5 class="card-title">Modifier les informations</h5>
        <form @submit.prevent="updateProfile">
          <div class="mb-3">
            <label for="username" class="form-label">Nom d'utilisateur</label>
            <input
              type="text"
              id="username"
              v-model="updatedUser.username"
              class="form-control"
              required />
          </div>
          <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input
              type="email"
              id="email"
              v-model="updatedUser.email"
              class="form-control"
              required />
          </div>
          <button type="submit" class="btn btn-primary">Enregistrer les modifications</button>
        </form>
      </div>
    </div>

    <!-- Change Password -->
    <div class="card mb-4">
      <div class="card-body">
        <h5 class="card-title">Changer le mot de passe</h5>
        <form @submit.prevent="changePassword">
          <div class="mb-3">
            <label for="currentPassword" class="form-label">Mot de passe actuel</label>
            <input
              type="password"
              id="currentPassword"
              v-model="passwords.currentPassword"
              class="form-control"
              required />
          </div>
          <div class="mb-3">
            <label for="newPassword" class="form-label">Nouveau mot de passe</label>
            <input
              type="password"
              id="newPassword"
              v-model="passwords.newPassword"
              class="form-control"
              required />
          </div>
          <div class="mb-3">
            <label for="confirmPassword" class="form-label">Confirmer le nouveau mot de passe</label>
            <input
              type="password"
              id="confirmPassword" 
              v-model="passwords.confirmPassword"
              class="form-control"
              required />
          </div>
          <button type="submit" class="btn btn-primary">Changer le mot de passe</button>
        </form>
      </div>
    </div>

    <!-- Logout Button -->
    <button @click="logout" class="btn btn-danger">Se déconnecter</button>
  </div>
</template>

<script>
import { mapState } from "vuex";
import UserService from "../services/UserService";

export default {
  name: "Profile",
  data() {
    return {
      updatedUser: {
        username: "",
        email: "",
      },
      passwords: {
        currentPassword: "",
        newPassword: "",
        confirmPassword: "",
      },
    };
  },
  computed: {
    ...mapState({
      user: (state) => state.auth.user, // Get the logged-in user from Vuex
      isLoggedIn: (state) => state.auth.status.loggedIn,
    }),
  },
  mounted() {
    if (!this.isLoggedIn) {
      this.$router.push({ path: '/login', query: { message: 'not-logged-in' } });
    }
  },
  created() {
    // Initialize updatedUser with the current user's data
    if (this.user) {
      this.updatedUser.username = this.user.username;
      this.updatedUser.email = this.user.email;
    }
  },
  methods: {
    updateProfile() {
      let changeOccurred = false;
      // Check if the username has changed
  const updateUsername = () => {
    if (this.updatedUser.username !== this.user.username) {
      return UserService.updateUsername(this.updatedUser.username)
        .then(() => {
          alert("Nom d'utilisateur mis à jour avec succès !");
          changeOccurred = true;
        })
        .catch((error) => {
          console.error("Erreur lors de la mise à jour du nom d'utilisateur :", error);
          alert("Une erreur est survenue lors de la mise à jour du profil.");
        });
    }
    return Promise.resolve(); // Return a resolved promise if no change
  };

  // Check if the email has changed
  const updateEmail = () => {
    if (this.updatedUser.email !== this.user.email) {
      return UserService.updateEmail(this.updatedUser.email)
        .then(() => {
          alert("Email mis à jour avec succès !");
          changeOccurred = true;
        })
        .catch((error) => {
          console.error("Erreur lors de la mise à jour de l'email :", error);
          alert("Une erreur est survenue lors de la mise à jour du profil.");
        });
    }
    return Promise.resolve(); // Return a resolved promise if no change
  };

  // Execute updates sequentially
  updateEmail()
    .then(() => updateUsername())
    .then(() => {
      if (changeOccurred) {
        this.$store.dispatch("auth/logout");
        this.$router.push({ path: "/login", query: { message: "user-updated" } });
      }
    });
    },
    changePassword() {
      if (this.passwords.newPassword !== this.passwords.confirmPassword) {
        alert("Les mots de passe ne correspondent pas.");
        return;
      }
      UserService.updatePassword(this.passwords.currentPassword, this.passwords.newPassword)
        .then(() => {
          alert("Mot de passe changé avec succès !");
          this.passwords = { currentPassword: "", newPassword: "", confirmPassword: "" };
          this.$store.dispatch("auth/logout");
          this.$router.push({ path: '/login', query: { message: 'user-updated' } });
        })
        .catch((error) => {
          if (error.response && error.response.data) {
            const { message, status } = error.response.data;
            console.error("Erreur lors du changement de mot de passe :" + "Error " + status + ": " + message);
            alert("Erreur : " + "Error " + status + ": " + message);
          } else {
            console.error("Erreur lors du changement de mot de passe :", error.message || error.toString());
            alert("Erreur : " + error.message || error.toString());
          }
        });
    },
    logout() {
      this.$store.dispatch("auth/logout");
      this.$router.push({ path: '/login', query: { message: 'logout-success' } });
    },
  },
};
</script>

<style scoped>
.profile-page {
  max-width: 600px;
  margin: 0 auto;
}

.card {
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.card-title {
  font-size: 1.25rem;
  font-weight: bold;
}

.btn {
  width: 100%;
}
</style>