package com.emargement.dao;

import com.emargement.model.Emargement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class EmargementDAO {

    /**
     * Enregistre un émargement pour une séance et un étudiant donnés.
     * @param seanceId L'ID de la séance.
     * @param etudiantId L'ID de l'étudiant.
     * @return true si l'enregistrement est réussi, false sinon (ex: déjà émargé).
     * @throws SQLException En cas d'erreur SQL.
     */
    public boolean createEmargement(int seanceId, int etudiantId) throws SQLException {
        // Vérification de la double présence
        if (isAlreadyEmarged(seanceId, etudiantId)) {
            System.out.println("DEBUG DAO: L'étudiant " + etudiantId + " a déjà émargé pour la séance " + seanceId);
            return false;
        }

        String sql = "INSERT INTO emargement (seanceId, etudiantId, dateHeureEmargement) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seanceId);
            stmt.setInt(2, etudiantId);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'insertion de l'émargement : " + e.getMessage());
            throw e;
        }
    }

    /**
     * Vérifie si un étudiant a déjà émargé pour une séance.
     */
    public boolean isAlreadyEmarged(int seanceId, int etudiantId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM emargement WHERE seanceId = ? AND etudiantId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seanceId);
            stmt.setInt(2, etudiantId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}