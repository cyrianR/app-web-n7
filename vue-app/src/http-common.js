import axios from "axios";
import store from "./store";
import { jwtDecode } from 'jwt-decode';

const backend_url = import.meta.env.VITE_BACKEND_URL;

const http = axios.create({
  baseURL: "http://" + backend_url +"/api", // api base url
  headers: {
    "Content-type": "application/json"
  }
});

// Add a request interceptor to check token validity before sending requests
// and add Authorization header if token is valid
http.interceptors.request.use(
  (config) => {
    const user = store.state.auth.user;
    if (user && user.token) {
      if (isTokenExpired(user.token)) {
        store.dispatch('auth/logout');
        router.push({ path: '/login', query: { message: 'session-expired' } });
        return Promise.reject(new Error('Token expired'));
      }
      config.headers.Authorization = `Bearer ${user.token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

function isTokenExpired(token) {
  try {
    const decoded = jwtDecode(token);
    const currentTime = Date.now() / 1000;
    return decoded.exp < currentTime;
  } catch (error) {
    return true;
  }
}

export default http;
