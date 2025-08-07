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
          <h1 class="h3 fw-bold text-primary">Dashboard Administrateur</h1>
          <div class="text-end">
            <small class="text-muted">Connecté en tant que</small><br>
            <strong>{{ adminUser?.nom || 'Administrateur' }}</strong>
          </div>
        </div>

        <!-- Loading -->
        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary"></div>
        </div>

        <!-- Statistiques -->
        <div v-else class="row g-4">
          <!-- Cards statistiques -->
          <div class="col-xl-3 col-md-6">
            <div class="card border-0 bg-primary text-white shadow">
              <div class="card-body">
                <div class="row align-items-center">
                  <div class="col">
                    <h6 class="text-uppercase text-light mb-2">Appartements</h6>
                    <span class="h2 fw-bold">{{ stats.total_appartements || 0 }}</span>
                  </div>
                  <div class="col-auto">
                    <i class="fas fa-home fa-2x opacity-75"></i>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="col-xl-3 col-md-6">
            <div class="card border-0 bg-success text-white shadow">
              <div class="card-body">
                <div class="row align-items-center">
                  <div class="col">
                    <h6 class="text-uppercase text-light mb-2">Réservations</h6>
                    <span class="h2 fw-bold">{{ stats.total_reservations || 0 }}</span>
                  </div>
                  <div class="col-auto">
                    <i class="fas fa-calendar-check fa-2x opacity-75"></i>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="col-xl-3 col-md-6">
            <div class="card border-0 bg-info text-white shadow">
              <div class="card-body">
                <div class="row align-items-center">
                  <div class="col">
                    <h6 class="text-uppercase text-light mb-2">Revenus</h6>
                    <span class="h2 fw-bold">${{ stats.revenus_totaux || 0 }}</span>
                  </div>
                  <div class="col-auto">
                    <i class="fas fa-dollar-sign fa-2x opacity-75"></i>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="col-xl-3 col-md-6">
            <div class="card border-0 bg-warning text-white shadow">
              <div class="card-body">
                <div class="row align-items-center">
                  <div class="col">
                    <h6 class="text-uppercase text-light mb-2">En Attente</h6>
                    <span class="h2 fw-bold">{{ getReservationsByStatus('en_attente') }}</span>
                  </div>
                  <div class="col-auto">
                    <i class="fas fa-clock fa-2x opacity-75"></i>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Graphiques et détails -->
          <div class="col-xl-8">
            <div class="card border-0 shadow">
              <div class="card-header bg-white">
                <h5 class="mb-0">
                  <i class="fas fa-chart-pie me-2"></i>Réservations par Statut
                </h5>
              </div>
              <div class="card-body">
                <div v-if="stats.reservations_par_statut && stats.reservations_par_statut.length > 0">
                  <div class="row">
                    <div v-for="status in stats.reservations_par_statut" :key="status.statut" class="col-6 mb-3">
                      <div class="d-flex align-items-center">
                        <div class="me-3">
                          <span :class="getStatusDotClass(status.statut)" 
                                class="badge rounded-pill">
                            {{ status.total }}
                          </span>
                        </div>
                        <div>
                          <h6 class="mb-1">{{ getStatusLabel(status.statut) }}</h6>
                          <small class="text-muted">{{ status.total }} réservation(s)</small>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div v-else class="text-center py-4 text-muted">
                  <i class="fas fa-chart-pie fa-3x mb-3"></i>
                  <p>Aucune donnée disponible</p>
                </div>
              </div>
            </div>
          </div>

          <div class="col-xl-4">
            <div class="card border-0 shadow">
              <div class="card-header bg-white">
                <h5 class="mb-0">
                  <i class="fas fa-tasks me-2"></i>Actions Rapides
                </h5>
              </div>
              <div class="card-body">
                <div class="d-grid gap-2">
                  <router-link to="/admin/apartments" class="btn btn-outline-primary">
                    <i class="fas fa-plus me-2"></i>Ajouter Appartement
                  </router-link>
                  
                  <router-link to="/admin/reservations" class="btn btn-outline-success">
                    <i class="fas fa-eye me-2"></i>Voir Réservations
                  </router-link>
                  
                  <button @click="refreshStats" class="btn btn-outline-info" :disabled="loading">
                    <i class="fas fa-sync-alt me-2"></i>Actualiser
                  </button>
                  
                  <hr>
                  
                  <button @click="logout" class="btn btn-outline-danger">
                    <i class="fas fa-sign-out-alt me-2"></i>Déconnexion
                  </button>
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

export default {
  name: 'AdminDashboard',
  data() {
    return {
      stats: {},
      loading: true,
      adminUser: null
    }
  },
  async created() {
    this.adminUser = JSON.parse(localStorage.getItem('admin_user') || '{}')
    await this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        this.loading = true
        const response = await api.getStats()
        this.stats = response.data
      } catch (error) {
        console.error('Erreur lors du chargement des statistiques:', error)
      } finally {
        this.loading = false
      }
    },
    
    async refreshStats() {
      await this.loadStats()
    },
    
    getReservationsByStatus(status) {
      const statusData = this.stats.reservations_par_statut?.find(s => s.statut === status)
      return statusData?.total || 0
    },
    
    getStatusLabel(status) {
      const labels = {
        'en_attente': 'En attente',
        'confirmee': 'Confirmées',
        'annulee': 'Annulées',
        'terminee': 'Terminées'
      }
      return labels[status] || status
    },
    
    getStatusDotClass(status) {
      const classes = {
        'en_attente': 'bg-warning',
        'confirmee': 'bg-success',
        'annulee': 'bg-danger',
        'terminee': 'bg-secondary'
      }
      return classes[status] || 'bg-light'
    },
    
    logout() {
      localStorage.removeItem('admin_user')
      this.$router.push('/admin/login')
    }
  }
}
</script>