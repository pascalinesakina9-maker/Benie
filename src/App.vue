<template>
  <div id="app">
    <Navbar />
    <main>
      <router-view />
    </main>
    <footer class="bg-dark text-light py-4 mt-5">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <h5 class="text-primary mb-3">Apparte Aide</h5>
            <p class="mb-2">Location d'appartements meublés à Bukavu</p>
            <p class="mb-0">
              <i class="fas fa-map-marker-alt me-2"></i>
              Bukavu, République Démocratique du Congo
            </p>
          </div>
          <div class="col-md-3">
            <h6 class="text-light mb-3">Nos Quartiers</h6>
            <ul class="list-unstyled">
              <li class="mb-1">Muhumba</li>
              <li class="mb-1">Nguba</li>
              <li class="mb-1">Avenue Gouverneur</li>
              <li class="mb-1">Hippodrome</li>
              <li class="mb-1">Laboute</li>
            </ul>
          </div>
          <div class="col-md-3">
            <h6 class="text-light mb-3">Services</h6>
            <ul class="list-unstyled">
              <li class="mb-1"><i class="fas fa-wifi me-2"></i>Wi-Fi Gratuit</li>
              <li class="mb-1"><i class="fas fa-tv me-2"></i>Télévision</li>
              <li class="mb-1"><i class="fas fa-car me-2"></i>Parking</li>
              <li class="mb-1"><i class="fas fa-utensils me-2"></i>Cuisine équipée</li>
            </ul>
          </div>
        </div>
        <hr class="my-4">
        <div class="row align-items-center">
          <div class="col-md-6">
            <p class="mb-0">&copy; 2024 Apparte Aide. Tous droits réservés.</p>
          </div>
          <div class="col-md-6 text-end">
            <small class="text-muted">Système de réservation avec QR Code</small>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import Navbar from './components/Navbar.vue'

export default {
  name: 'App',
  components: {
    Navbar
  }
}
</script>

<style>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

main {
  flex: 1;
}

.router-link-active {
  font-weight: bold;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter, .fade-leave-to {
  opacity: 0;
}
</style>