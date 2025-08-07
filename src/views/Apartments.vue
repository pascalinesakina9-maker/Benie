<template>
  <div class="container py-5">
    <!-- En-tête -->
    <div class="row mb-5">
      <div class="col-lg-8 mx-auto text-center">
        <h1 class="display-5 fw-bold text-primary mb-3">Nos Appartements</h1>
        <p class="lead text-muted">Découvrez notre sélection d'appartements meublés à Bukavu</p>
      </div>
    </div>

    <!-- Filtres -->
    <div class="row mb-4">
      <div class="col-lg-8 mx-auto">
        <div class="card border-0 shadow-sm">
          <div class="card-body">
            <div class="row g-3">
              <div class="col-md-4">
                <label class="form-label">Quartier</label>
                <select v-model="filters.quartier" @change="filterApartments" class="form-select">
                  <option value="">Tous les quartiers</option>
                  <option value="Muhumba">Muhumba</option>
                  <option value="Nguba">Nguba</option>
                  <option value="Avenue Gouverneur">Avenue Gouverneur</option>
                  <option value="Hippodrome">Hippodrome</option>
                  <option value="Laboute">Laboute</option>
                </select>
              </div>
              
              <div class="col-md-4">
                <label class="form-label">Type</label>
                <select v-model="filters.type" @change="filterApartments" class="form-select">
                  <option value="">Tous les types</option>
                  <option value="Studio">Studio</option>
                  <option value="T2">T2</option>
                  <option value="T3">T3</option>
                </select>
              </div>
              
              <div class="col-md-4">
                <label class="form-label">Prix maximum</label>
                <select v-model="filters.priceMax" @change="filterApartments" class="form-select">
                  <option value="">Tous les prix</option>
                  <option value="30">Jusqu'à $30</option>
                  <option value="50">Jusqu'à $50</option>
                  <option value="70">Jusqu'à $70</option>
                </select>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Chargement...</span>
      </div>
    </div>

    <!-- Liste des appartements -->
    <div v-else-if="filteredApartments.length > 0" class="row">
      <ApartmentCard 
        v-for="apartment in filteredApartments" 
        :key="apartment.id" 
        :apartment="apartment" 
      />
    </div>

    <!-- Aucun résultat -->
    <div v-else class="text-center py-5">
      <i class="fas fa-home fa-3x text-muted mb-3"></i>
      <h4 class="text-muted">Aucun appartement trouvé</h4>
      <p class="text-muted">Essayez de modifier vos critères de recherche</p>
    </div>
  </div>
</template>

<script>
import ApartmentCard from '@/components/ApartmentCard.vue'
import api from '@/services/api'

export default {
  name: 'Apartments',
  components: {
    ApartmentCard
  },
  data() {
    return {
      apartments: [],
      filteredApartments: [],
      loading: true,
      filters: {
        quartier: '',
        type: '',
        priceMax: ''
      }
    }
  },
  async created() {
    await this.loadApartments()
  },
  methods: {
    async loadApartments() {
      try {
        this.loading = true
        const response = await api.getApartments()
        this.apartments = response.data
        this.filteredApartments = [...this.apartments]
      } catch (error) {
        console.error('Erreur lors du chargement des appartements:', error)
      } finally {
        this.loading = false
      }
    },
    
    filterApartments() {
      this.filteredApartments = this.apartments.filter(apartment => {
        let matches = true
        
        if (this.filters.quartier && apartment.quartier !== this.filters.quartier) {
          matches = false
        }
        
        if (this.filters.type && apartment.type !== this.filters.type) {
          matches = false
        }
        
        if (this.filters.priceMax && apartment.prix_nuit > this.filters.priceMax) {
          matches = false
        }
        
        return matches
      })
    }
  }
}
</script>