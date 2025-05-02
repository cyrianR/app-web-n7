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
            <span v-show="loading" class="spinner-border spinner-border-sm"></span>
            <span>Se connecter</span>
          </button>
        </div>
        <div class="form-group mt-3">
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
      submitMessage: ''
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
  },
  methods: {
    handleLogin(values) {
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