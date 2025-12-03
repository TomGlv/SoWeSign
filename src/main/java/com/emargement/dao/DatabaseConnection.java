// Fichier : src/main/java/com/emargement/dao/DatabaseConnection.java

package com.emargement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Connexion à la base de données locale 'emargement_db'
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/emargement_db";

    // ⭐️ CORRECTION CONFIRMÉE : root sans mot de passe
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Établit et retourne une connexion à la base de données.
     */
    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("DEBUG DB: Tentative de connexion à l'utilisateur : " + USER);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("ERREUR GRAVE DB: Échec de la connexion. Vérifiez si MySQL est démarré.");
            System.err.println("Détails de l'erreur : " + e.getMessage());
            throw e;
        }
    }
}