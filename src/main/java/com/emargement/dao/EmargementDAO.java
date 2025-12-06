package com.emargement.dao;

import com.emargement.model.Emargement;

import java.sql.*;
import java.time.LocalDateTime;

public class EmargementDAO {

    /**
     * Enregistre un émargement automatique via code ou QR.
     */
    public boolean createEmargement(int seanceId, int etudiantId) throws SQLException {

        // Vérifier la présence déjà existante
        if (isAlreadyEmarged(seanceId, etudiantId)) {
            return false;
        }

        String sql = "INSERT INTO emargement (seanceId, etudiantId, dateHeureEmargement, present) VALUES (?, ?, ?, 1)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seanceId);
            stmt.setInt(2, etudiantId);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Vérifie si l'étudiant a déjà émargé.
     */
    public boolean isAlreadyEmarged(int seanceId, int etudiantId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM emargement WHERE seanceId = ? AND etudiantId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seanceId);
            stmt.setInt(2, etudiantId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * ⭐ PROF MANUAL VALIDATION
     * Insert OR update presence, using MySQL UPSERT.
     */
    public void upsertPresence(int seanceId, int etudiantId, boolean present) throws SQLException {

        String sql = """
        INSERT INTO emargement (seanceId, etudiantId, dateHeureEmargement, present)
        VALUES (?, ?, NOW(), ?)
        ON DUPLICATE KEY UPDATE 
            present = VALUES(present),
            dateHeureEmargement = NOW();
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seanceId);
            stmt.setInt(2, etudiantId);
            stmt.setBoolean(3, present);

            stmt.executeUpdate();
        }
    }
}
