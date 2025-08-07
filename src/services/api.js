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
  },

  // Mise à jour d'appartement
  updateApartment(id, data) {
    return api.post('?entity=update_apartment', { ...data, id })
  },

  // Suppression d'image d'appartement
  deleteApartmentImage(imageId) {
    return api.post('?entity=delete_apartment_image', { image_id: imageId })
  },

  // Ajout d'images à un appartement
  addApartmentImages(apartmentId, images) {
    return api.post('?entity=add_apartment_images', { apartment_id: apartmentId, images })
  }
}