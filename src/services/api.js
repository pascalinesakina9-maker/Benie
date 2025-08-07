import axios from 'axios'

const API_BASE_URL = 'http://localhost/Benie/Backend/api.php'

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

export default {
  // Appartements
  getApartments() {
    return api.get('?entity=appartements')
  },
  
  getApartment(id) {
    return api.get(`?entity=appartements&id=${id}`)
  },

  // Réservations
  createReservation(data) {
    return api.post('?entity=reservations', data)
  },

  getReservations() {
    return api.get('?entity=reservations')
  },

  getUserReservations(phone) {
    return api.get(`?entity=reservations&phone=${phone}`)
  },

  getReservation(id) {
    return api.get(`?entity=reservations&id=${id}`)
  },

  // Vérification QR
  verifyQR(id) {
    return api.get(`?entity=verify_qr&id=${id}`)
  },

  // Authentification admin
  adminLogin(credentials) {
    return api.post('?entity=auth', credentials)
  },

  // Statistiques
  getStats() {
    return api.get('?entity=stats')
  }
}