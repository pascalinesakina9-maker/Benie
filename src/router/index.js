import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Apartments from '../views/Apartments.vue'
import Booking from '../views/Booking.vue'
import MyReservations from '../views/MyReservations.vue'
import AdminLogin from '../views/admin/AdminLogin.vue'
import AdminDashboard from '../views/admin/AdminDashboard.vue'
import AdminApartments from '../views/admin/AdminApartments.vue'
import AdminReservations from '../views/admin/AdminReservations.vue'
import AdminEditApartment from '../views/admin/AdminEditApartment.vue'
import VerifyQR from '../views/VerifyQR.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/apartments',
    name: 'Apartments',
    component: Apartments
  },
  {
    path: '/booking/:id',
    name: 'Booking',
    component: Booking,
    props: true
  },
  {
    path: '/my-reservations',
    name: 'MyReservations',
    component: MyReservations
  },
  {
    path: '/verify/:id',
    name: 'VerifyQR',
    component: VerifyQR,
    props: true
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: AdminLogin
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: AdminDashboard,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/apartments',
    name: 'AdminApartments',
    component: AdminApartments,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/reservations',
    name: 'AdminReservations',
    component: AdminReservations,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/apartments/edit/:id',
    name: 'AdminEditApartment',
    component: AdminEditApartment,
    props: true,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guard pour les routes admin
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('admin_user')
  
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      next('/admin/login')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router