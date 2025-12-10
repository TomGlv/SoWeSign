package com.emargement.dao;

import com.emargement.model.Role;
import com.emargement.model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilisateurDAO {

    // -----------------------------
    //  FIND BY LOGIN
    // -----------------------------
    public Optional<Utilisateur> findByLogin(String login) {
        String sql = "SELECT * FROM utilisateur WHERE login = ?";
        Utilisateur user = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = mapToUtilisateur(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans UtilisateurDAO.findByLogin : " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.ofNullable(user);
    }


    // -----------------------------
    //  AJOUTER UN UTILISATEUR
    // -----------------------------
    public void save(Utilisateur user) {
        String sql = "INSERT INTO utilisateur (login, motDePasseHashed, nom, prenom, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getMotDePasseHashed());
            stmt.setString(3, user.getNom());
            stmt.setString(4, user.getPrenom());
            stmt.setString(5, user.getRole().toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans UtilisateurDAO.save : " + e.getMessage());
        }
    }


    // -----------------------------
    //  LISTE DES UTILISATEURS
    // -----------------------------
    public List<Utilisateur> findAll() {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                liste.add(mapToUtilisateur(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans UtilisateurDAO.findAll : " + e.getMessage());
        }

        return liste;
    }


    // -----------------------------
    //  SUPPRIMER UN UTILISATEUR
    // -----------------------------
    public void delete(int id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans UtilisateurDAO.delete : " + e.getMessage());
        }
    }


    // -----------------------------
    //  MODIFIER UN UTILISATEUR
    // -----------------------------
    public void update(Utilisateur u) {
        String sql = "UPDATE utilisateur SET login=?, motDePasseHashed=?, nom=?, prenom=?, role=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getLogin());
            stmt.setString(2, u.getMotDePasseHashed());
            stmt.setString(3, u.getNom());
            stmt.setString(4, u.getPrenom());
            stmt.setString(5, u.getRole().toString());
            stmt.setInt(6, u.getId());

            stmt.executeUpdate();
            System.out.println(">>> UTILISATEUR MODIFIÉ : " + u.getLogin());

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans UtilisateurDAO.update : " + e.getMessage());
        }
    }


    // -----------------------------
    //  MAPPING RESULTSET → OBJET JAVA
    // -----------------------------
    private Utilisateur mapToUtilisateur(ResultSet rs) throws SQLException {
        return new Utilisateur(
                rs.getInt("id"),
                rs.getString("login"),
                rs.getString("motDePasseHashed"),
                rs.getString("nom"),
                rs.getString("prenom"),
                Role.valueOf(rs.getString("role"))
        );
    }
}
