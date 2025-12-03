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
        String sql = "SELECT s.*, c.nomCours FROM seance s JOIN cours c ON s.coursId = c.id WHERE s.coursId = ?";

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
        String sql = "SELECT s.*, c.nomCours FROM seance s JOIN cours c ON s.coursId = c.id WHERE s.codeEmargement = ?";

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
        seance.setDateDebut(rs.getTimestamp("dateDebut").toLocalDateTime());
        seance.setDateFin(rs.getTimestamp("dateFin").toLocalDateTime());
        seance.setCodeEmargement(rs.getString("codeEmargement"));

        Timestamp expireTimestamp = rs.getTimestamp("codeEmargementExpire");
        seance.setCodeEmargementExpire(expireTimestamp != null ? expireTimestamp.toLocalDateTime() : null);

        try {
            seance.setNomCours(rs.getString("nomCours"));
        } catch (SQLException ignored) {}

        return seance;
    }
}