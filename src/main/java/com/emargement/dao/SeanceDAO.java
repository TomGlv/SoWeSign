package com.emargement.dao;

import com.emargement.model.Seance;
import com.emargement.dao.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public class SeanceDAO {

    private static final String SEANCE_FIELDS =
            "id, coursId, salle, codeEmargement, codeEmargementExpire, " +
                    "CONCAT(dateSeance, ' ', heureDebut) AS dateHeureDebut, " +
                    "CONCAT(dateSeance, ' ', heureFin) AS dateHeureFin ";

    public List<Seance> findByCoursId(int coursId) {
        List<Seance> seances = new ArrayList<>();
        String sql = "SELECT " + SEANCE_FIELDS +
                "FROM seance WHERE coursId = ? ORDER BY dateSeance, heureDebut ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, coursId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Seance seance = new Seance();
                    seance.setId(rs.getInt("id"));
                    seance.setCoursId(rs.getInt("coursId"));

                    seance.setDateDebut(rs.getTimestamp("dateHeureDebut").toLocalDateTime());
                    seance.setDateFin(rs.getTimestamp("dateHeureFin").toLocalDateTime());

                    seance.setSalle(rs.getString("salle"));
                    seance.setCodeEmargement(rs.getString("codeEmargement"));

                    Timestamp tsExpire = rs.getTimestamp("codeEmargementExpire");
                    if (tsExpire != null) {
                        seance.setCodeEmargementExpire(tsExpire.toLocalDateTime());
                    }
                    seances.add(seance);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans SeanceDAO.findByCoursId : " + e.getMessage());
        }
        return seances;
    }

    public Optional<Seance> findById(int seanceId) {
        String sql = "SELECT " + SEANCE_FIELDS + "FROM seance WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seanceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Seance seance = new Seance();
                    seance.setId(rs.getInt("id"));
                    seance.setCoursId(rs.getInt("coursId"));

                    seance.setDateDebut(rs.getTimestamp("dateHeureDebut").toLocalDateTime());
                    seance.setDateFin(rs.getTimestamp("dateHeureFin").toLocalDateTime());

                    seance.setSalle(rs.getString("salle"));
                    seance.setCodeEmargement(rs.getString("codeEmargement"));

                    Timestamp tsExpire = rs.getTimestamp("codeEmargementExpire");
                    if (tsExpire != null) {
                        seance.setCodeEmargementExpire(tsExpire.toLocalDateTime());
                    }

                    return Optional.of(seance);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans SeanceDAO.findById : " + e.getMessage());
        }
        return Optional.empty();
    }

    public void updateCodeEmargement(int seanceId, String code, LocalDateTime expiration) throws SQLException {
        String sql = "UPDATE seance SET codeEmargement = ?, codeEmargementExpire = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            stmt.setTimestamp(2, Timestamp.valueOf(expiration));
            stmt.setInt(3, seanceId);

            stmt.executeUpdate();
        }
    }

    public Optional<Seance> findByCodeEmargement(String code) {
        String sql = "SELECT " + SEANCE_FIELDS + "FROM seance WHERE codeEmargement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Seance seance = new Seance();
                    seance.setId(rs.getInt("id"));
                    seance.setCoursId(rs.getInt("coursId"));

                    seance.setDateDebut(rs.getTimestamp("dateHeureDebut").toLocalDateTime());
                    seance.setDateFin(rs.getTimestamp("dateHeureFin").toLocalDateTime());

                    seance.setSalle(rs.getString("salle"));
                    seance.setCodeEmargement(rs.getString("codeEmargement"));

                    Timestamp tsExpire = rs.getTimestamp("codeEmargementExpire");
                    if (tsExpire != null) {
                        seance.setCodeEmargementExpire(tsExpire.toLocalDateTime());
                    }
                    return Optional.of(seance);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans SeanceDAO.findByCodeEmargement : " + e.getMessage());
        }
        return Optional.empty();
    }
}