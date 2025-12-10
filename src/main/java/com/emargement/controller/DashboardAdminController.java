package com.emargement.controller;

import com.emargement.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;

public class DashboardAdminController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private void handleManageUsers() {
        System.out.println(">>> Bouton Gérer les utilisateurs cliqué");

        try {
            App.setRoot("GestionUtilisateurs");
            System.out.println(">>> Navigation vers GestionUtilisateurs.fxml OK");
        } catch (IOException e) {
            System.out.println(">>> ERREUR : Impossible de charger GestionUtilisateurs.fxml <<<");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        System.out.println(">>> Déconnexion");

        try {
            App.setRoot("Login");
        } catch (IOException e) {
            System.out.println(">>> ERREUR : Impossible de revenir au Login <<<");
            e.printStackTrace();
        }
    }
}