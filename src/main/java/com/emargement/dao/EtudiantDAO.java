package com.emargement.dao;

import com.emargement.model.Etudiant;
import com.emargement.model.Utilisateur;
import com.emargement.model.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EtudiantDAO {

    public Optional<Etudiant> findByUtilisateurId(int utilisateurId) {
        String sql = "SELECT * FROM etudiant e JOIN utilisateur u ON e.utilisateurId = u.id WHERE e.utilisateurId = ?";
        Etudiant etudiant = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateurId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    etudiant = new Etudiant();
                    etudiant.setId(rs.getInt("e.id"));
                    etudiant.setNumeroEtudiant(rs.getString("numeroEtudiant"));
                    etudiant.setUtilisateurId(utilisateurId);

                    Utilisateur user = new Utilisateur(
                            rs.getInt("u.id"),
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

    // Simule la récupération de la liste des étudiants (car la table d'inscription manque)
    public List<Etudiant> findByCoursId(int coursId) {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT e.id, e.numeroEtudiant, u.id as u_id, u.login, u.nom, u.prenom, u.role, u.motDePasseHashed FROM etudiant e JOIN utilisateur u ON e.utilisateurId = u.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Etudiant etudiant = new Etudiant();
                etudiant.setId(rs.getInt("e.id"));
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
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans EtudiantDAO.findAll : " + e.getMessage());
        }
        return etudiants;
    }
}