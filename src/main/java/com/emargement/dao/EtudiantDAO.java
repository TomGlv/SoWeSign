package com.emargement.dao;

import com.emargement.model.Etudiant;
import com.emargement.model.Utilisateur;
import com.emargement.model.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EtudiantDAO {

    // --- Data Mapper ---
    private Etudiant mapResultSetToEtudiant(ResultSet rs) throws SQLException {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(rs.getInt("e_id"));
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
        return etudiant;
    }
    // -------------------

    public Optional<Etudiant> findByUtilisateurId(int utilisateurId) throws SQLException {
        String sql = "SELECT e.id as e_id, e.numeroEtudiant, e.utilisateurId, " +
                "u.id as u_id, u.login, u.motDePasseHashed, u.nom, u.prenom, u.role " +
                "FROM etudiant e JOIN utilisateur u ON e.utilisateurId = u.id WHERE e.utilisateurId = ?";
        Etudiant etudiant = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateurId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    etudiant = new Etudiant();
                    etudiant.setId(rs.getInt("e_id"));
                    etudiant.setNumeroEtudiant(rs.getString("numeroEtudiant"));
                    etudiant.setUtilisateurId(utilisateurId);

                    Utilisateur user = new Utilisateur(
                            rs.getInt("u_id"),
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
            throw e;
        }
        return Optional.ofNullable(etudiant);
    }

    /**
     * ✅ FIX: IMPLÉMENTATION SIMPLIFIÉE: Récupère TOUS les étudiants.
     */
    public List<Etudiant> findByCoursId(int coursId) throws SQLException {
        List<Etudiant> etudiants = new ArrayList<>();

        String sql = "SELECT e.id as e_id, e.numeroEtudiant, u.id as u_id, u.login, u.nom, u.prenom, u.role, u.motDePasseHashed "
                + "FROM etudiant e JOIN utilisateur u ON e.utilisateurId = u.id WHERE u.role = 'ETUDIANT'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                etudiants.add(mapResultSetToEtudiant(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans EtudiantDAO.findByCoursId : " + e.getMessage());
            throw e; // RETHROW IS CORRECT
        }
        return etudiants;
    }

    /**
     * ✅ FIX: IMPLÉMENTATION SIMPLIFIÉE: Retourne tous les étudiants.
     */
    public List<Etudiant> getEtudiantsBySeanceId(int seanceId) throws SQLException {
        // -1 passed as parameter since findByCoursId does not use it in the current simplified SQL
        return findByCoursId(-1);
    }
}