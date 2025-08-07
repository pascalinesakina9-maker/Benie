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
              <router-link to="/admin/apartments" class="nav-link text-dark">
                <i class="fas fa-home me-2"></i>Appartements
              </router-link>
              <router-link to="/admin/reservations" class="nav-link text-primary fw-bold">
                <i class="fas fa-calendar me-2"></i>Réservations
              </router-link>
            </nav>
          </div>
        </div>
      </div>

      <!-- Main content -->
      <div class="col-md-10">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h1 class="h3 fw-bold text-primary">Gestion des Réservations</h1>
          <div class="d-flex gap-2">
            <button @click="refreshReservations" class="btn btn-outline-primary" :disabled="loading">
              <i class="fas fa-sync-alt me-2"></i>Actualiser
            </button>
          </div>
        </div>

        <!-- Filtres -->
        <div class="card border-0 shadow-sm mb-4">
          <div class="card-body">
            <div class="row g-3">
              <div class="col-md-3">
                <select v-model="filters.statut" @change="filterReservations" class="form-select">
                  <option value="">Tous les statuts</option>
                  <option value="en_attente">En attente</option>
                  <option value="confirmee">Confirmée</option>
                  <option value="annulee">Annulée</option>
                  <option value="terminee">Terminée</option>
                </select>
              </div>
              
              <div class="col-md-3">
                <select v-model="filters.quartier" @change="filterReservations" class="form-select">
                  <option value="">Tous les quartiers</option>
                  <option value="Muhumba">Muhumba</option>
                  <option value="Nguba">Nguba</option>
                  <option value="Avenue Gouverneur">Avenue Gouverneur</option>
                  <option value="Hippodrome">Hippodrome</option>
                  <option value="Laboute">Laboute</option>
                </select>
              </div>
              
              <div class="col-md-6">
                <div class="input-group">
                  <input v-model="searchTerm" @input="filterReservations" 
                         type="text" class="form-control" placeholder="Rechercher par nom, ticket ou téléphone...">
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

        <!-- Table des réservations -->
        <div v-else class="card border-0 shadow">
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-hover mb-0">
                <thead class="bg-light">
                  <tr>
                    <th>Ticket</th>
                    <th>Client</th>
                    <th>Appartement</th>
                    <th>Dates</th>
                    <th>Montant</th>
                    <th>Statut</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="reservation in filteredReservations" :key="reservation.id">
                    <td>
                      <strong class="text-primary">{{ reservation.ticket_id }}</strong>
                      <br>
                      <small class="text-muted">#{{ reservation.id }}</small>
                    </td>
                    <td>
                      <div>
                        <strong>{{ reservation.nom }}</strong>
                        <br>
                        <small class="text-muted">{{ reservation.telephone }}</small>
                      </div>
                    </td>
                    <td>
                      <span class="badge bg-info">{{ reservation.code }}</span>
                      <br>
                      <small class="text-muted">{{ reservation.quartier }}</small>
                    </td>
                    <td>
                      <div>
                        <small>{{ formatDate(reservation.date_arrivee) }}</small>
                        <br>
                        <small>{{ formatDate(reservation.date_depart) }}</small>
                      </div>
                    </td>
                    <td>
                      <strong class="text-success">${{ reservation.montant_total }}</strong>
                      <br>
                      <small class="text-muted">{{ reservation.nb_adultes }}A + {{ reservation.nb_enfants }}E</small>
                    </td>
                    <td>
                      <span :class="getStatusBadgeClass(reservation.statut)">
                        {{ getStatusLabel(reservation.statut) }}
                      </span>
                    </td>
                    <td>
                      <div class="btn-group btn-group-sm">
                        <button @click="viewReservation(reservation)" 
                                class="btn btn-outline-primary" title="Voir détails">
                          <i class="fas fa-eye"></i>
                        </button>
                        <button @click="updateStatus(reservation, 'confirmee')" 
                                v-if="reservation.statut === 'en_attente'"
                                class="btn btn-outline-success" title="Confirmer">
                          <i class="fas fa-check"></i>
                        </button>
                        <button @click="updateStatus(reservation, 'annulee')" 
                                v-if="reservation.statut !== 'annulee'"
                                class="btn btn-outline-danger" title="Annuler">
                          <i class="fas fa-times"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                  
                  <tr v-if="filteredReservations.length === 0">
                    <td colspan="7" class="text-center py-4 text-muted">
                      <i class="fas fa-calendar-times fa-2x mb-2"></i>
                      <div>Aucune réservation trouvée</div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal détails réservation -->
    <div class="modal fade" id="reservationModal" tabindex="-1">
      <div class="modal-dialog modal-xl">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">
              <i class="fas fa-calendar me-2"></i>Réservation {{ selectedReservation?.ticket_id }}
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body" v-if="selectedReservation">
            <div class="row g-4">
              <div class="col-md-6">
                <h6 class="text-primary mb-3">
                  <i class="fas fa-user me-2"></i>Informations Client
                </h6>
                <p><strong>Nom :</strong> {{ selectedReservation.nom }}</p>
                <p><strong>Téléphone :</strong> {{ selectedReservation.telephone }}</p>
                <p><strong>Motif :</strong> {{ selectedReservation.motif || 'Non spécifié' }}</p>
                <p><strong>Adultes :</strong> {{ selectedReservation.nb_adultes }}</p>
                <p><strong>Enfants :</strong> {{ selectedReservation.nb_enfants }}</p>
              </div>
              
              <div class="col-md-6">
                <h6 class="text-primary mb-3">
                  <i class="fas fa-home me-2"></i>Détails Appartement
                </h6>
                <p><strong>Code :</strong> {{ selectedReservation.code }}</p>
                <p><strong>Quartier :</strong> {{ selectedReservation.quartier }}</p>
                <p><strong>Type :</strong> {{ selectedReservation.type }}</p>
                <p><strong>Arrivée :</strong> {{ formatDate(selectedReservation.date_arrivee) }}</p>
                <p><strong>Départ :</strong> {{ formatDate(selectedReservation.date_depart) }}</p>
              </div>
              
              <div class="col-md-6">
                <h6 class="text-primary mb-3">
                  <i class="fas fa-dollar-sign me-2"></i>Informations Financières
                </h6>
                <p><strong>Montant total :</strong> <span class="text-success">${{ selectedReservation.montant_total }}</span></p>
                <p><strong>Acompte :</strong> ${{ selectedReservation.acompte || 0 }}</p>
                <p><strong>Reste à payer :</strong> ${{ (selectedReservation.montant_total - (selectedReservation.acompte || 0)) }}</p>
              </div>
              
              <div class="col-md-6">
                <h6 class="text-primary mb-3">
                  <i class="fas fa-info-circle me-2"></i>Statut & QR Code
                </h6>
                <p><strong>Statut :</strong> 
                  <span :class="getStatusBadgeClass(selectedReservation.statut)">
                    {{ getStatusLabel(selectedReservation.statut) }}
                  </span>
                </p>
                <p><strong>Créée le :</strong> {{ formatDateTime(selectedReservation.created_at) }}</p>
                
                <!-- QR Code (simulé) -->
                <div class="bg-light p-3 rounded text-center">
                  <i class="fas fa-qrcode fa-3x text-primary mb-2"></i>
                  <div><small class="text-muted">QR Code de vérification</small></div>
                  <div class="mt-2">
                    <router-link :to="`/verify/${selectedReservation.id}`" 
                                 target="_blank" 
                                 class="btn btn-sm btn-outline-primary">
                      <i class="fas fa-external-link-alt me-1"></i>Tester QR
                    </router-link>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <div class="me-auto">
              <div class="btn-group">
                <button @click="updateStatus(selectedReservation, 'confirmee')" 
                        v-if="selectedReservation?.statut === 'en_attente'"
                        class="btn btn-success">
                  <i class="fas fa-check me-2"></i>Confirmer
                </button>
                <button @click="updateStatus(selectedReservation, 'terminee')" 
                        v-if="selectedReservation?.statut === 'confirmee'"
                        class="btn btn-info">
                  <i class="fas fa-flag-checkered me-2"></i>Terminer
                </button>
                <button @click="updateStatus(selectedReservation, 'annulee')" 
                        v-if="selectedReservation?.statut !== 'annulee'"
                        class="btn btn-danger">
                  <i class="fas fa-times me-2"></i>Annuler
                </button>
              </div>
            </div>
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
  name: 'AdminReservations',
  data() {
    return {
      reservations: [],
      filteredReservations: [],
      selectedReservation: null,
      loading: true,
      filters: {
        statut: '',
        quartier: ''
      },
      searchTerm: ''
    }
  },
  async created() {
    await this.loadReservations()
  },
  methods: {
    async loadReservations() {
      try {
        this.loading = true
        const response = await api.getReservations()
        this.reservations = response.data || []
        this.filteredReservations = [...this.reservations]
      } catch (error) {
        console.error('Erreur lors du chargement des réservations:', error)
      } finally {
        this.loading = false
      }
    },
    
    async refreshReservations() {
      await this.loadReservations()
    },
    
    filterReservations() {
      this.filteredReservations = this.reservations.filter(reservation => {
        let matches = true
        
        if (this.filters.statut && reservation.statut !== this.filters.statut) {
          matches = false
        }
        
        if (this.filters.quartier && reservation.quartier !== this.filters.quartier) {
          matches = false
        }
        
        if (this.searchTerm) {
          const searchLower = this.searchTerm.toLowerCase()
          const matchesSearch = 
            reservation.nom.toLowerCase().includes(searchLower) ||
            reservation.ticket_id.toLowerCase().includes(searchLower) ||
            reservation.telephone.includes(searchLower)
          if (!matchesSearch) matches = false
        }
        
        return matches
      })
    },
    
    viewReservation(reservation) {
      this.selectedReservation = reservation
      const modal = new Modal(document.getElementById('reservationModal'))
      modal.show()
    },
    
    async updateStatus(reservation, newStatus) {
      if (confirm(`Changer le statut de la réservation ${reservation.ticket_id} vers "${this.getStatusLabel(newStatus)}" ?`)) {
        // TODO: Implémenter la mise à jour du statut
        alert(`Fonctionnalité de mise à jour du statut vers "${newStatus}" à implémenter`)
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
    
    formatDateTime(dateStr) {
      const date = new Date(dateStr)
      return date.toLocaleDateString('fr-FR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
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