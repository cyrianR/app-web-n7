import { createWebHistory, createRouter } from "vue-router";

const routes =  [
  {
    path: "/",
    name: "accueil",
    component: () => import("./components/Accueil.vue")
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