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
          <div>
            <h1 class="h3 fw-bold text-primary">
              {{ isEditing ? 'Modifier l\'appartement' : 'Nouvel appartement' }}
            </h1>
            <nav aria-label="breadcrumb">
              <ol class="breadcrumb">
                <li class="breadcrumb-item">
                  <router-link to="/admin" class="text-decoration-none">Dashboard</router-link>
                </li>
                <li class="breadcrumb-item">
                  <router-link to="/admin/apartments" class="text-decoration-none">Appartements</router-link>
                </li>
                <li class="breadcrumb-item active">{{ isEditing ? 'Modifier' : 'Nouveau' }}</li>
              </ol>
            </nav>
          </div>
          <router-link to="/admin/apartments" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left me-2"></i>Retour
          </router-link>
        </div>

        <!-- Loading -->
        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;"></div>
          <p class="mt-3 text-muted">Chargement des données...</p>
        </div>

        <!-- Formulaire -->
        <div v-else class="row">
          <div class="col-lg-8">
            <form @submit.prevent="saveApartment" novalidate>
              <div class="card border-0 shadow-sm mb-4">
                <div class="card-header bg-primary text-white">
                  <h5 class="mb-0">
                    <i class="fas fa-info-circle me-2"></i>Informations générales
                  </h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <!-- Code de l'appartement -->
                    <div class="col-md-6">
                      <label class="form-label">
                        Code de l'appartement *
                        <i class="fas fa-info-circle text-info ms-1" 
                           data-bs-toggle="tooltip" 
                           title="Identifiant unique de l'appartement (ex: APP001)"></i>
                      </label>
                      <input 
                        v-model="apartment.code" 
                        type="text" 
                        class="form-control"
                        :class="{ 'is-invalid': errors.code }"
                        placeholder="APP001"
                        required>
                      <div v-if="errors.code" class="invalid-feedback">{{ errors.code }}</div>
                    </div>

                    <!-- Quartier -->
                    <div class="col-md-6">
                      <label class="form-label">
                        Quartier *
                        <i class="fas fa-info-circle text-info ms-1" 
                           data-bs-toggle="tooltip" 
                           title="Localisation de l'appartement à Bukavu"></i>
                      </label>
                      <select 
                        v-model="apartment.quartier" 
                        class="form-select"
                        :class="{ 'is-invalid': errors.quartier }"
                        required>
                        <option value="">Sélectionner un quartier</option>
                        <option value="Muhumba">Muhumba</option>
                        <option value="Nguba">Nguba</option>
                        <option value="Avenue Gouverneur">Avenue Gouverneur</option>
                        <option value="Hippodrome">Hippodrome</option>
                        <option value="Laboute">Laboute</option>
                      </select>
                      <div v-if="errors.quartier" class="invalid-feedback">{{ errors.quartier }}</div>
                    </div>

                    <!-- Type -->
                    <div class="col-md-6">
                      <label class="form-label">
                        Type d'appartement *
                        <i class="fas fa-info-circle text-info ms-1" 
                           data-bs-toggle="tooltip" 
                           title="Configuration de l'appartement"></i>
                      </label>
                      <select 
                        v-model="apartment.type" 
                        class="form-select"
                        :class="{ 'is-invalid': errors.type }"
                        required>
                        <option value="">Sélectionner un type</option>
                        <option value="Studio">Studio</option>
                        <option value="T2">T2 (2 pièces)</option>
                        <option value="T3">T3 (3 pièces)</option>
                        <option value="T4">T4 (4 pièces)</option>
                      </select>
                      <div v-if="errors.type" class="invalid-feedback">{{ errors.type }}</div>
                    </div>

                    <!-- Nombre de chambres -->
                    <div class="col-md-6">
                      <label class="form-label">
                        Nombre de chambres *
                        <i class="fas fa-info-circle text-info ms-1" 
                           data-bs-toggle="tooltip" 
                           title="Nombre de chambres à coucher"></i>
                      </label>
                      <input 
                        v-model.number="apartment.nb_chambres" 
                        type="number" 
                        class="form-control"
                        :class="{ 'is-invalid': errors.nb_chambres }"
                        min="1" 
                        max="10"
                        required>
                      <div v-if="errors.nb_chambres" class="invalid-feedback">{{ errors.nb_chambres }}</div>
                    </div>

                    <!-- Prix par nuit -->
                    <div class="col-md-6">
                      <label class="form-label">
                        Prix par nuit (USD) *
                        <i class="fas fa-info-circle text-info ms-1" 
                           data-bs-toggle="tooltip" 
                           title="Tarif de location par nuit en dollars américains"></i>
                      </label>
                      <div class="input-group">
                        <span class="input-group-text">$</span>
                        <input 
                          v-model.number="apartment.prix_nuit" 
                          type="number" 
                          class="form-control"
                          :class="{ 'is-invalid': errors.prix_nuit }"
                          min="1" 
                          step="0.01"
                          placeholder="25.00"
                          required>
                        <div v-if="errors.prix_nuit" class="invalid-feedback">{{ errors.prix_nuit }}</div>
                      </div>
                    </div>

                    <!-- Statut -->
                    <div class="col-md-6">
                      <label class="form-label">
                        Statut
                        <i class="fas fa-info-circle text-info ms-1" 
                           data-bs-toggle="tooltip" 
                           title="Disponibilité actuelle de l'appartement"></i>
                      </label>
                      <select v-model="apartment.statut" class="form-select">
                        <option value="disponible">Disponible</option>
                        <option value="occupe">Occupé</option>
                        <option value="maintenance">En maintenance</option>
                      </select>
                    </div>

                    <!-- Description -->
                    <div class="col-12">
                      <label class="form-label">
                        Description
                        <i class="fas fa-info-circle text-info ms-1" 
                           data-bs-toggle="tooltip" 
                           title="Description détaillée de l'appartement et de ses atouts"></i>
                      </label>
                      <textarea 
                        v-model="apartment.description" 
                        class="form-control"
                        :class="{ 'is-invalid': errors.description }"
                        rows="4"
                        placeholder="Décrivez l'appartement, ses caractéristiques, son environnement..."></textarea>
                      <div v-if="errors.description" class="invalid-feedback">{{ errors.description }}</div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Équipements -->
              <div class="card border-0 shadow-sm mb-4">
                <div class="card-header bg-success text-white">
                  <h5 class="mb-0">
                    <i class="fas fa-cogs me-2"></i>Équipements et services
                  </h5>
                </div>
                <div class="card-body">
                  <div class="row g-2">
                    <div v-for="equipement in availableEquipements" :key="equipement.value" class="col-md-6 col-lg-4">
                      <div class="form-check">
                        <input 
                          :id="`equipement-${equipement.value}`"
                          v-model="apartment.equipements" 
                          :value="equipement.value"
                          type="checkbox" 
                          class="form-check-input">
                        <label :for="`equipement-${equipement.value}`" class="form-check-label">
                          <i :class="equipement.icon" class="me-2"></i>{{ equipement.label }}
                        </label>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Boutons d'action -->
              <div class="card border-0 shadow-sm">
                <div class="card-body">
                  <div class="d-flex justify-content-between">
                    <router-link to="/admin/apartments" class="btn btn-outline-secondary">
                      <i class="fas fa-times me-2"></i>Annuler
                    </router-link>
                    <button type="submit" class="btn btn-primary" :disabled="saving">
                      <span v-if="saving" class="spinner-border spinner-border-sm me-2"></span>
                      <i v-else class="fas fa-save me-2"></i>
                      {{ saving ? 'Enregistrement...' : (isEditing ? 'Mettre à jour' : 'Créer l\'appartement') }}
                    </button>
                  </div>
                </div>
              </div>
            </form>
          </div>

          <!-- Images -->
          <div class="col-lg-4">
            <div class="card border-0 shadow-sm">
              <div class="card-header bg-info text-white">
                <h5 class="mb-0">
                  <i class="fas fa-images me-2"></i>Images de l'appartement
                </h5>
              </div>
              <div class="card-body">
                <!-- Images existantes -->
                <div v-if="apartment.images && apartment.images.length > 0" class="mb-4">
                  <h6 class="text-muted mb-3">Images actuelles</h6>
                  
                  <!-- Carrousel si plusieurs images -->
                  <div v-if="apartment.images.length > 1" :id="`carousel-edit`" class="carousel slide mb-3" data-bs-ride="carousel">
                    <div class="carousel-inner rounded">
                      <div v-for="(image, index) in apartment.images" :key="index" 
                           :class="['carousel-item', { active: index === 0 }]">
                        <div class="position-relative">
                          <img :src="image.url || image" class="d-block w-100" style="height: 200px; object-fit: cover;" :alt="`Image ${index + 1}`">
                          <button @click="removeImage(image, index)" 
                                  type="button" 
                                  class="btn btn-danger btn-sm position-absolute top-0 end-0 m-2"
                                  title="Supprimer cette image">
                            <i class="fas fa-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#carousel-edit" data-bs-slide="prev">
                      <span class="carousel-control-prev-icon"></span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carousel-edit" data-bs-slide="next">
                      <span class="carousel-control-next-icon"></span>
                    </button>
                  </div>
                  
                  <!-- Image unique -->
                  <div v-else class="position-relative mb-3">
                    <img :src="apartment.images[0].url || apartment.images[0]" 
                         class="img-fluid rounded" 
                         style="height: 200px; width: 100%; object-fit: cover;" 
                         alt="Image de l'appartement">
                    <button @click="removeImage(apartment.images[0], 0)" 
                            type="button" 
                            class="btn btn-danger btn-sm position-absolute top-0 end-0 m-2"
                            title="Supprimer cette image">
                      <i class="fas fa-trash"></i>
                    </button>
                  </div>
                </div>

                <!-- Ajouter de nouvelles images -->
                <div>
                  <h6 class="text-muted mb-3">Ajouter des images</h6>
                  <div class="mb-3">
                    <label class="form-label">
                      URLs des images
                      <i class="fas fa-info-circle text-info ms-1" 
                         data-bs-toggle="tooltip" 
                         title="Ajoutez les URLs des images (une par ligne)"></i>
                    </label>
                    <textarea 
                      v-model="newImagesText" 
                      class="form-control" 
                      rows="4"
                      placeholder="https://images.pexels.com/photos/example1.jpg&#10;https://images.pexels.com/photos/example2.jpg"></textarea>
                    <small class="form-text text-muted">Une URL par ligne</small>
                  </div>
                  <button @click="addNewImages" 
                          type="button" 
                          class="btn btn-outline-info w-100"
                          :disabled="!newImagesText.trim()">
                    <i class="fas fa-plus me-2"></i>Ajouter les images
                  </button>
                </div>

                <!-- Preview des nouvelles images -->
                <div v-if="newImages.length > 0" class="mt-4">
                  <h6 class="text-muted mb-3">Aperçu des nouvelles images</h6>
                  <div class="row g-2">
                    <div v-for="(image, index) in newImages" :key="index" class="col-6">
                      <div class="position-relative">
                        <img :src="image" class="img-fluid rounded" style="height: 80px; width: 100%; object-fit: cover;" :alt="`Nouvelle image ${index + 1}`">
                        <button @click="removeNewImage(index)" 
                                type="button" 
                                class="btn btn-danger btn-sm position-absolute top-0 end-0 m-1"
                                style="font-size: 0.7rem;">
                          <i class="fas fa-times"></i>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/services/api'
import { Tooltip } from 'bootstrap'

export default {
  name: 'AdminEditApartment',
  props: ['id'],
  data() {
    return {
      apartment: {
        code: '',
        quartier: '',
        type: '',
        nb_chambres: 1,
        description: '',
        equipements: [],
        prix_nuit: 0,
        statut: 'disponible',
        images: []
      },
      newImages: [],
      newImagesText: '',
      loading: false,
      saving: false,
      errors: {},
      availableEquipements: [
        { value: 'Wi-Fi', label: 'Wi-Fi', icon: 'fas fa-wifi' },
        { value: 'TV', label: 'Télévision', icon: 'fas fa-tv' },
        { value: 'Climatisation', label: 'Climatisation', icon: 'fas fa-snowflake' },
        { value: 'Parking', label: 'Parking', icon: 'fas fa-car' },
        { value: 'Cuisine équipée', label: 'Cuisine équipée', icon: 'fas fa-utensils' },
        { value: 'Balcon', label: 'Balcon', icon: 'fas fa-building' },
        { value: 'Jardin', label: 'Jardin', icon: 'fas fa-tree' },
        { value: 'Sécurité', label: 'Sécurité 24h/24', icon: 'fas fa-shield-alt' },
        { value: 'Ascenseur', label: 'Ascenseur', icon: 'fas fa-elevator' },
        { value: 'Piscine', label: 'Piscine', icon: 'fas fa-swimming-pool' }
      ]
    }
  },
  computed: {
    isEditing() {
      return !!this.id
    }
  },
  async created() {
    if (this.isEditing) {
      await this.loadApartment()
    }
    this.initTooltips()
  },
  mounted() {
    this.initTooltips()
  },
  methods: {
    async loadApartment() {
      try {
        this.loading = true
        const response = await api.getApartment(this.id)
        const apartmentData = response.data
        
        this.apartment = {
          ...apartmentData,
          equipements: Array.isArray(apartmentData.equipements) 
            ? apartmentData.equipements 
            : (apartmentData.equipements ? JSON.parse(apartmentData.equipements) : []),
          images: apartmentData.images || []
        }
      } catch (error) {
        console.error('Erreur lors du chargement de l\'appartement:', error)
        this.$router.push('/admin/apartments')
      } finally {
        this.loading = false
      }
    },

    validateForm() {
      this.errors = {}
      
      if (!this.apartment.code.trim()) {
        this.errors.code = 'Le code est obligatoire'
      }
      
      if (!this.apartment.quartier) {
        this.errors.quartier = 'Le quartier est obligatoire'
      }
      
      if (!this.apartment.type) {
        this.errors.type = 'Le type est obligatoire'
      }
      
      if (!this.apartment.nb_chambres || this.apartment.nb_chambres < 1) {
        this.errors.nb_chambres = 'Le nombre de chambres doit être au moins 1'
      }
      
      if (!this.apartment.prix_nuit || this.apartment.prix_nuit <= 0) {
        this.errors.prix_nuit = 'Le prix doit être supérieur à 0'
      }
      
      return Object.keys(this.errors).length === 0
    },

    async saveApartment() {
      if (!this.validateForm()) {
        return
      }

      try {
        this.saving = true
        
        // Ajouter les nouvelles images si il y en a
        if (this.newImages.length > 0) {
          await api.addApartmentImages(this.apartment.id || this.id, this.newImages)
          this.newImages = []
          this.newImagesText = ''
        }
        
        if (this.isEditing) {
          // Mise à jour
          const response = await api.updateApartment(this.id, this.apartment)
          if (response.data.success) {
            this.$router.push('/admin/apartments')
          } else {
            alert('Erreur lors de la mise à jour: ' + response.data.message)
          }
        } else {
          // Création (à implémenter si nécessaire)
          alert('Fonctionnalité de création à implémenter')
        }
      } catch (error) {
        console.error('Erreur lors de la sauvegarde:', error)
        alert('Erreur lors de la sauvegarde')
      } finally {
        this.saving = false
      }
    },

    async removeImage(image, index) {
      if (confirm('Êtes-vous sûr de vouloir supprimer cette image ?')) {
        try {
          if (image.id) {
            // Image existante en base
            await api.deleteApartmentImage(image.id)
          }
          
          // Retirer de la liste locale
          this.apartment.images.splice(index, 1)
        } catch (error) {
          console.error('Erreur lors de la suppression de l\'image:', error)
          alert('Erreur lors de la suppression de l\'image')
        }
      }
    },

    addNewImages() {
      const urls = this.newImagesText.split('\n')
        .map(url => url.trim())
        .filter(url => url && this.isValidUrl(url))
      
      this.newImages = [...this.newImages, ...urls]
      this.newImagesText = ''
    },

    removeNewImage(index) {
      this.newImages.splice(index, 1)
    },

    isValidUrl(string) {
      try {
        new URL(string)
        return true
      } catch (_) {
        return false
      }
    },

    initTooltips() {
      this.$nextTick(() => {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
        tooltipTriggerList.map(function (tooltipTriggerEl) {
          return new Tooltip(tooltipTriggerEl)
        })
      })
    }
  }
}
</script>

<style scoped>
.form-control.is-invalid,
.form-select.is-invalid {
  border-color: #dc3545;
}

.invalid-feedback {
  display: block;
}

.carousel-control-prev,
.carousel-control-next {
  width: 5%;
}

.position-relative .btn {
  z-index: 10;
}
</style>