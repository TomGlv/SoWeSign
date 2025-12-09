package com.emargement.controller;

import com.emargement.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label; // N'oubliez pas l'import pour welcomeLabel
import java.io.IOException;

public class DashboardAdminController {

    // IMPORTANT : Ce champ est nécessaire pour le fx:id="welcomeLabel" dans le FXML
    @FXML
    private Label welcomeLabel;

    // Ajoutez la méthode initialize si vous voulez définir le texte du label
    // @FXML
    // public void initialize() {
    //     welcomeLabel.setText("Bienvenue, Admin Root!");
    // }

    @FXML
    private void handleManageUsers() {
        System.out.println(">>> Bouton Gérer les utilisateurs cliqué");

        try {
            App.setRoot("GestionUtilisateurs");
            System.out.println(">>> Navigation vers GestionUtilisateurs.fxml OK");
        } catch (IOException e) { // Utilisez IOException pour le chargement FXML
            System.out.println(">>> ERREUR : Impossible de charger GestionUtilisateurs.fxml <<<");
            e.printStackTrace();
        }
    }

    // NOTE : handleViewStats a été retiré pour correspondre au FXML épuré.

    @FXML
    private void handleLogout() {
        System.out.println(">>> Déconnexion");

        try {
            App.setRoot("Login");
        } catch (IOException e) { // Utilisez IOException pour le chargement FXML
            System.out.println(">>> ERREUR : Impossible de revenir au Login <<<");
            e.printStackTrace();
        }
    }
}