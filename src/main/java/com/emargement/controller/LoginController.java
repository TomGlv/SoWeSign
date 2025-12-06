// src/main/java/com/emargement/controller/LoginController.java

package com.emargement.controller;

import com.emargement.App;
import com.emargement.model.Utilisateur;
import com.emargement.service.AuthService;
import com.emargement.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLoginButtonAction() {

        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        // 0. Validation Professeur (unchanged logic)
        if (login.toLowerCase().endsWith(".prof@edu.ece.fr")) {
            String regex = "^[a-z]+\\.[a-z]+\\.prof@edu\\.ece\\.fr$";
            if (!login.matches(regex)) {
                messageLabel.setText("Format email professeur invalide (prenom.nom.prof@edu.ece.fr)");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
        }

        // 1. Authentification
        Optional<Utilisateur> userOpt = authService.authenticate(login, password);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            UserSession.getInstance().setUtilisateur(user);

            try {
                // 3. REDIRECTION SELON LE RÔLE
                switch (user.getRole()) {
                    case ADMIN:
                        App.setRoot("DashboardAdmin");
                        break;
                    case PROFESSEUR:
                        // FXML file name is DashboardProfesseur.fxml
                        App.setRoot("DashboardProfesseur");
                        break;
                    case ETUDIANT:
                        App.setRoot("DashboardEtudiant");
                        break;
                    default:
                        messageLabel.setText("Rôle utilisateur non reconnu.");
                        messageLabel.setStyle("-fx-text-fill: red;");
                }
            } catch (IOException e) {
                System.err.println("Erreur de chargement du dashboard pour le rôle " + user.getRole());
                e.printStackTrace();
                messageLabel.setText("Erreur interne : Impossible de charger la vue.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }

        } else {
            // 4. Échec de l'authentification (FIXED BY DB UPDATE)
            messageLabel.setText("Login ou mot de passe incorrect.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}