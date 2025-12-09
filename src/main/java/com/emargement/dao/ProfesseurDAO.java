package com.emargement.dao;

import com.emargement.model.Professeur;
import com.emargement.model.Utilisateur;
import com.emargement.model.Role;
// ⭐️ CORRECTION 1 : L'importation pointe maintenant vers le paquetage 'dao'
import com.emargement.dao.DatabaseConnection;
import java.sql.*;
import java.util.Optional;

public class ProfesseurDAO {

    /**
     * Recherche un objet Professeur à partir de l'ID de l'utilisateur associé.
     * @param utilisateurId L'ID de l'utilisateur (issu de la table 'utilisateur').
     * @return Un Optional contenant l'objet Professeur complet s'il est trouvé.
     */
    public Optional<Professeur> findByUtilisateurId(int utilisateurId) {

        // ⭐️ CORRECTION 2 : Changement de 'p.utilisateurId' à 'p.utilisateur_id' ⭐️
        String sql = "SELECT p.id as professeur_id, p.utilisateur_id, u.login, u.nom, u.prenom, u.role, u.motDePasseHashed " +
                "FROM professeur p JOIN utilisateur u ON p.utilisateur_id = u.id " +
                "WHERE p.utilisateur_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateurId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    // 1. Création de l'entité Professeur
                    Professeur professeur = new Professeur();

                    // ⭐️ CORRECTION 3 : Utiliser l'alias 'professeur_id' si vous l'avez ajouté, sinon 'id'.
                    // Je pars du principe que vous utilisez l'alias pour éviter le conflit avec u.id.
                    professeur.setId(rs.getInt("professeur_id"));

                    professeur.setUtilisateurId(rs.getInt("utilisateur_id"));

                    // 2. Création de l'entité Utilisateur intégrée (u.id est implicitement égal à utilisateurId)
                    Utilisateur user = new Utilisateur(
                            utilisateurId,
                            rs.getString("login"),
                            rs.getString("motDePasseHashed"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            Role.valueOf(rs.getString("role"))
                    );

                    // 3. Liaison de l'utilisateur au professeur
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