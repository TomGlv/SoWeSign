package com.emargement.controller;

import com.emargement.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class DashboardAdminController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        // Logique d'initialisation (peut être utilisé pour charger le nom de l'admin)
        // Pour l'instant, on laisse le message par défaut.
    }

    @FXML
    private void handleManageUsers() {
        // Logique pour ouvrir la vue de gestion des utilisateurs
        System.out.println("Ouvrir la gestion des utilisateurs...");
        // App.setRoot("GestionUtilisateurs");
    }

    @FXML
    private void handleViewStats() {
        // Logique pour ouvrir la vue des statistiques
        System.out.println("Ouvrir les statistiques...");
        // App.setRoot("Statistiques");
    }

    @FXML
    private void handleLogout() {
        try {
            // Logique de déconnexion : retour à la vue de login
            App.setRoot("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}