package com.emargement.controller;

import com.emargement.App;
import com.emargement.service.EmargementService;
import com.emargement.session.UserSession;
import com.emargement.model.Utilisateur;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class DashboardEtudiantController {

    @FXML private Label welcomeLabel;
    @FXML private TextField codeInputField;
    @FXML private Label messageLabel;

    private final EmargementService emargementService = new EmargementService();
    private Utilisateur utilisateur;

    @FXML
    public void initialize() {
        utilisateur = UserSession.getInstance().getUtilisateur();
        String nom = utilisateur.getPrenom() + " " + utilisateur.getNom();
        welcomeLabel.setText("Bienvenue, Étudiant " + nom + " !");
    }

    @FXML
    private void handleEmarger() {
        String code = codeInputField.getText().trim().toUpperCase();

        if (code.isEmpty()) {
            messageLabel.setText("Veuillez entrer un code.");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        boolean success = emargementService.emarger(code, utilisateur);

        if (success) {
            messageLabel.setText("Présence enregistrée pour le cours correspondant !");
            messageLabel.setStyle("-fx-text-fill: green;");
            codeInputField.clear();
        } else {
            messageLabel.setText("Échec : Code invalide ou expiré.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().clearSession();
        try {
            App.setRoot("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}