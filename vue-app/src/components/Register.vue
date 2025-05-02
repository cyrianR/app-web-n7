<template>
  <div class="page">
    <div class="register-container">
      <h2>Inscription</h2>
      <Form :validation-schema="schema" @submit="handleRegister">
        <div v-if="!successful">
          <div class="mb-3">
            <label for="username" class="form-label">Nom d'utilisateur</label>
            <Field name="username" class="form-control" type="text" placeholder="Nom d'utilisateur"/>
            <ErrorMessage name="username" v-slot="{ message }">
              <p class="unvalid-message">{{ message }}</p>
            </ErrorMessage>
          </div>
          <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <Field name="email" class="form-control" type="email" placeholder="Email"/>
            <ErrorMessage name="email" v-slot="{ message }">
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
          <div class="mb-3">
            <label for="confirmPassword" class="form-label">Confirmer mot de passe</label>
            <Field name="confirmPassword" class="form-control" type="password" placeholder="Confirmer"/>
            <ErrorMessage name="confirmPassword" v-slot="{ message }">
              <p class="unvalid-message">{{ message }}</p>
            </ErrorMessage>
          </div>
          <button type="submit" class="btn btn-primary">S'inscrire</button>
        </div>
      </Form>
      <div
        v-if="submitMessage"
        class="alert mt-3"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >{{submitMessage}}</div>
    </div>
  </div>
</template>

<script>
import { Field, Form, ErrorMessage } from 'vee-validate';
import * as yup from 'yup';

const schema = yup.object().shape({
  username: yup.string().required('Nom d\'utilisateur requis').min(3, '3 caractères minimum').max(20, '20 caractères maximum'),
  email: yup.string().required('Email requis').email('Email invalide'),
  password: yup.string().required('Mot de passe requis').min(8, '8 caractères minimum').max(100, '100 caractères maximum'),
  confirmPassword: yup.string()
    .oneOf([yup.ref('password'), null], 'Mots de passe non identiques')
    .required('Confirmation requise')
});

export default {
  name: "Register",
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
      successful: false,
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  mounted() {
    if (this.loggedIn) {
      this.$router.push('/');
    }
  },
  methods: {
    handleRegister(values) {
      this.submitMessage = '';
      this.$store.dispatch('auth/register', values)
        .then(data => {
          this.submitMessage = data.message;
          this.successful = true;
          setTimeout(() => {
            this.$router.push('/login');
          }, 1000);
        })
        .catch(error => {
          this.successful = false;
          if (error.response && error.response.data) {
            const { message } = error.response.data;
            const status = error.response.status;
            this.submitMessage = `Error ${status}: ${message}`;
          } else {
            this.submitMessage = error.message || error.toString();
          }
        });
    }
  }
};
</script>

<style scoped>
.page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: max-content;
  min-height: 80vh;
}

.register-container {
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