-- Base de données pour Apparte Aide
-- Système de location d'appartements meublés

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    type ENUM('client', 'admin') DEFAULT 'client',
    mot_de_passe VARCHAR(255) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des appartements
CREATE TABLE IF NOT EXISTS appartements (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(20) UNIQUE NOT NULL,
    quartier ENUM('Muhumba', 'Nguba', 'Avenue Gouverneur', 'Hippodrome', 'Laboute') NOT NULL,
    type VARCHAR(50) NOT NULL,
    nb_chambres INT NOT NULL,
    description TEXT,
    equipements JSON,
    prix_nuit DECIMAL(10,2) NOT NULL,
    statut ENUM('disponible', 'occupe', 'maintenance') DEFAULT 'disponible',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des images d'appartements
CREATE TABLE IF NOT EXISTS appartement_images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    appartement_id INT NOT NULL,
    url VARCHAR(500) NOT NULL,
    ordre INT DEFAULT 0,
    FOREIGN KEY (appartement_id) REFERENCES appartements(id) ON DELETE CASCADE
);

-- Table des réservations
CREATE TABLE IF NOT EXISTS reservations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    appartement_id INT NOT NULL,
    date_arrivee DATE NOT NULL,
    date_depart DATE NOT NULL,
    nb_adultes INT NOT NULL DEFAULT 1,
    nb_enfants INT NOT NULL DEFAULT 0,
    motif VARCHAR(100),
    ticket_id VARCHAR(20) UNIQUE NOT NULL,
    qr_code_url VARCHAR(500),
    acompte DECIMAL(10,2) DEFAULT 0,
    montant_total DECIMAL(10,2) NOT NULL,
    statut ENUM('en_attente', 'confirmee', 'annulee', 'terminee') DEFAULT 'en_attente',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (appartement_id) REFERENCES appartements(id)
);

-- Table des tickets de support
CREATE TABLE IF NOT EXISTS tickets (
    id INT PRIMARY KEY AUTO_INCREMENT,
    reservation_id INT NOT NULL,
    sujet VARCHAR(255) NOT NULL,
    message TEXT,
    statut ENUM('ouvert', 'en_cours', 'ferme') DEFAULT 'ouvert',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

-- Table des paiements
CREATE TABLE IF NOT EXISTS paiements (
    id INT PRIMARY KEY AUTO_INCREMENT,
    reservation_id INT NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    type_paiement ENUM('especes', 'mobile_money', 'virement', 'carte') NOT NULL,
    statut ENUM('en_attente', 'valide', 'echec') DEFAULT 'en_attente',
    reference VARCHAR(100),
    date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

-- Insertion de données de test
INSERT INTO users (nom, telephone, type, mot_de_passe) VALUES 
('Admin Apparte Aide', '+243970000000', 'admin', MD5('admin123'));

INSERT INTO appartements (code, quartier, type, nb_chambres, description, equipements, prix_nuit) VALUES 
('APP001', 'Muhumba', 'Studio', 1, 'Studio moderne avec vue sur le lac', '["Wi-Fi", "TV", "Climatisation", "Cuisine équipée"]', 25.00),
('APP002', 'Nguba', 'T2', 2, 'Appartement 2 chambres spacieux', '["Wi-Fi", "TV", "Parking", "Balcon"]', 40.00),
('APP003', 'Avenue Gouverneur', 'T3', 3, 'Grand appartement familial', '["Wi-Fi", "TV", "Parking", "Cuisine équipée", "Balcon"]', 60.00),
('APP004', 'Hippodrome', 'Studio', 1, 'Studio cosy près du centre', '["Wi-Fi", "TV", "Climatisation"]', 20.00),
('APP005', 'Laboute', 'T2', 2, 'Appartement calme avec jardin', '["Wi-Fi", "TV", "Jardin", "Parking"]', 35.00);

INSERT INTO appartement_images (appartement_id, url, ordre) VALUES 
(1, 'https://images.pexels.com/photos/1457842/pexels-photo-1457842.jpeg', 1),
(1, 'https://images.pexels.com/photos/1454806/pexels-photo-1454806.jpeg', 2),
(2, 'https://images.pexels.com/photos/1571460/pexels-photo-1571460.jpeg', 1),
(2, 'https://images.pexels.com/photos/1454804/pexels-photo-1454804.jpeg', 2),
(3, 'https://images.pexels.com/photos/1571468/pexels-photo-1571468.jpeg', 1),
(3, 'https://images.pexels.com/photos/1454805/pexels-photo-1454805.jpeg', 2),
(4, 'https://images.pexels.com/photos/1457847/pexels-photo-1457847.jpeg', 1),
(4, 'https://images.pexels.com/photos/1454807/pexels-photo-1454807.jpeg', 2),
(5, 'https://images.pexels.com/photos/1571453/pexels-photo-1571453.jpeg', 1),
(5, 'https://images.pexels.com/photos/1457849/pexels-photo-1457849.jpeg', 2);