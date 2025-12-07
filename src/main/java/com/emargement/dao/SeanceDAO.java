package com.emargement.dao;

import com.emargement.model.Seance;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeanceDAO {

    public List<Seance> findByCoursId(int coursId) {
        List<Seance> seanceList = new ArrayList<>();
        String sql = "SELECT s.id, s.coursId, s.codeEmargement, s.codeEmargementExpire, c.nomCours, " +
                "CONCAT(s.dateSeance, ' ', s.heureDebut) AS dateDebut, " +
                "CONCAT(s.dateSeance, ' ', s.heureFin) AS dateFin " +
                "FROM seance s JOIN cours c ON s.coursId = c.id WHERE s.coursId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, coursId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    seanceList.add(mapResultSetToSeance(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans SeanceDAO.findByCoursId : " + e.getMessage());
        }
        return seanceList;
    }

    public Optional<Seance> findByCodeEmargement(String code) {
        String sql = "SELECT s.id, s.coursId, s.codeEmargement, s.codeEmargementExpire, c.nomCours, " +
                "CONCAT(s.dateSeance, ' ', s.heureDebut) AS dateDebut, " +
                "CONCAT(s.dateSeance, ' ', s.heureFin) AS dateFin " +
                "FROM seance s JOIN cours c ON s.coursId = c.id WHERE s.codeEmargement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSeance(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans SeanceDAO.findByCodeEmargement : " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Récupère la prochaine séance (aujourd'hui ou future) pour un professeur donné.
     */
    public Optional<Seance> findNextByProfesseurId(int professeurId) {
        String sql =
                "SELECT s.id, s.coursId, s.codeEmargement, s.codeEmargementExpire, c.nomCours, " +
                        "CONCAT(s.dateSeance, ' ', s.heureDebut) AS dateDebut, " +
                        "CONCAT(s.dateSeance, ' ', s.heureFin) AS dateFin " +
                        "FROM seance s " +
                        "JOIN cours c ON s.coursId = c.id " +
                        "WHERE c.professeurId = ? " +
                        "AND CONCAT(s.dateSeance, ' ', s.heureDebut) >= NOW() " +
                        "ORDER BY dateDebut ASC " +
                        "LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, professeurId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSeance(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans SeanceDAO.findNextByProfesseurId : " + e.getMessage());
        }
        return Optional.empty();
    }

    public void updateCodeEmargement(int seanceId, String code, LocalDateTime expiration) throws SQLException {
        String sql = "UPDATE seance SET codeEmargement = ?, codeEmargementExpire = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            stmt.setTimestamp(2, expiration != null ? Timestamp.valueOf(expiration) : null);
            stmt.setInt(3, seanceId);
            stmt.executeUpdate();
        }
    }

    private Seance mapResultSetToSeance(ResultSet rs) throws SQLException {
        Seance seance = new Seance();
        seance.setId(rs.getInt("id"));
        seance.setCoursId(rs.getInt("coursId"));

        Timestamp debutTimestamp = rs.getTimestamp("dateDebut");
        if (debutTimestamp != null) {
            seance.setDateDebut(debutTimestamp.toLocalDateTime());
        }

        Timestamp finTimestamp = rs.getTimestamp("dateFin");
        if (finTimestamp != null) {
            seance.setDateFin(finTimestamp.toLocalDateTime());
        }

        seance.setCodeEmargement(rs.getString("codeEmargement"));

        Timestamp expireTimestamp = rs.getTimestamp("codeEmargementExpire");
        seance.setCodeEmargementExpire(expireTimestamp != null ? expireTimestamp.toLocalDateTime() : null);

        try {
            seance.setNomCours(rs.getString("nomCours"));
        } catch (SQLException ignore) {
            // nomCours peut ne pas être présent selon la requête
        }
        return seance;
    }
}
