import { createWebHistory, createRouter } from "vue-router";

const routes =  [
  {
    path: "/",
    name: "accueil",
    component: () => import("./components/Welcome.vue")
  },
  {
    path: "/agenda",
    name: "agenda",
    component: () => import("./components/Agenda.vue")
  },
  {
    path: "/login",
    name: "login",
    component: () => import("./components/Login.vue")
  },
  {
    path: "/register",
    name: "register",
    component: () => import("./components/Register.vue")
  },
  {
    path: "/tutorials",
    name: "tutorials",
    component: () => import("./components/TutorialsList.vue")
  },
  {
    path: "/tutorials/:id",
    name: "tutorial-details",
    component: () => import("./components/Tutorial.vue")
  },
  {
    path: "/add",
    name: "add",
    component: () => import("./components/AddTutorial.vue")
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;