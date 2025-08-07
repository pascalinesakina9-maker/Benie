<template>
  <div class="container py-5">
    <div class="row">
      <!-- Détails de l'appartement -->
      <div class="col-lg-6 mb-4">
        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary"></div>
        </div>
        
        <div v-else-if="apartment" class="card border-0 shadow">
          <!-- Images carousel -->
          <div v-if="apartment.images && apartment.images.length > 0" 
               :id="`carousel-apartment`" class="carousel slide" data-bs-ride="carousel">
            <div class="carousel-inner">
              <div v-for="(image, index) in apartment.images" :key="index" 
                   :class="['carousel-item', { active: index === 0 }]">
                <img :src="image" class="d-block w-100" style="height: 300px; object-fit: cover;" :alt="`Image ${index + 1}`">
              </div>
            </div>
            <button v-if="apartment.images.length > 1" class="carousel-control-prev" type="button" 
                    data-bs-target="#carousel-apartment" data-bs-slide="prev">
              <span class="carousel-control-prev-icon"></span>
            </button>
            <button v-if="apartment.images.length > 1" class="carousel-control-next" type="button" 
                    data-bs-target="#carousel-apartment" data-bs-slide="next">
              <span class="carousel-control-next-icon"></span>
            </button>
          </div>
          
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-3">
              <h3 class="card-title mb-0">{{ apartment.code }}</h3>
              <span class="badge bg-success fs-6">${{ apartment.prix_nuit }}/nuit</span>
            </div>
            
            <div class="mb-3">
              <span class="badge bg-info me-2">{{ apartment.quartier }}</span>
              <span class="badge bg-secondary me-2">{{ apartment.type }}</span>
              <span class="badge bg-outline-primary">{{ apartment.nb_chambres }} chambre(s)</span>
            </div>
            
            <p class="card-text mb-4">{{ apartment.description }}</p>
            
            <!-- Services -->
            <h5 class="mb-3">Services inclus</h5>
            <div class="row g-2">
              <div v-for="service in apartment.equipements" :key="service" class="col-auto">
                <span class="badge bg-light text-dark">
                  <i :class="getServiceIcon(service)" class="me-1"></i>{{ service }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Formulaire de réservation -->
      <div class="col-lg-6">
        <div class="card border-0 shadow">
          <div class="card-header bg-primary text-white">
            <h4 class="mb-0">
              <i class="fas fa-calendar-plus me-2"></i>Réserver cet appartement
            </h4>
          </div>
          
          <div class="card-body">
            <form @submit.prevent="submitBooking">
              <div class="row g-3">
                <div class="col-12">
                  <label class="form-label">Nom complet *</label>
                  <input v-model="booking.nom" type="text" class="form-control" required>
                </div>
                
                <div class="col-12">
                  <label class="form-label">Téléphone / WhatsApp *</label>
                  <input v-model="booking.telephone" type="tel" class="form-control" placeholder="+243..." required>
                </div>
                
                <div class="col-md-6">
                  <label class="form-label">Date d'arrivée *</label>
                  <input v-model="booking.date_arrivee" type="date" class="form-control" :min="today" required>
                </div>
                
                <div class="col-md-6">
                  <label class="form-label">Date de départ *</label>
                  <input v-model="booking.date_depart" type="date" class="form-control" :min="booking.date_arrivee" required>
                </div>
                
                <div class="col-md-6">
                  <label class="form-label">Nombre d'adultes *</label>
                  <select v-model="booking.nb_adultes" class="form-select" required>
                    <option value="1">1 adulte</option>
                    <option value="2">2 adultes</option>
                    <option value="3">3 adultes</option>
                    <option value="4">4 adultes</option>
                  </select>
                </div>
                
                <div class="col-md-6">
                  <label class="form-label">Nombre d'enfants</label>
                  <select v-model="booking.nb_enfants" class="form-select">
                    <option value="0">Aucun</option>
                    <option value="1">1 enfant</option>
                    <option value="2">2 enfants</option>
                    <option value="3">3 enfants</option>
                  </select>
                </div>
                
                <div class="col-12">
                  <label class="form-label">Motif du séjour</label>
                  <select v-model="booking.motif" class="form-select">
                    <option value="Affaires">Affaires</option>
                    <option value="Tourisme">Tourisme</option>
                    <option value="Famille">Famille</option>
                    <option value="Événement">Événement</option>
                    <option value="Autre">Autre</option>
                  </select>
                </div>
                
                <!-- Résumé de la réservation -->
                <div class="col-12" v-if="nightsCount > 0">
                  <div class="alert alert-info">
                    <h6 class="mb-2">Résumé de votre réservation</h6>
                    <p class="mb-1">{{ nightsCount }} nuit(s) x ${{ apartment?.prix_nuit || 0 }} = <strong>${{ totalPrice }}</strong></p>
                    <p class="mb-0"><small>{{ booking.nb_adultes }} adulte(s) + {{ booking.nb_enfants }} enfant(s)</small></p>
                  </div>
                </div>
                
                <div class="col-12">
                  <button type="submit" class="btn btn-primary btn-lg w-100" :disabled="submitting">
                    <span v-if="submitting" class="spinner-border spinner-border-sm me-2"></span>
                    {{ submitting ? 'Réservation en cours...' : 'Confirmer la réservation' }}
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de confirmation -->
    <div class="modal fade" id="confirmationModal" tabindex="-1">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header bg-success text-white">
            <h5 class="modal-title">
              <i class="fas fa-check-circle me-2"></i>Réservation confirmée !
            </h5>
          </div>
          <div class="modal-body text-center">
            <div class="mb-4">
              <i class="fas fa-ticket-alt fa-3x text-success mb-3"></i>
              <h4>Votre ticket : {{ reservationResult?.ticket_id }}</h4>
              <p class="text-muted">Montant total : ${{ reservationResult?.montant_total }}</p>
            </div>
            
            <!-- QR Code -->
            <div v-if="reservationResult?.qr_code_url" class="mb-4">
              <h6>Votre QR Code de réservation</h6>
              <div class="bg-light p-3 rounded">
                <i class="fas fa-qrcode fa-4x text-primary"></i>
                <p class="small text-muted mt-2">Code généré pour la réservation #{{ reservationResult?.reservation_id }}</p>
              </div>
            </div>
            
            <p><small class="text-muted">Un représentant vous contactera bientôt pour finaliser les détails.</small></p>
          </div>
          <div class="modal-footer">
            <router-link to="/my-reservations" class="btn btn-primary">
              Voir mes réservations
            </router-link>
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
  name: 'Booking',
  props: ['id'],
  data() {
    return {
      apartment: null,
      loading: true,
      submitting: false,
      reservationResult: null,
      today: new Date().toISOString().split('T')[0],
      booking: {
        nom: '',
        telephone: '',
        date_arrivee: '',
        date_depart: '',
        nb_adultes: 1,
        nb_enfants: 0,
        motif: 'Affaires',
        appartement_id: this.id
      }
    }
  },
  computed: {
    nightsCount() {
      if (!this.booking.date_arrivee || !this.booking.date_depart) return 0
      const start = new Date(this.booking.date_arrivee)
      const end = new Date(this.booking.date_depart)
      const diffTime = Math.abs(end - start)
      return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    },
    
    totalPrice() {
      return this.nightsCount * (this.apartment?.prix_nuit || 0)
    }
  },
  async created() {
    await this.loadApartment()
  },
  methods: {
    async loadApartment() {
      try {
        this.loading = true
        const response = await api.getApartment(this.id)
        this.apartment = response.data
        this.booking.appartement_id = this.id
      } catch (error) {
        console.error('Erreur lors du chargement de l\'appartement:', error)
        this.$router.push('/apartments')
      } finally {
        this.loading = false
      }
    },
    
    async submitBooking() {
      try {
        this.submitting = true
        const response = await api.createReservation(this.booking)
        
        if (response.data.success) {
          this.reservationResult = response.data
          const modal = new Modal(document.getElementById('confirmationModal'))
          modal.show()
        } else {
          alert('Erreur lors de la réservation')
        }
      } catch (error) {
        console.error('Erreur lors de la réservation:', error)
        alert('Erreur lors de la réservation')
      } finally {
        this.submitting = false
      }
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