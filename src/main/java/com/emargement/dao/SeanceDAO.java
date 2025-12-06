package com.emargement.dao;

import com.emargement.model.Seance;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeanceDAO {

    private Seance mapResultSetToSeance(ResultSet rs) throws SQLException {
        Seance seance = new Seance();
        seance.setId(rs.getInt("id"));
        seance.setCoursId(rs.getInt("coursId"));

        // ⭐️ CRITICAL FIX: Get raw SQL Date/Time fields and combine in Java
        Date date = rs.getDate("dateSeance");
        Time timeDebut = rs.getTime("heureDebut");
        Time timeFin = rs.getTime("heureFin");

        if (date != null && timeDebut != null) {
            seance.setDateDebut(LocalDateTime.of(date.toLocalDate(), timeDebut.toLocalTime()));
        }
        if (date != null && timeFin != null) {
            seance.setDateFin(LocalDateTime.of(date.toLocalDate(), timeFin.toLocalTime()));
        }

        seance.setCodeEmargement(rs.getString("codeEmargement"));

        Timestamp expireTimestamp = rs.getTimestamp("codeEmargementExpire");
        seance.setCodeEmargementExpire(expireTimestamp != null ? expireTimestamp.toLocalDateTime() : null);

        try {
            // Get the course name which is joined in the main query
            seance.setNomCours(rs.getString("nomCours"));
        } catch (SQLException ignore) {
            // nomCours may not always be in the ResultSet
        }
        return seance;
    }

    // ⭐️ FIX: Main method to retrieve all sessions for a course
    public List<Seance> getSeancesByCoursId(int coursId) throws SQLException {
        List<Seance> seanceList = new ArrayList<>();

        // Fetching required columns without CONCAT, relies on Java mapping
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
            System.err.println("Erreur SQL dans SeanceDAO.getSeancesByCoursId : " + e.getMessage());
            throw e; // Re-throw for controller to catch
        }
        return seanceList;
    }


    // The redundant findByCoursId method is merged into getSeancesByCoursId

    public Optional<Seance> findByCodeEmargement(String code) throws SQLException {
        // Keeping your original SQL structure for this method, but simplifying the select list
        String sql = "SELECT s.*, c.nomCours " +
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
            throw e;
        }
        return Optional.empty();
    }


    // Inside SeanceDAO.java

    public void updateCodeEmargement(int seanceId, String code, LocalDateTime expiration) throws SQLException {
        // CRITICAL FIX: Ensure 'codeActif = 1' is set, assuming your DB schema has this column.
        String sql = "UPDATE seance SET codeEmargement = ?, codeEmargementExpire = ?, codeActif = 1 WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            stmt.setTimestamp(2, expiration != null ? Timestamp.valueOf(expiration) : null);
            stmt.setInt(3, seanceId);

            stmt.executeUpdate();
        }
        // If an error still occurs here, it is due to a DB constraint (e.g., column size for codeEmargement).
    }
}