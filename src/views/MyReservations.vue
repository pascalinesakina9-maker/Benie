<template>
  <div class="container py-5">
    <div class="row">
      <div class="col-lg-8 mx-auto">
        <div class="text-center mb-5">
          <h1 class="display-6 fw-bold text-primary mb-3">Mes Réservations</h1>
          <p class="lead text-muted">Consultez l'état de vos réservations</p>
        </div>

        <!-- Recherche par téléphone -->
        <div class="card border-0 shadow-sm mb-4">
          <div class="card-body">
            <div class="row g-3 align-items-end">
              <div class="col-md-8">
                <label class="form-label">Numéro de téléphone</label>
                <input v-model="searchPhone" @keyup.enter="searchReservations" 
                       type="tel" class="form-control" placeholder="+243...">
              </div>
              <div class="col-md-4">
                <button @click="searchReservations" class="btn btn-primary w-100" :disabled="searching">
                  <span v-if="searching" class="spinner-border spinner-border-sm me-2"></span>
                  {{ searching ? 'Recherche...' : 'Rechercher' }}
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Résultats -->
        <div v-if="reservations.length > 0">
          <div v-for="reservation in reservations" :key="reservation.id" class="card mb-4 border-0 shadow-sm">
            <div class="card-body">
              <div class="row">
                <div class="col-md-8">
                  <div class="d-flex justify-content-between align-items-start mb-3">
                    <h5 class="card-title mb-0">
                      <i class="fas fa-home me-2"></i>{{ reservation.code }}
                    </h5>
                    <span :class="getStatusBadgeClass(reservation.statut)">
                      {{ getStatusLabel(reservation.statut) }}
                    </span>
                  </div>
                  
                  <p class="text-muted mb-2">
                    <i class="fas fa-map-marker-alt me-2"></i>{{ reservation.quartier }} - {{ reservation.type }}
                  </p>
                  
                  <div class="row g-2 mb-3">
                    <div class="col-auto">
                      <small class="badge bg-light text-dark">
                        <i class="fas fa-calendar me-1"></i>
                        {{ formatDate(reservation.date_arrivee) }} - {{ formatDate(reservation.date_depart) }}
                      </small>
                    </div>
                    <div class="col-auto">
                      <small class="badge bg-light text-dark">
                        <i class="fas fa-users me-1"></i>
                        {{ reservation.nb_adultes }} adulte(s) + {{ reservation.nb_enfants }} enfant(s)
                      </small>
                    </div>
                  </div>
                  
                  <div class="mb-2">
                    <strong>Ticket :</strong> {{ reservation.ticket_id }}
                  </div>
                  
                  <div class="mb-2">
                    <strong>Montant :</strong> ${{ reservation.montant_total }}
                  </div>
                </div>
                
                <div class="col-md-4 text-end">
                  <!-- QR Code (simulé) -->
                  <div class="mb-3">
                    <div class="bg-light p-3 rounded text-center">
                      <i class="fas fa-qrcode fa-3x text-primary mb-2"></i>
                      <div><small class="text-muted">QR Code</small></div>
                    </div>
                  </div>
                  
                  <div class="d-grid gap-2">
                    <button @click="showReservationDetails(reservation)" 
                            class="btn btn-outline-primary btn-sm">
                      <i class="fas fa-eye me-1"></i>Détails
                    </button>
                    
                    <router-link v-if="reservation.statut === 'en_attente'" 
                                 :to="`/verify/${reservation.id}`" 
                                 class="btn btn-success btn-sm">
                      <i class="fas fa-check me-1"></i>Vérifier
                    </router-link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Aucune réservation -->
        <div v-else-if="searched && !searching" class="text-center py-5">
          <i class="fas fa-calendar-times fa-3x text-muted mb-3"></i>
          <h4 class="text-muted">Aucune réservation trouvée</h4>
          <p class="text-muted mb-4">Vérifiez votre numéro de téléphone</p>
          <router-link to="/apartments" class="btn btn-primary">
            <i class="fas fa-plus me-2"></i>Faire une réservation
          </router-link>
        </div>

        <!-- État initial -->
        <div v-else-if="!searched" class="text-center py-5">
          <i class="fas fa-search fa-3x text-muted mb-3"></i>
          <h4 class="text-muted">Recherchez vos réservations</h4>
          <p class="text-muted">Saisissez votre numéro de téléphone pour voir vos réservations</p>
        </div>
      </div>
    </div>

    <!-- Modal détails réservation -->
    <div class="modal fade" id="detailsModal" tabindex="-1">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Détails de la réservation</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body" v-if="selectedReservation">
            <div class="row g-4">
              <div class="col-md-6">
                <h6 class="text-primary mb-3">Informations client</h6>
                <p><strong>Nom :</strong> {{ selectedReservation.nom }}</p>
                <p><strong>Téléphone :</strong> {{ selectedReservation.telephone }}</p>
                <p><strong>Motif :</strong> {{ selectedReservation.motif }}</p>
              </div>
              
              <div class="col-md-6">
                <h6 class="text-primary mb-3">Détails appartement</h6>
                <p><strong>Code :</strong> {{ selectedReservation.code }}</p>
                <p><strong>Quartier :</strong> {{ selectedReservation.quartier }}</p>
                <p><strong>Type :</strong> {{ selectedReservation.type }}</p>
              </div>
              
              <div class="col-12">
                <h6 class="text-primary mb-3">Séjour</h6>
                <div class="row">
                  <div class="col-md-6">
                    <p><strong>Arrivée :</strong> {{ formatDate(selectedReservation.date_arrivee) }}</p>
                    <p><strong>Départ :</strong> {{ formatDate(selectedReservation.date_depart) }}</p>
                  </div>
                  <div class="col-md-6">
                    <p><strong>Adultes :</strong> {{ selectedReservation.nb_adultes }}</p>
                    <p><strong>Enfants :</strong> {{ selectedReservation.nb_enfants }}</p>
                  </div>
                </div>
              </div>
              
              <div class="col-12">
                <div class="alert alert-info">
                  <h6 class="mb-2">Résumé financier</h6>
                  <p class="mb-1"><strong>Montant total :</strong> ${{ selectedReservation.montant_total }}</p>
                  <p class="mb-0"><strong>Ticket :</strong> {{ selectedReservation.ticket_id }}</p>
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
  name: 'MyReservations',
  data() {
    return {
      searchPhone: '',
      reservations: [],
      selectedReservation: null,
      searching: false,
      searched: false
    }
  },
  methods: {
    async searchReservations() {
      if (!this.searchPhone.trim()) return
      
      try {
        this.searching = true
        this.searched = true
        const response = await api.getUserReservations(this.searchPhone)
        this.reservations = response.data || []
      } catch (error) {
        console.error('Erreur lors de la recherche:', error)
        this.reservations = []
      } finally {
        this.searching = false
      }
    },
    
    showReservationDetails(reservation) {
      this.selectedReservation = reservation
      const modal = new Modal(document.getElementById('detailsModal'))
      modal.show()
    },
    
    getStatusLabel(status) {
      const labels = {
        'en_attente': 'En attente',
        'confirmee': 'Confirmée',
        'annulee': 'Annulée',
        'terminee': 'Terminée'
      }
      return labels[status] || status
    },
    
    getStatusBadgeClass(status) {
      const classes = {
        'en_attente': 'badge bg-warning',
        'confirmee': 'badge bg-success',
        'annulee': 'badge bg-danger',
        'terminee': 'badge bg-secondary'
      }
      return classes[status] || 'badge bg-light'
    },
    
    formatDate(dateStr) {
      const date = new Date(dateStr)
      return date.toLocaleDateString('fr-FR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      })
    }
  }
}
</script>