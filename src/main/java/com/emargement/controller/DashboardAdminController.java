package com.emargement.controller;

import com.emargement.App;
import javafx.fxml.FXML;

public class DashboardAdminController {

    @FXML
    private void handleManageUsers() {
        System.out.println(">>> Bouton Gérer les utilisateurs cliqué");

        try {
            App.setRoot("GestionUtilisateurs");
            System.out.println(">>> Navigation vers GestionUtilisateurs.fxml OK");
        } catch (Exception e) {
            System.out.println(">>> ERREUR : Impossible de charger GestionUtilisateurs.fxml <<<");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewStats() {
        System.out.println(">>> Bouton Voir les statistiques cliqué");

        try {
            App.setRoot("DashboardAdmin"); // pas encore de page stats
        } catch (Exception e) {
            System.out.println(">>> ERREUR : Impossible de charger la page Stats <<<");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        System.out.println(">>> Déconnexion");

        try {
            App.setRoot("Login");
        } catch (Exception e) {
            System.out.println(">>> ERREUR : Impossible de revenir au Login <<<");
            e.printStackTrace();
        }
    }
}
