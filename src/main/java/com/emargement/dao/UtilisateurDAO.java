package com.emargement.dao;

import com.emargement.model.Utilisateur;
import com.emargement.model.Role; // Import de la nouvelle Enum Role
import java.sql.*;
import java.util.Optional;

public class UtilisateurDAO {

    public Optional<Utilisateur> findByLogin(String login) {
        // Sélectionne toutes les colonnes nécessaires
        String sql = "SELECT id, login, motDePasseHashed, nom, prenom, role FROM utilisateur WHERE login = ?";
        Utilisateur user = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new Utilisateur();
                    user.setId(rs.getInt("id"));
                    user.setLogin(rs.getString("login"));
                    user.setMotDePasseHashed(rs.getString("motDePasseHashed"));
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));

                    // ⭐️ CORRECTION : Conversion de String à Role
                    user.setRole(Role.valueOf(rs.getString("role")));
                }
            }

        } catch (SQLException e) {
            // Affiche l'erreur SQL pour débogage
            System.err.println("Erreur SQL dans UtilisateurDAO.findByLogin : " + e.getMessage());
            e.printStackTrace();
            // L'erreur de connexion DB (Access denied) sera affichée ici ou dans DatabaseConnection
            user = null;
        }

        // Retourne un Optional
        return Optional.ofNullable(user);
    }
}