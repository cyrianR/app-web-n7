<template>
  <div class="page">
    <div class="login-container">
      <h2>Connection</h2>
      <Form :validation-schema="schema" @submit="handleLogin">
        <div class="mb-3">
          <label for="username" class="form-label">Nom d'utilisateur</label>
          <Field name="username" class="form-control" type="text" placeholder="Nom d'utilisateur"/>
          <ErrorMessage name="username" v-slot="{ message }">
            <p class="unvalid-message">{{ message }}</p>
          </ErrorMessage>
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">Mot de passe</label>
          <Field name="password" class="form-control" type="password" placeholder="Mot de passe"/>
          <ErrorMessage name="password" v-slot="{ message }">
            <p class="unvalid-message">{{ message }}</p>
          </ErrorMessage>
        </div>
        <div class="form-group">
          <button type="submit" class="btn btn-primary" :disabled="loading">
            <span v-show="loading" class="spinner-border spinner-border-sm me-2"></span>
            <span>Se connecter</span>
          </button>
        </div>
        <div class="form-group mt-3">
          <div v-if="redirectMessage" class="alert alert-warning" role="alert">{{ redirectMessage }}</div>
          <div v-if="submitMessage" class="alert alert-danger" role="alert">{{submitMessage}}</div>
        </div>
      </Form>
    </div>
  </div>
</template>

<script>
import { Field, Form, ErrorMessage } from 'vee-validate';
import * as yup from 'yup';

const schema = yup.object().shape({
  username: yup.string().required('Nom d\'utilisateur requis'),
  password: yup.string().required('Mot de passe requis')
});

export default {
  name: "Login",
  components: {
    Field,
    Form,
    ErrorMessage
  },
  setup() {
    return {
      schema,
    };
  },
  data() {
    return {
      loading: false,
      submitMessage: '',
      redirectMessage: ''
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  created() {
    if (this.loggedIn) {
      this.$router.push('/');
    }
    const queryMessage = this.$route.query.message;
    if (queryMessage === 'session-expired') {
      this.redirectMessage = 'Votre session a expiré. Veuillez vous reconnecter.';
    }
    if (queryMessage === 'logout-success') {
      this.redirectMessage = 'Vous avez été déconnecté avec succès.';
    }
    if (queryMessage === 'register-success') {
      this.redirectMessage = 'Inscription réussie. Veuillez vous connecter.';
    }
    if (queryMessage === 'not-logged-in') {
      this.redirectMessage = 'Veuillez vous connecter pour accéder à cette page.';
    }
    if (queryMessage === 'user-updated') {
      this.redirectMessage = 'Veuillez vous reconnecter.';
    }
  },
  methods: {
    handleLogin(values) {
      this.redirectMessage = '';
      this.submitMessage = '';
      this.loading = true;
      this.$store.dispatch('auth/login', values)
        .then(() => {
          this.$router.push('/');
        })
        .catch(error => {
          this.loading = false;
          if (error.response && error.response.data) {
            const { message, status } = error.response.data;
            this.submitMessage = `Error ${status}: ${message}`;
          } else {
            this.submitMessage = error.message || error.toString();
          }
        });
    }
  },
}

</script>

<style scoped>
.page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: max-content;
  min-height: 80vh;
}

.login-container {
  max-width: 400px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.unvalid-message {
  color: red;
  font-size: 0.7em;
}
</style>