# Apparte Aide - Système de Location d'Appartements Meublés

Site web complet pour la gestion et la location d'appartements meublés à Bukavu, République Démocratique du Congo.

## 🏠 Fonctionnalités

### Interface Utilisateur
- **Catalogue d'appartements** : Parcourir plus de 35 appartements dans 5 quartiers
- **Réservation en ligne** : Formulaire complet avec génération automatique de QR code
- **Suivi des réservations** : Recherche par numéro de téléphone
- **Vérification QR** : Système de validation des réservations via QR code

### Interface Administrateur
- **Dashboard** : Statistiques et aperçu général
- **Gestion des appartements** : CRUD complet avec gestion multi-images
- **Gestion des réservations** : Suivi, filtrage et mise à jour des statuts
- **Système de tickets** : Support client intégré

## 🏗️ Architecture Technique

### Frontend (Vue.js 3)
- **Framework** : Vue.js 3 avec Composition API
- **Routage** : Vue Router avec guards d'authentification
- **UI** : Bootstrap 5 + Font Awesome
- **HTTP** : Axios pour les appels API
- **Responsive** : Design mobile-first

### Backend (PHP)
- **API** : PHP avec architecture REST
- **Base de données** : MySQL avec PDO
- **QR Code** : Génération automatique pour chaque réservation
- **Sécurité** : Authentification simple et validation des données

### Base de Données (MySQL)
```sql
- users (clients et administrateurs)
- appartements (informations des logements)
- appartement_images (galerie multi-images)
- reservations (avec génération de tickets et QR codes)
- tickets (support client)
- paiements (suivi financier)
```

## 🚀 Installation

### Prérequis
- Node.js 18+
- PHP 8.0+
- MySQL 8.0+
- Serveur web (Apache/Nginx)

### Frontend
```bash
npm install
npm run dev
```

### Backend
1. Configurer la base de données dans `backend/config/db.php`
2. Importer le schéma depuis `database/schema.sql`
3. Configurer le serveur web pour pointer vers `/backend/`

### Configuration
- **Base de données** : Modifier les paramètres dans `backend/config/db.php`
- **API URL** : Ajuster `API_BASE_URL` dans `src/services/api.js`

## 📱 Fonctionnalités par Interface

### Client
- ✅ Parcourir les appartements avec filtres
- ✅ Réserver avec génération automatique de QR code
- ✅ Suivre ses réservations par téléphone
- ✅ Vérifier une réservation via QR code

### Administrateur
- ✅ Dashboard avec statistiques temps réel
- ✅ Gestion complète des appartements et images
- ✅ Suivi et validation des réservations
- ✅ Scanner QR pour vérification

## 🎨 Design

- **Couleurs** : Bleu primaire (#0d6efd), vert succès (#198754)
- **Typographie** : Segoe UI avec hiérarchie claire
- **Animations** : Transitions fluides et micro-interactions
- **Responsive** : Optimisé mobile, tablette et desktop

## 🏘️ Quartiers Couverts

1. **Muhumba** - Vue sur le lac Kivu
2. **Nguba** - Zone résidentielle calme
3. **Avenue Gouverneur** - Centre-ville
4. **Hippodrome** - Quartier dynamique  
5. **Laboute** - Zone tranquille avec espaces verts

## 🔐 Authentification

**Compte administrateur de test :**
- Utilisateur : `+243970000000`
- Mot de passe : `admin123`

## 📊 Statistiques

- Total des appartements
- Réservations par statut
- Revenus générés
- Tableau de bord en temps réel

## 🔧 API Endpoints

```
GET  /api.php?entity=appartements - Liste des appartements
POST /api.php?entity=reservations - Créer une réservation
GET  /api.php?entity=verify_qr&id={id} - Vérifier QR code
POST /api.php?entity=auth - Authentification admin
GET  /api.php?entity=stats - Statistiques dashboard
```

## 📄 License

Projet développé pour Apparte Aide - Bukavu, RDC

---

**Contact** : Système de réservation moderne avec QR code pour une expérience client optimale.