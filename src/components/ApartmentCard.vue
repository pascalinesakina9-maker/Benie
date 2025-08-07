<template>
  <div class="col-md-6 col-lg-4 mb-4">
    <div class="card h-100 shadow-sm apartment-card">
      <!-- Carousel d'images -->
      <div v-if="apartment.images && apartment.images.length > 0" :id="`carousel-${apartment.id}`" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-inner">
          <div v-for="(image, index) in apartment.images" :key="index" 
               :class="['carousel-item', { active: index === 0 }]">
            <img :src="image" class="d-block w-100 apartment-image" :alt="`Image ${index + 1}`">
          </div>
        </div>
        <button v-if="apartment.images.length > 1" class="carousel-control-prev" type="button" 
                :data-bs-target="`#carousel-${apartment.id}`" data-bs-slide="prev">
          <span class="carousel-control-prev-icon"></span>
        </button>
        <button v-if="apartment.images.length > 1" class="carousel-control-next" type="button" 
                :data-bs-target="`#carousel-${apartment.id}`" data-bs-slide="next">
          <span class="carousel-control-next-icon"></span>
        </button>
      </div>
      
      <div class="card-body d-flex flex-column">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <h5 class="card-title mb-0">{{ apartment.code }}</h5>
          <span class="badge bg-success">${{ apartment.prix_nuit }}/nuit</span>
        </div>
        
        <div class="mb-2">
          <span class="badge bg-info me-2">{{ apartment.quartier }}</span>
          <span class="badge bg-secondary">{{ apartment.type }}</span>
        </div>
        
        <p class="card-text mb-3">{{ apartment.description }}</p>
        
        <div class="mb-3">
          <small class="text-muted">
            <i class="fas fa-bed me-1"></i>{{ apartment.nb_chambres }} chambre(s)
          </small>
        </div>
        
        <!-- Services -->
        <div class="mb-3">
          <div class="d-flex flex-wrap gap-1">
            <span v-for="service in apartment.equipements" :key="service" 
                  class="badge bg-light text-dark">
              <i :class="getServiceIcon(service)" class="me-1"></i>{{ service }}
            </span>
          </div>
        </div>
        
        <div class="mt-auto">
          <router-link :to="`/booking/${apartment.id}`" class="btn btn-primary w-100">
            <i class="fas fa-calendar-plus me-2"></i>Réserver
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ApartmentCard',
  props: {
    apartment: {
      type: Object,
      required: true
    }
  },
  methods: {
    getServiceIcon(service) {
      const icons = {
        'Wi-Fi': 'fas fa-wifi',
        'TV': 'fas fa-tv',
        'Climatisation': 'fas fa-snowflake',
        'Parking': 'fas fa-car',
        'Cuisine équipée': 'fas fa-utensils',
        'Balcon': 'fas fa-building',
        'Jardin': 'fas fa-tree'
      }
      return icons[service] || 'fas fa-check'
    }
  }
}
</script>

<style scoped>
.apartment-card {
  transition: transform 0.2s ease-in-out;
  border: none;
}

.apartment-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.15) !important;
}

.apartment-image {
  height: 200px;
  object-fit: cover;
}

.carousel-control-prev,
.carousel-control-next {
  width: 5%;
}
</style>