<template>
  <div>
    <!-- Hero Section -->
    <section class="hero-section bg-primary text-white py-5">
      <div class="container">
        <div class="row align-items-center">
          <div class="col-lg-6">
            <h1 class="display-4 fw-bold mb-4">Bienvenue chez Apparte Aide</h1>
            <p class="lead mb-4">
              Découvrez nos appartements meublés de qualité à Bukavu. 
              Plus de 35 logements disponibles dans les meilleurs quartiers de la ville.
            </p>
            <router-link to="/apartments" class="btn btn-light btn-lg me-3">
              <i class="fas fa-search me-2"></i>Voir les Appartements
            </router-link>
            <router-link to="/my-reservations" class="btn btn-outline-light btn-lg">
              <i class="fas fa-calendar-check me-2"></i>Mes Réservations
            </router-link>
          </div>
          <div class="col-lg-6">
            <img src="https://images.pexels.com/photos/1457842/pexels-photo-1457842.jpeg" 
                 alt="Appartement moderne" class="img-fluid rounded shadow-lg">
          </div>
        </div>
      </div>
    </section>

    <!-- Services Section -->
    <section class="py-5">
      <div class="container">
        <div class="row">
          <div class="col-lg-8 mx-auto text-center mb-5">
            <h2 class="h1 fw-bold text-primary">Nos Services</h2>
            <p class="lead text-muted">Des appartements meublés avec tous les équipements nécessaires</p>
          </div>
        </div>
        
        <div class="row g-4">
          <div class="col-md-6 col-lg-3">
            <div class="text-center p-4 h-100">
              <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width: 70px; height: 70px;">
                <i class="fas fa-wifi text-primary fs-3"></i>
              </div>
              <h4 class="h5 mb-2">Wi-Fi Gratuit</h4>
              <p class="text-muted mb-0">Connexion internet haut débit dans tous nos appartements</p>
            </div>
          </div>
          
          <div class="col-md-6 col-lg-3">
            <div class="text-center p-4 h-100">
              <div class="bg-success bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width: 70px; height: 70px;">
                <i class="fas fa-tv text-success fs-3"></i>
              </div>
              <h4 class="h5 mb-2">TV & Divertissement</h4>
              <p class="text-muted mb-0">Télévision avec chaînes câblées disponibles</p>
            </div>
          </div>
          
          <div class="col-md-6 col-lg-3">
            <div class="text-center p-4 h-100">
              <div class="bg-info bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width: 70px; height: 70px;">
                <i class="fas fa-car text-info fs-3"></i>
              </div>
              <h4 class="h5 mb-2">Parking Sécurisé</h4>
              <p class="text-muted mb-0">Stationnement sécurisé pour votre véhicule</p>
            </div>
          </div>
          
          <div class="col-md-6 col-lg-3">
            <div class="text-center p-4 h-100">
              <div class="bg-warning bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width: 70px; height: 70px;">
                <i class="fas fa-utensils text-warning fs-3"></i>
              </div>
              <h4 class="h5 mb-2">Cuisine Équipée</h4>
              <p class="text-muted mb-0">Cuisine moderne avec tous les équipements</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Quartiers Section -->
    <section class="py-5 bg-light">
      <div class="container">
        <div class="row">
          <div class="col-lg-8 mx-auto text-center mb-5">
            <h2 class="h1 fw-bold text-primary">Nos Quartiers</h2>
            <p class="lead text-muted">Des appartements situés dans les meilleurs quartiers de Bukavu</p>
          </div>
        </div>
        
        <div class="row g-4">
          <div v-for="quartier in quartiers" :key="quartier.name" class="col-md-6 col-lg-4">
            <div class="card border-0 shadow-sm h-100">
              <img :src="quartier.image" class="card-img-top" style="height: 200px; object-fit: cover;" :alt="quartier.name">
              <div class="card-body text-center">
                <h5 class="card-title text-primary">{{ quartier.name }}</h5>
                <p class="card-text text-muted">{{ quartier.description }}</p>
                <span class="badge bg-primary">{{ quartier.count }} appartements</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="py-5 bg-primary text-white">
      <div class="container">
        <div class="row">
          <div class="col-lg-8 mx-auto text-center">
            <h2 class="h1 fw-bold mb-4">Prêt à Réserver ?</h2>
            <p class="lead mb-4">
              Trouvez l'appartement parfait pour votre séjour à Bukavu. 
              Réservation simple et confirmation rapide.
            </p>
            <router-link to="/apartments" class="btn btn-light btn-lg">
              <i class="fas fa-calendar-plus me-2"></i>Réserver Maintenant
            </router-link>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      quartiers: [
        {
          name: 'Muhumba',
          description: 'Quartier moderne avec vue sur le lac Kivu',
          image: 'https://images.pexels.com/photos/1571460/pexels-photo-1571460.jpeg',
          count: 8
        },
        {
          name: 'Nguba',
          description: 'Zone résidentielle calme et sécurisée',
          image: 'https://images.pexels.com/photos/1571468/pexels-photo-1571468.jpeg',
          count: 7
        },
        {
          name: 'Avenue Gouverneur',
          description: 'Centre-ville, proche des commerces',
          image: 'https://images.pexels.com/photos/1457847/pexels-photo-1457847.jpeg',
          count: 9
        },
        {
          name: 'Hippodrome',
          description: 'Quartier dynamique et bien desservi',
          image: 'https://images.pexels.com/photos/1571453/pexels-photo-1571453.jpeg',
          count: 6
        },
        {
          name: 'Laboute',
          description: 'Zone tranquille avec espaces verts',
          image: 'https://images.pexels.com/photos/1457849/pexels-photo-1457849.jpeg',
          count: 5
        }
      ]
    }
  }
}
</script>

<style scoped>
.hero-section {
  background: linear-gradient(135deg, #0d6efd 0%, #0056b3 100%);
}

.card {
  transition: transform 0.2s ease-in-out;
}

.card:hover {
  transform: translateY(-5px);
}
</style>