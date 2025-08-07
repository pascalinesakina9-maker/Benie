<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization');

if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {
    exit(0);
}

require_once 'config/db.php';

// Fonction pour générer un QR code
function generateQRCode($data, $filename) {
    $qr_dir = 'assets/qrcodes/';
    if (!file_exists($qr_dir)) {
        mkdir($qr_dir, 0777, true);
    }
    
    // Simulation de génération QR code (en production, utiliser phpqrcode)
    $qr_content = "QR Code pour: " . $data;
    file_put_contents($qr_dir . $filename, $qr_content);
    return $qr_dir . $filename;
}

// Fonction pour générer un ticket ID unique
function generateTicketId() {
    return 'TK' . date('Ymd') . rand(1000, 9999);
}

$database = new Database();
$db = $database->getConnection();

$entity = isset($_GET['entity']) ? $_GET['entity'] : '';
$method = $_SERVER['REQUEST_METHOD'];

try {
    switch ($entity) {
        case 'appartements':
            if ($method === 'GET') {
                $id = isset($_GET['id']) ? $_GET['id'] : null;
                
                if ($id) {
                    // Récupérer un appartement spécifique avec ses images
                    $query = "SELECT a.*, GROUP_CONCAT(ai.url ORDER BY ai.ordre) as images 
                             FROM appartements a 
                             LEFT JOIN appartement_images ai ON a.id = ai.appartement_id 
                             WHERE a.id = ? GROUP BY a.id";
                    $stmt = $db->prepare($query);
                    $stmt->bindParam(1, $id);
                } else {
                    // Récupérer tous les appartements disponibles avec leurs images
                    $query = "SELECT a.*, GROUP_CONCAT(ai.url ORDER BY ai.ordre) as images 
                             FROM appartements a 
                             LEFT JOIN appartement_images ai ON a.id = ai.appartement_id 
                             WHERE a.statut = 'disponible' 
                             GROUP BY a.id";
                    $stmt = $db->prepare($query);
                }
                
                $stmt->execute();
                $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
                
                // Traiter les images pour chaque appartement
                foreach ($result as &$appartement) {
                    $appartement['images'] = $appartement['images'] ? explode(',', $appartement['images']) : [];
                    $appartement['equipements'] = json_decode($appartement['equipements'], true) ?: [];
                }
                
                echo json_encode($id ? $result[0] : $result);
            }
            break;

        case 'reservations':
            if ($method === 'POST') {
                // Créer une nouvelle réservation
                $data = json_decode(file_get_contents('php://input'), true);
                
                // Vérifier si l'utilisateur existe, sinon le créer
                $query = "SELECT id FROM users WHERE telephone = ?";
                $stmt = $db->prepare($query);
                $stmt->bindParam(1, $data['telephone']);
                $stmt->execute();
                $user = $stmt->fetch(PDO::FETCH_ASSOC);
                
                if (!$user) {
                    // Créer un nouveau utilisateur
                    $query = "INSERT INTO users (nom, telephone) VALUES (?, ?)";
                    $stmt = $db->prepare($query);
                    $stmt->bindParam(1, $data['nom']);
                    $stmt->bindParam(2, $data['telephone']);
                    $stmt->execute();
                    $user_id = $db->lastInsertId();
                } else {
                    $user_id = $user['id'];
                }
                
                // Calculer le montant total (exemple: prix par nuit * nombre de nuits)
                $date_arrivee = new DateTime($data['date_arrivee']);
                $date_depart = new DateTime($data['date_depart']);
                $nb_nuits = $date_arrivee->diff($date_depart)->days;
                
                $query = "SELECT prix_nuit FROM appartements WHERE id = ?";
                $stmt = $db->prepare($query);
                $stmt->bindParam(1, $data['appartement_id']);
                $stmt->execute();
                $appartement = $stmt->fetch(PDO::FETCH_ASSOC);
                $montant_total = $appartement['prix_nuit'] * $nb_nuits;
                
                // Générer ticket ID et créer la réservation
                $ticket_id = generateTicketId();
                
                $query = "INSERT INTO reservations (user_id, appartement_id, date_arrivee, date_depart, nb_adultes, nb_enfants, motif, ticket_id, montant_total) 
                         VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                $stmt = $db->prepare($query);
                $stmt->execute([
                    $user_id,
                    $data['appartement_id'],
                    $data['date_arrivee'],
                    $data['date_depart'],
                    $data['nb_adultes'],
                    $data['nb_enfants'],
                    $data['motif'],
                    $ticket_id,
                    $montant_total
                ]);
                
                $reservation_id = $db->lastInsertId();
                
                // Générer le QR code
                $qr_filename = "user_{$user_id}/reservation_{$reservation_id}.png";
                $qr_data = "http://localhost/backend/api.php?entity=verify_qr&id={$reservation_id}";
                $qr_path = generateQRCode($qr_data, $qr_filename);
                
                // Mettre à jour la réservation avec l'URL du QR code
                $query = "UPDATE reservations SET qr_code_url = ? WHERE id = ?";
                $stmt = $db->prepare($query);
                $stmt->execute([$qr_path, $reservation_id]);
                
                echo json_encode([
                    'success' => true,
                    'reservation_id' => $reservation_id,
                    'ticket_id' => $ticket_id,
                    'montant_total' => $montant_total,
                    'qr_code_url' => $qr_path
                ]);
                
            } elseif ($method === 'GET') {
                $id = isset($_GET['id']) ? $_GET['id'] : null;
                $user_phone = isset($_GET['phone']) ? $_GET['phone'] : null;
                
                if ($id) {
                    // Récupérer une réservation spécifique
                    $query = "SELECT r.*, u.nom, u.telephone, a.code, a.quartier, a.type 
                             FROM reservations r 
                             JOIN users u ON r.user_id = u.id 
                             JOIN appartements a ON r.appartement_id = a.id 
                             WHERE r.id = ?";
                    $stmt = $db->prepare($query);
                    $stmt->bindParam(1, $id);
                } elseif ($user_phone) {
                    // Récupérer les réservations d'un utilisateur par téléphone
                    $query = "SELECT r.*, u.nom, u.telephone, a.code, a.quartier, a.type 
                             FROM reservations r 
                             JOIN users u ON r.user_id = u.id 
                             JOIN appartements a ON r.appartement_id = a.id 
                             WHERE u.telephone = ? 
                             ORDER BY r.created_at DESC";
                    $stmt = $db->prepare($query);
                    $stmt->bindParam(1, $user_phone);
                } else {
                    // Récupérer toutes les réservations (admin)
                    $query = "SELECT r.*, u.nom, u.telephone, a.code, a.quartier, a.type 
                             FROM reservations r 
                             JOIN users u ON r.user_id = u.id 
                             JOIN appartements a ON r.appartement_id = a.id 
                             ORDER BY r.created_at DESC";
                    $stmt = $db->prepare($query);
                }
                
                $stmt->execute();
                $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
                
                echo json_encode($id ? ($result ? $result[0] : null) : $result);
            }
            break;

        case 'verify_qr':
            if ($method === 'GET') {
                $id = isset($_GET['id']) ? $_GET['id'] : null;
                
                if ($id) {
                    $query = "SELECT r.*, u.nom, u.telephone, a.code, a.quartier, a.type, a.description 
                             FROM reservations r 
                             JOIN users u ON r.user_id = u.id 
                             JOIN appartements a ON r.appartement_id = a.id 
                             WHERE r.id = ?";
                    $stmt = $db->prepare($query);
                    $stmt->bindParam(1, $id);
                    $stmt->execute();
                    $reservation = $stmt->fetch(PDO::FETCH_ASSOC);
                    
                    if ($reservation) {
                        echo json_encode([
                            'success' => true,
                            'reservation' => $reservation,
                            'message' => 'Réservation trouvée'
                        ]);
                    } else {
                        echo json_encode([
                            'success' => false,
                            'message' => 'Réservation introuvable'
                        ]);
                    }
                } else {
                    echo json_encode([
                        'success' => false,
                        'message' => 'ID de réservation manquant'
                    ]);
                }
            }
            break;

        case 'auth':
            if ($method === 'POST') {
                $data = json_decode(file_get_contents('php://input'), true);
                
                $query = "SELECT * FROM users WHERE telephone = ? AND type = 'admin' AND mot_de_passe = MD5(?)";
                $stmt = $db->prepare($query);
                $stmt->execute([$data['username'], $data['password']]);
                $admin = $stmt->fetch(PDO::FETCH_ASSOC);
                
                if ($admin) {
                    echo json_encode([
                        'success' => true,
                        'user' => [
                            'id' => $admin['id'],
                            'nom' => $admin['nom'],
                            'type' => $admin['type']
                        ]
                    ]);
                } else {
                    echo json_encode([
                        'success' => false,
                        'message' => 'Identifiants incorrects'
                    ]);
                }
            }
            break;

        case 'stats':
            if ($method === 'GET') {
                // Statistiques pour le dashboard admin
                $stats = [];
                
                // Total appartements
                $query = "SELECT COUNT(*) as total FROM appartements";
                $stmt = $db->prepare($query);
                $stmt->execute();
                $stats['total_appartements'] = $stmt->fetch(PDO::FETCH_ASSOC)['total'];
                
                // Total réservations
                $query = "SELECT COUNT(*) as total FROM reservations";
                $stmt = $db->prepare($query);
                $stmt->execute();
                $stats['total_reservations'] = $stmt->fetch(PDO::FETCH_ASSOC)['total'];
                
                // Réservations par statut
                $query = "SELECT statut, COUNT(*) as total FROM reservations GROUP BY statut";
                $stmt = $db->prepare($query);
                $stmt->execute();
                $stats['reservations_par_statut'] = $stmt->fetchAll(PDO::FETCH_ASSOC);
                
                // Revenus totaux
                $query = "SELECT SUM(montant_total) as total FROM reservations WHERE statut IN ('confirmee', 'terminee')";
                $stmt = $db->prepare($query);
                $stmt->execute();
                $stats['revenus_totaux'] = $stmt->fetch(PDO::FETCH_ASSOC)['total'] ?: 0;
                
                echo json_encode($stats);
            }
            break;

        default:
            echo json_encode(['error' => 'Endpoint non trouvé']);
            break;
    }
} catch (Exception $e) {
    echo json_encode(['error' => $e->getMessage()]);
}
?>