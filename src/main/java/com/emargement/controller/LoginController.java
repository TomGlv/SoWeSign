package com.emargement.controller;

import com.emargement.App;
import com.emargement.dao.EtudiantDAO;
import com.emargement.model.Etudiant;
import com.emargement.model.Role;
import com.emargement.model.Utilisateur;
import com.emargement.service.AuthService;
import com.emargement.util.SessionManager;
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
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();

    @FXML
    private void handleLoginButtonAction() {
        String login = loginField.getText();
        String password = passwordField.getText();

        Optional<Utilisateur> userOpt = authService.authenticate(login, password);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            Object sessionObject = user;

            switch (user.getRole()) {
                case ETUDIANT:
                    Optional<Etudiant> etudiantOpt = etudiantDAO.findByUtilisateurId(user.getId());
                    if (etudiantOpt.isPresent()) {
                        sessionObject = etudiantOpt.get();
                    } else {
                        messageLabel.setText("Erreur : Fiche étudiant introuvable.");
                        messageLabel.setStyle("-fx-text-fill: red;");
                        return;
                    }
                    break;
                case PROFESSEUR:
                    sessionObject = user;
                    break;
                default:
                    break;
            }

            SessionManager.setCurrentUser(sessionObject);

            try {
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
                System.err.println("Erreur de chargement du dashboard pour le rôle " + user.getRole());
                e.printStackTrace();

                if (messageLabel != null) {
                    messageLabel.setText("Erreur interne : Impossible de charger la vue du Tableau de Bord (Erreur FXML).");
                    messageLabel.setStyle("-fx-text-fill: red;");
                }
            }

        } else {
            if (messageLabel != null) {
                messageLabel.setText("Login ou mot de passe incorrect.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }
}