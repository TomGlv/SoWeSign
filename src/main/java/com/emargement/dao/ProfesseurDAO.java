package com.emargement.dao;

import com.emargement.model.Professeur;
import com.emargement.model.Utilisateur;
import com.emargement.model.Role;
import java.sql.*;
import java.util.Optional;

public class ProfesseurDAO {

    public Optional<Professeur> findByUtilisateurId(int utilisateurId) {
        String sql = "SELECT p.*, u.login, u.nom, u.prenom, u.role, u.motDePasseHashed " +
                "FROM professeur p JOIN utilisateur u ON p.utilisateurId = u.id " +
                "WHERE p.utilisateurId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateurId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Professeur professeur = new Professeur();
                    professeur.setId(rs.getInt("id"));
                    professeur.setUtilisateurId(utilisateurId);

                    Utilisateur user = new Utilisateur(
                            utilisateurId,
                            rs.getString("login"),
                            rs.getString("motDePasseHashed"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            Role.valueOf(rs.getString("role"))
                    );
                    professeur.setUtilisateur(user);
                    return Optional.of(professeur);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans ProfesseurDAO.findByUtilisateurId : " + e.getMessage());
        }
        return Optional.empty();
    }
}