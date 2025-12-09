package com.emargement.dao;

import com.emargement.model.Etudiant;
import com.emargement.model.Utilisateur;
import com.emargement.model.Role;
import com.emargement.model.EtudiantPresence;
import com.emargement.dao.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EtudiantDAO {

    /**
     * Récupère un étudiant par l'ID de son utilisateur.
     */
    public Optional<Etudiant> findByUtilisateurId(int utilisateurId) {
        String sql = "SELECT e.id AS etudiantId, e.numeroEtudiant, u.* " +
                "FROM etudiant e " +
                "JOIN utilisateur u ON e.utilisateurId = u.id " +
                "WHERE e.utilisateurId = ?";
        Etudiant etudiant = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateurId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    etudiant = new Etudiant();
                    etudiant.setId(rs.getInt("etudiantId"));
                    etudiant.setNumeroEtudiant(rs.getString("numeroEtudiant"));
                    etudiant.setUtilisateurId(utilisateurId);

                    Utilisateur user = new Utilisateur(
                            rs.getInt("id"),
                            rs.getString("login"),
                            rs.getString("motDePasseHashed"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            Role.valueOf(rs.getString("role"))
                    );
                    etudiant.setUtilisateur(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans EtudiantDAO.findByUtilisateurId : " + e.getMessage());
        }
        return Optional.ofNullable(etudiant);
    }

    /**
     * Récupère la liste des étudiants inscrits à un cours spécifique.
     */
    public List<Etudiant> findByCoursId(int coursId) {
        List<Etudiant> etudiants = new ArrayList<>();

        String sql = "SELECT e.id AS etudiantId, e.numeroEtudiant, u.id as u_id, u.login, u.nom, u.prenom, u.role, u.motDePasseHashed " +
                "FROM etudiant e " +
                "JOIN utilisateur u ON e.utilisateurId = u.id " +
                "INNER JOIN etudiant_cours ec ON e.id = ec.etudiantId " + // ⭐️ Rétablissement de 'etudiant_cours ec' ⭐️
                "WHERE ec.coursId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, coursId);

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Etudiant etudiant = new Etudiant();
                    etudiant.setId(rs.getInt("etudiantId"));
                    etudiant.setNumeroEtudiant(rs.getString("numeroEtudiant"));
                    etudiant.setUtilisateurId(rs.getInt("u_id"));

                    Utilisateur user = new Utilisateur(
                            rs.getInt("u_id"),
                            rs.getString("login"),
                            rs.getString("motDePasseHashed"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            Role.valueOf(rs.getString("role"))
                    );
                    etudiant.setUtilisateur(user);
                    etudiants.add(etudiant);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans EtudiantDAO.findByCoursId : " + e.getMessage());
        }
        return etudiants;
    }

    /**
     * Récupère les étudiants inscrits au cours de la séance, avec leur statut de présence en temps réel.
     */
    public List<EtudiantPresence> findEtudiantsPresenceForSeance(int seanceId, int coursId) {
        List<EtudiantPresence> etudiantsPresence = new ArrayList<>();

        // ⭐️ Rétablissement de 'etudiant_cours ec' + Logique de présence corrigée (em.id IS NOT NULL) ⭐️
        String sql = "SELECT e.id AS etudiantId, e.numeroEtudiant, u.nom, u.prenom, " +
                "em.id IS NOT NULL AS estPresent " +
                "FROM etudiant e " +
                "JOIN utilisateur u ON e.utilisateurId = u.id " +
                "INNER JOIN etudiant_cours ec ON e.id = ec.etudiantId " + // ⭐️ CORRIGÉ ICI ⭐️
                "LEFT JOIN emargement em ON e.id = em.etudiantId AND em.seanceId = ? " +
                "WHERE ec.coursId = ? " +
                "ORDER BY u.nom, u.prenom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seanceId);
            stmt.setInt(2, coursId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int etudiantId = rs.getInt("etudiantId");
                    String nomComplet = rs.getString("prenom") + " " + rs.getString("nom");
                    String numero = rs.getString("numeroEtudiant");

                    boolean estPresent = rs.getBoolean("estPresent");

                    EtudiantPresence ep = new EtudiantPresence(etudiantId, nomComplet, numero, estPresent);

                    etudiantsPresence.add(ep);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans EtudiantDAO.findEtudiantsPresenceForSeance : " + e.getMessage());
        }
        return etudiantsPresence;
    }
}