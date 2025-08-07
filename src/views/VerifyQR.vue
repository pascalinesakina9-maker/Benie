<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-lg-6">
        <div class="text-center mb-4">
          <h1 class="display-6 fw-bold text-primary mb-3">
            <i class="fas fa-qrcode me-3"></i>Vérification QR
          </h1>
          <p class="lead text-muted">Vérification de votre réservation</p>
        </div>

        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary mb-3" style="width: 3rem; height: 3rem;"></div>
          <p class="text-muted">Vérification en cours...</p>
        </div>

        <!-- Réservation trouvée -->
        <div v-else-if="reservation" class="card border-0 shadow-lg">
          <div class="card-header bg-success text-white text-center">
            <h4 class="mb-0">
              <i class="fas fa-check-circle me-2"></i>Réservation Valide
            </h4>
          </div>
          
          <div class="card-body p-4">
            <div class="row g-4">
              <div class="col-12 text-center mb-3">
                <i class="fas fa-ticket-alt fa-3x text-success mb-3"></i>
                <h5 class="text-primary">Ticket : {{ reservation.ticket_id }}</h5>
              </div>
              
              <div class="col-md-6">
                <h6 class="text-success mb-3">
                  <i class="fas fa-user me-2"></i>Informations Client
                </h6>
                <p><strong>Nom :</strong> {{ reservation.nom }}</p>
                <p><strong>Téléphone :</strong> {{ reservation.telephone }}</p>
                <p><strong>Motif :</strong> {{ reservation.motif || 'Non spécifié' }}</p>
              </div>
              
              <div class="col-md-6">
                <h6 class="text-success mb-3">
                  <i class="fas fa-home me-2"></i>Appartement
                </h6>
                <p><strong>Code :</strong> {{ reservation.code }}</p>
                <p><strong>Quartier :</strong> {{ reservation.quartier }}</p>
                <p><strong>Type :</strong> {{ reservation.type }}</p>
              </div>
              
              <div class="col-12">
                <h6 class="text-success mb-3">
                  <i class="fas fa-calendar me-2"></i>Détails du Séjour
                </h6>
                <div class="row">
                  <div class="col-md-6">
                    <p><strong>Arrivée :</strong> {{ formatDate(reservation.date_arrivee) }}</p>
                    <p><strong>Départ :</strong> {{ formatDate(reservation.date_depart) }}</p>
                  </div>
                  <div class="col-md-6">
                    <p><strong>Adultes :</strong> {{ reservation.nb_adultes }}</p>
                    <p><strong>Enfants :</strong> {{ reservation.nb_enfants }}</p>
                  </div>
                </div>
              </div>
              
              <div class="col-12">
                <div class="alert alert-info mb-0">
                  <div class="row align-items-center">
                    <div class="col-md-8">
                      <h6 class="mb-1">
                        <i class="fas fa-dollar-sign me-2"></i>Montant Total
                      </h6>
                      <span class="h4 text-primary">${{ reservation.montant_total }}</span>
                    </div>
                    <div class="col-md-4 text-end">
                      <span :class="getStatusBadgeClass(reservation.statut)" class="fs-6">
                        {{ getStatusLabel(reservation.statut) }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="card-footer text-center bg-light">
            <small class="text-muted">
              <i class="fas fa-clock me-1"></i>
              Réservation créée le {{ formatDate(reservation.created_at) }}
            </small>
          </div>
        </div>

        <!-- Réservation non trouvée -->
        <div v-else class="card border-0 shadow-lg">
          <div class="card-header bg-danger text-white text-center">
            <h4 class="mb-0">
              <i class="fas fa-exclamation-triangle me-2"></i>Réservation Introuvable
            </h4>
          </div>
          
          <div class="card-body text-center p-5">
            <i class="fas fa-times-circle fa-4x text-danger mb-4"></i>
            <h5 class="text-danger mb-3">Aucune réservation trouvée</h5>
            <p class="text-muted mb-4">
              Le code QR scanné ne correspond à aucune réservation valide dans notre système.
            </p>
            
            <div class="d-grid gap-2 d-md-flex justify-content-md-center">
              <router-link to="/apartments" class="btn btn-primary">
                <i class="fas fa-search me-2"></i>Voir les appartements
              </router-link>
              <router-link to="/my-reservations" class="btn btn-outline-primary">
                <i class="fas fa-calendar me-2"></i>Mes réservations
              </router-link>
            </div>
          </div>
        </div>

        <!-- Bouton retour -->
        <div class="text-center mt-4">
          <router-link to="/" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left me-2"></i>Retour à l'accueil
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/services/api'

export default {
  name: 'VerifyQR',
  props: ['id'],
  data() {
    return {
      reservation: null,
      loading: true,
      error: null
    }
  },
  async created() {
    await this.verifyReservation()
  },
  methods: {
    async verifyReservation() {
      try {
        this.loading = true
        const response = await api.verifyQR(this.id)
        
        if (response.data.success) {
          this.reservation = response.data.reservation
        } else {
          this.reservation = null
        }
      } catch (error) {
        console.error('Erreur lors de la vérification:', error)
        this.reservation = null
      } finally {
        this.loading = false
      }
    },
    
    formatDate(dateStr) {
      const date = new Date(dateStr)
      return date.toLocaleDateString('fr-FR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      })
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
    }
  }
}
</script>