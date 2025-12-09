package com.emargement.dao;

import com.emargement.model.Emargement;
import com.emargement.dao.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class EmargementDAO {

    /**
     * Enregistre un émargement (utilisé par l'étudiant via code).
     */
    public boolean save(Emargement emargement) {
        String sql = "INSERT INTO emargement (seanceId, etudiantId, present, heureEmargement) VALUES (?, ?, 1, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, emargement.getSeanceId());
            stmt.setInt(2, emargement.getEtudiantId());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("L'étudiant a déjà émargé pour cette séance.");
            } else {
                System.err.println("Erreur SQL lors de l'enregistrement de l'émargement: " + e.getMessage());
            }
            return false;
        }
    }

    /**
     * Vérifie si un étudiant a déjà émargé pour une séance donnée.
     */
    public boolean hasAttended(int seanceId, int etudiantId) {
        String sql = "SELECT COUNT(*) FROM emargement WHERE seanceId = ? AND etudiantId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seanceId);
            stmt.setInt(2, etudiantId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la vérification de présence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour ou insère l'enregistrement de présence manuellement (transactionnel).
     * ⭐️ LOGIQUE CORRIGÉE : Utilise DELETE puis INSERT. ⭐️
     */
    public boolean saveOrDeleteAttendance(int seanceId, int etudiantId, boolean estPresent) {
        String deleteSql = "DELETE FROM emargement WHERE seanceId = ? AND etudiantId = ?";
        String insertSql = "INSERT INTO emargement (seanceId, etudiantId, present, heureEmargement) VALUES (?, ?, 1, ?)";
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Début de la transaction

            // 1. Suppression de toute ligne existante (nettoyage)
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, seanceId);
                deleteStmt.setInt(2, etudiantId);
                deleteStmt.executeUpdate(); // Exécute le DELETE (0 ou 1 ligne supprimée)
            }

            if (estPresent) {
                // 2. Si on veut PRÉSENT, on insère la nouvelle ligne
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, seanceId);
                    insertStmt.setInt(2, etudiantId);
                    insertStmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                    insertStmt.executeUpdate();
                }
            }
            // Si estPresent est FALSE, seule la suppression a eu lieu (Absence = Absence de ligne)

            conn.commit(); // Validation
            return true;

        } catch (SQLException e) {
            System.err.println("ERREUR SQL CRITIQUE - EmargementDAO.saveOrDeleteAttendance : " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback(); // Annulation en cas d'erreur
                }
            } catch (SQLException ex) {
                System.err.println("Erreur lors du rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close(); // Fermeture
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
}