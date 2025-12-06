package com.emargement.dao;

import com.emargement.model.Cours;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursDAO {

    public List<Cours> findByProfesseurId(int professeurId) {
        List<Cours> coursList = new ArrayList<>();
        String sql = "SELECT * FROM cours WHERE professeurId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, professeurId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cours cours = new Cours();
                    cours.setId(rs.getInt("id"));
                    cours.setNomCours(rs.getString("nomCours"));
                    cours.setCode(rs.getString("code"));
                    cours.setDescription(rs.getString("description"));
                    cours.setProfesseurId(rs.getInt("professeurId"));
                    coursList.add(cours);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans CoursDAO.findByProfesseurId : " + e.getMessage());
        }
        return coursList;
    }

    public List<Cours> getCoursByProfId(int id) {
        return List.of();
    }
}