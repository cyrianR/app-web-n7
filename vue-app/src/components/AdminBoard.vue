<template>
  <div class="admin-board">
    <h1 class="pb-2">Admin Board</h1>

    <!-- Search Users -->
    <div class="search-bar">
      <input v-model="searchQuery" @input="fetchUsers" type="text" placeholder="Rechercher par nom"
        class="form-control" />
    </div>

    <!-- User List -->
    <table class="table table-striped mt-3">
      <thead>
        <tr>
          <th>ID</th>
          <th>Nom</th>
          <th>Email</th>
          <th>Rôles</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.email }}</td>
          <td>
            <span v-for="role in user.roles" :key="role" class="badge bg-primary me-1">
              {{ role }}
            </span>
          </td>
          <td>
            <button @click="deleteUser(user.id)" class="btn btn-danger btn-sm me-2">Supprimer</button>
            <button @click="openRoleModal(user)" class="btn btn-warning btn-sm">Changer Rôles</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Role Update Modal -->
    <div v-if="selectedUser" class="modal" tabindex="-1" style="display: block;">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Update Roles for {{ selectedUser.username }}</h5>
            <button type="button" class="btn-close" @click="closeRoleModal"></button>
          </div>
          <div class="modal-body">
            <div v-for="role in availableRoles" :key="role" class="form-check">
              <input class="form-check-input" type="checkbox" :id="role" :value="role" v-model="selectedRoles" />
              <label class="form-check-label" :for="role">{{ role }}</label>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeRoleModal">Cancel</button>
            <button type="button" class="btn btn-primary" @click="updateRoles">Save Changes</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserService from "../services/UserService";

export default {
  data() {
    return {
      users: [], // List of users
      searchQuery: "", // Search query for filtering users
      selectedUser: null, // User selected for role update
      selectedRoles: [], // Roles selected for the user
      availableRoles: ["ROLE_LESSON_ADMIN", "ROLE_KAROKE_ADMIN", "ROLE_ADMIN", "ROLE_MEMBER", "ROLE_EXTERN"], // Available roles
    };
  },
  created() {
    this.fetchUsers();
  },
  methods: {
    // Fetch users from the backend
    fetchUsers() {
      UserService.getAll(this.searchQuery)
        .then((response) => {
          this.users = response.data;
        })
        .catch((error) => {
          console.error("Error fetching users:", error);
        });
    },

    // Delete a user
    deleteUser(id) {
      if (confirm("Are you sure you want to delete this user?")) {
        UserService.delete(id)
          .then(() => {
            this.fetchUsers(); // Refresh the user list
          })
          .catch((error) => {
            console.error("Error deleting user:", error);
          });
      }
    },

    // Open the role update modal
    openRoleModal(user) {
      this.selectedUser = user;
      this.selectedRoles = [...user.roles]; // Pre-fill with current roles
    },

    // Close the role update modal
    closeRoleModal() {
      this.selectedUser = null;
      this.selectedRoles = [];
    },

    // Update roles for the selected user
    updateRoles() {
      if (this.selectedUser) {
        UserService.updateRoles(this.selectedUser.id, this.selectedRoles)
          .then(() => {
            this.fetchUsers(); // Refresh the user list
            this.closeRoleModal(); // Close the modal
          })
          .catch((error) => {
            console.error("Error updating roles:", error);
          });
      }
    },
  },
};
</script>