package com.emargement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // 🎯 CORRECTION: MySQL server runs on PORT 3306 (NOT 8080)
    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/emargement_db?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("DEBUG DB: Tentative de connexion MySQL → " + URL);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("❌ Connexion MySQL échouée !");
            System.err.println("Détails : " + e.getMessage());
            throw e;
        }
    }
}
