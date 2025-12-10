package com.emargement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Constantes de connexion à la base de données
    private static final String DB_URL = "jdbc:mysql://localhost:3306/emargement_db";
    private static final String URL = DB_URL + "?serverTimezone=Europe/Paris";

    // ⭐️ Configuration utilisateur
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Établit et retourne une connexion à la base de données.
     * @return Une connexion active à la base de données.
     * @throws SQLException Si la connexion échoue (ex: MySQL non démarré, identifiants incorrects).
     */
    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("DEBUG DB: Tentative de connexion à l'utilisateur : " + USER);
            // Établit la connexion JDBC en utilisant l'URL, l'utilisateur et le mot de passe
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Log l'erreur et la propage
            System.err.println("ERREUR GRAVE DB: Échec de la connexion. Vérifiez si MySQL est démarré.");
            System.err.println("Détails de l'erreur : " + e.getMessage());
            throw e;
        }
    }
}