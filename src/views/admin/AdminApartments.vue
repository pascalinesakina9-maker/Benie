<template>
  <div class="container-fluid py-4">
    <div class="row">
      <!-- Sidebar -->
      <div class="col-md-2">
        <div class="card border-0 shadow-sm">
          <div class="card-body p-3">
            <h6 class="text-primary mb-3">
              <i class="fas fa-tachometer-alt me-2"></i>Dashboard
            </h6>
            <nav class="nav flex-column">
              <router-link to="/admin" class="nav-link text-dark">
                <i class="fas fa-chart-bar me-2"></i>Statistiques
              </router-link>
              <router-link to="/admin/apartments" class="nav-link text-primary fw-bold">
                <i class="fas fa-home me-2"></i>Appartements
              </router-link>
              <router-link to="/admin/reservations" class="nav-link text-dark">
                <i class="fas fa-calendar me-2"></i>Réservations
              </router-link>
            </nav>
          </div>
        </div>
      </div>

      <!-- Main content -->
      <div class="col-md-10">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h1 class="h3 fw-bold text-primary">Gestion des Appartements</h1>
          <button class="btn btn-primary" @click="showAddModal">
            <i class="fas fa-plus me-2"></i>Nouvel Appartement
          </button>
        </div>

        <!-- Filtres -->
        <div class="card border-0 shadow-sm mb-4">
          <div class="card-body">
            <div class="row g-3">
              <div class="col-md-3">
                <select v-model="filters.quartier" @change="filterApartments" class="form-select">
                  <option value="">Tous les quartiers</option>
                  <option value="Muhumba">Muhumba</option>
                  <option value="Nguba">Nguba</option>
                  <option value="Avenue Gouverneur">Avenue Gouverneur</option>
                  <option value="Hippodrome">Hippodrome</option>
                  <option value="Laboute">Laboute</option>
                </select>
              </div>
              
              <div class="col-md-3">
                <select v-model="filters.statut" @change="filterApartments" class="form-select">
                  <option value="">Tous les statuts</option>
                  <option value="disponible">Disponible</option>
                  <option value="occupe">Occupé</option>
                  <option value="maintenance">Maintenance</option>
                </select>
              </div>
              
              <div class="col-md-6">
                <div class="input-group">
                  <input v-model="searchTerm" @input="filterApartments" 
                         type="text" class="form-control" placeholder="Rechercher par code ou description...">
                  <span class="input-group-text">
                    <i class="fas fa-search"></i>
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Loading -->
        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary"></div>
        </div>

        <!-- Table des appartements -->
        <div v-else class="card border-0 shadow">
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-hover mb-0">
                <thead class="bg-light">
                  <tr>
                    <th>Code</th>
                    <th>Quartier</th>
                    <th>Type</th>
                    <th>Chambres</th>
                    <th>Prix/Nuit</th>
                    <th>Statut</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="apartment in filteredApartments" :key="apartment.id">
                    <td>
                      <strong class="text-primary">{{ apartment.code }}</strong>
                    </td>
                    <td>{{ apartment.quartier }}</td>
                    <td>
                      <span class="badge bg-secondary">{{ apartment.type }}</span>
                    </td>
                    <td>{{ apartment.nb_chambres }}</td>
                    <td>
                      <strong class="text-success">${{ apartment.prix_nuit }}</strong>
                    </td>
                    <td>
                      <span :class="getStatusBadgeClass(apartment.statut)">
                        {{ getStatusLabel(apartment.statut) }}
                      </span>
                    </td>
                    <td>
                      <div class="btn-group btn-group-sm">
                        <button @click="viewApartment(apartment)" 
                                class="btn btn-outline-primary" title="Voir détails">
                          <i class="fas fa-eye"></i>
                        </button>
                        <button @click="editApartment(apartment)" 
                                class="btn btn-outline-warning" title="Modifier">
                          <i class="fas fa-edit"></i>
                        </button>
                        <button @click="deleteApartment(apartment)" 
                                class="btn btn-outline-danger" title="Supprimer">
                          <i class="fas fa-trash"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                  
                  <tr v-if="filteredApartments.length === 0">
                    <td colspan="7" class="text-center py-4 text-muted">
                      <i class="fas fa-home fa-2x mb-2"></i>
                      <div>Aucun appartement trouvé</div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal détails appartement -->
    <div class="modal fade" id="apartmentModal" tabindex="-1">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">
              <i class="fas fa-home me-2"></i>{{ selectedApartment?.code || 'Détails appartement' }}
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body" v-if="selectedApartment">
            <div class="row g-4">
              <!-- Images -->
              <div class="col-12" v-if="selectedApartment.images && selectedApartment.images.length > 0">
                <div class="carousel slide" :id="`carousel-modal`" data-bs-ride="carousel">
                  <div class="carousel-inner rounded">
                    <div v-for="(image, index) in selectedApartment.images" :key="index" 
                         :class="['carousel-item', { active: index === 0 }]">
                      <img :src="image" class="d-block w-100" style="height: 200px; object-fit: cover;" :alt="`Image ${index + 1}`">
                    </div>
                  </div>
                  <button v-if="selectedApartment.images.length > 1" class="carousel-control-prev" type="button" 
                          data-bs-target="#carousel-modal" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon"></span>
                  </button>
                  <button v-if="selectedApartment.images.length > 1" class="carousel-control-next" type="button" 
                          data-bs-target="#carousel-modal" data-bs-slide="next">
                    <span class="carousel-control-next-icon"></span>
                  </button>
                </div>
              </div>
              
              <div class="col-md-6">
                <h6 class="text-primary mb-3">Informations générales</h6>
                <p><strong>Code :</strong> {{ selectedApartment.code }}</p>
                <p><strong>Quartier :</strong> {{ selectedApartment.quartier }}</p>
                <p><strong>Type :</strong> {{ selectedApartment.type }}</p>
                <p><strong>Chambres :</strong> {{ selectedApartment.nb_chambres }}</p>
                <p><strong>Prix :</strong> ${{ selectedApartment.prix_nuit }}/nuit</p>
                <p><strong>Statut :</strong> 
                  <span :class="getStatusBadgeClass(selectedApartment.statut)">
                    {{ getStatusLabel(selectedApartment.statut) }}
                  </span>
                </p>
              </div>
              
              <div class="col-md-6">
                <h6 class="text-primary mb-3">Description</h6>
                <p>{{ selectedApartment.description || 'Aucune description' }}</p>
              </div>
              
              <div class="col-12">
                <h6 class="text-primary mb-3">Équipements</h6>
                <div class="d-flex flex-wrap gap-2">
                  <span v-for="equipement in selectedApartment.equipements" :key="equipement" 
                        class="badge bg-light text-dark">
                    <i :class="getServiceIcon(equipement)" class="me-1"></i>{{ equipement }}
                  </span>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/services/api'
import { Modal } from 'bootstrap'

export default {
  name: 'AdminApartments',
  data() {
    return {
      apartments: [],
      filteredApartments: [],
      selectedApartment: null,
      loading: true,
      filters: {
        quartier: '',
        statut: ''
      },
      searchTerm: ''
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
        
        if (this.filters.statut && apartment.statut !== this.filters.statut) {
          matches = false
        }
        
        if (this.searchTerm) {
          const searchLower = this.searchTerm.toLowerCase()
          const matchesSearch = 
            apartment.code.toLowerCase().includes(searchLower) ||
            apartment.description?.toLowerCase().includes(searchLower)
          if (!matchesSearch) matches = false
        }
        
        return matches
      })
    },
    
    viewApartment(apartment) {
      this.selectedApartment = apartment
      const modal = new Modal(document.getElementById('apartmentModal'))
      modal.show()
    },
    
    editApartment(apartment) {
      // TODO: Implémenter l'édition
      alert('Fonctionnalité d\'édition à implémenter')
    },
    
    deleteApartment(apartment) {
      if (confirm(`Êtes-vous sûr de vouloir supprimer l'appartement ${apartment.code} ?`)) {
        // TODO: Implémenter la suppression
        alert('Fonctionnalité de suppression à implémenter')
      }
    },
    
    showAddModal() {
      // TODO: Implémenter l'ajout
      alert('Fonctionnalité d\'ajout à implémenter')
    },
    
    getStatusLabel(status) {
      const labels = {
        'disponible': 'Disponible',
        'occupe': 'Occupé',
        'maintenance': 'Maintenance'
      }
      return labels[status] || status
    },
    
    getStatusBadgeClass(status) {
      const classes = {
        'disponible': 'badge bg-success',
        'occupe': 'badge bg-danger',
        'maintenance': 'badge bg-warning'
      }
      return classes[status] || 'badge bg-secondary'
    },
    
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