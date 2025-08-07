<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-lg-4 col-md-6">
        <div class="card border-0 shadow-lg">
          <div class="card-body p-5">
            <div class="text-center mb-4">
              <i class="fas fa-user-shield fa-3x text-primary mb-3"></i>
              <h3 class="fw-bold text-primary">Administration</h3>
              <p class="text-muted">Connexion au panneau d'administration</p>
            </div>

            <form @submit.prevent="login">
              <div class="mb-3">
                <label class="form-label">Nom d'utilisateur</label>
                <div class="input-group">
                  <span class="input-group-text">
                    <i class="fas fa-user"></i>
                  </span>
                  <input v-model="credentials.username" 
                         type="text" 
                         class="form-control" 
                         placeholder="Nom d'utilisateur ou téléphone"
                         required>
                </div>
              </div>

              <div class="mb-4">
                <label class="form-label">Mot de passe</label>
                <div class="input-group">
                  <span class="input-group-text">
                    <i class="fas fa-lock"></i>
                  </span>
                  <input v-model="credentials.password" 
                         :type="showPassword ? 'text' : 'password'" 
                         class="form-control" 
                         placeholder="Mot de passe"
                         required>
                  <button type="button" 
                          @click="showPassword = !showPassword"
                          class="btn btn-outline-secondary">
                    <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
                  </button>
                </div>
              </div>

              <div v-if="error" class="alert alert-danger">
                <i class="fas fa-exclamation-triangle me-2"></i>{{ error }}
              </div>

              <button type="submit" 
                      class="btn btn-primary w-100 btn-lg" 
                      :disabled="loading">
                <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                {{ loading ? 'Connexion...' : 'Se connecter' }}
              </button>
            </form>

            <div class="text-center mt-4">
              <small class="text-muted">
                <i class="fas fa-info-circle me-1"></i>
                Accès réservé aux administrateurs
              </small>
            </div>
          </div>
        </div>

        <!-- Informations de test -->
        <div class="card mt-3 border-info">
          <div class="card-body">
            <h6 class="text-info mb-2">
              <i class="fas fa-info-circle me-2"></i>Compte de test
            </h6>
            <p class="small mb-1"><strong>Utilisateur :</strong> +243970000000</p>
            <p class="small mb-0"><strong>Mot de passe :</strong> admin123</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/services/api'

export default {
  name: 'AdminLogin',
  data() {
    return {
      credentials: {
        username: '',
        password: ''
      },
      loading: false,
      error: null,
      showPassword: false
    }
  },
  methods: {
    async login() {
      try {
        this.loading = true
        this.error = null
        
        const response = await api.adminLogin(this.credentials)
        
        if (response.data.success) {
          // Stocker les informations de l'admin
          localStorage.setItem('admin_user', JSON.stringify(response.data.user))
          
          // Rediriger vers le dashboard
          this.$router.push('/admin')
        } else {
          this.error = response.data.message || 'Identifiants incorrects'
        }
      } catch (error) {
        console.error('Erreur de connexion:', error)
        this.error = 'Erreur de connexion. Veuillez réessayer.'
      } finally {
        this.loading = false
      }
    }
  },
  created() {
    // Rediriger si déjà connecté
    if (localStorage.getItem('admin_user')) {
      this.$router.push('/admin')
    }
  }
}
</script>