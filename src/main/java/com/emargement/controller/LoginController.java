package com.emargement.controller;

import com.emargement.App;
import com.emargement.model.Utilisateur;
import com.emargement.service.AuthService;
import com.emargement.session.UserSession; // ⭐️ Import pour la gestion de session
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private final AuthService authService = new AuthService();

    /**
     * Tente d'authentifier l'utilisateur et le redirige selon son rôle.
     */
    @FXML
    private void handleLoginButtonAction() {
        String login = loginField.getText();
        String password = passwordField.getText();

        // 1. Authentification
        Optional<Utilisateur> userOpt = authService.authenticate(login, password);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();

            // 2. ENREGISTREMENT DE LA SESSION
            UserSession.getInstance().setUtilisateur(user);

            try {
                // 3. LOGIQUE DE REDIRECTION BASÉE SUR LE RÔLE
                switch (user.getRole()) {
                    case ADMIN:
                        App.setRoot("DashboardAdmin");
                        break;
                    case PROFESSEUR:
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
                // Gestion de l'erreur si le fichier FXML n'est pas trouvé
                System.err.println("Erreur de chargement du dashboard pour le rôle " + user.getRole());
                e.printStackTrace();
                messageLabel.setText("Erreur interne : Impossible de charger la vue.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }

        } else {
            // 4. Échec de l'authentification
            messageLabel.setText("Login ou mot de passe incorrect.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}