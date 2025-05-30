import { createWebHistory, createRouter } from "vue-router";
import store from "./store";

const routes =  [
  {
    path: "/",
    name: "accueil",
    component: () => import("./components/Welcome.vue")
  },
  {
    path: "/adminboard",
    name: "adminboard",
    component: () => import("./components/AdminBoard.vue"),
    meta: { roles: ["ROLE_ADMIN"] }
  },
  {
    path: "/agenda",
    name: "agenda",
    component: () => import("./components/Agenda.vue")
  },
  {
    path: "/profile",
    name: "profile",
    component: () => import("./components/Profile.vue")
  },
  {
    path: "/lesson",
    name: "lesson",
    component: () => import("./components/Lesson.vue")
  },
  {
    path: "/proj",
    name: "proj",
    component: () => import("./components/Proj.vue")
  },
  {
    path: "/login",
    name: "login",
    component: () => import("./components/Login.vue")
  },
  {
    path: "/logout",
    name: "logout",
    component: () => import("./components/Logout.vue")
  },
  {
    path: "/register",
    name: "register",
    component: () => import("./components/Register.vue")
  },
  {  
    path: "/photos",
    name: "photos",
    component: () => import("./components/Photos.vue")
  },
  {  
    path: "/event/:id",
    name: "event",
    component: () => import("./components/Event.vue")
  },
  {  
    path: "/post/:id",
    name: "post",
    component: () => import("./components/Post.vue")
  },
  {
    path: "/tutorials",
    name: "tutorials",
    component: () => import("./components/TutorialsList.vue"),
    meta: { roles: ["ROLE_ADMIN"] }
  },
  {
    path: "/tutorials/:id",
    name: "tutorial-details",
    component: () => import("./components/Tutorial.vue"),
    meta: { roles: ["ROLE_ADMIN"] }
  },
  {
    path: "/add",
    name: "add",
    component: () => import("./components/AddTutorial.vue"),
    meta: { roles: ["ROLE_ADMIN"] }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Add a global navigation guard for role-based access control
router.beforeEach((to, from, next) => {
  const user = store.state.auth.user;
  const requiredRoles = to.meta.roles;
  if (requiredRoles) {
    if (!user || !user.roles) {
      // If user is not logged in or roles are not defined, redirect to login 
      return next('/login');
    }
    const hasAccess = user.roles.some(role => requiredRoles.includes(role));
    if (!hasAccess) {
      // If user does not have the required role, redirect to home
      alert('You do not have access to this page.');
      return next('/');
    }
  }
  // Allow navigation to the route if the user has the required role
  next();
});

export default router;