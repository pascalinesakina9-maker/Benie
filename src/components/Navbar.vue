<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top">
    <div class="container">
      <router-link to="/" class="navbar-brand fw-bold">
        <i class="fas fa-home me-2"></i>Apparte Aide
      </router-link>
      
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>
      
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <router-link to="/" class="nav-link">Accueil</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/apartments" class="nav-link">Appartements</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/my-reservations" class="nav-link">Mes Réservations</router-link>
          </li>
        </ul>
        
        <ul class="navbar-nav">
          <li class="nav-item" v-if="!isAdmin">
            <router-link to="/admin/login" class="nav-link">
              <i class="fas fa-user-shield me-1"></i>Admin
            </router-link>
          </li>
          <li class="nav-item dropdown" v-if="isAdmin">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
              <i class="fas fa-user-shield me-1"></i>Admin
            </a>
            <ul class="dropdown-menu">
              <li><router-link to="/admin" class="dropdown-item">Dashboard</router-link></li>
              <li><router-link to="/admin/apartments" class="dropdown-item">Appartements</router-link></li>
              <li><router-link to="/admin/reservations" class="dropdown-item">Réservations</router-link></li>
              <li><hr class="dropdown-divider"></li>
              <li><a @click="logout" class="dropdown-item" href="#">Déconnexion</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'Navbar',
  computed: {
    isAdmin() {
      return localStorage.getItem('admin_user') !== null
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('admin_user')
      this.$router.push('/admin/login')
    }
  }
}
</script>