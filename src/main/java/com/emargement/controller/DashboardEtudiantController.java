package com.emargement.controller;

import com.emargement.App;
import com.emargement.model.Etudiant;
import com.emargement.service.EmargementService;
import com.emargement.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;

public class DashboardEtudiantController {

    @FXML
    private TextField codeInputField;
    @FXML
    private Label statusLabel;
    @FXML
    private Label welcomeLabel;

    private final EmargementService emargementService = new EmargementService();

    @FXML
    public void initialize() {
        Object currentUser = SessionManager.getCurrentUser();

        if (currentUser instanceof Etudiant) {
            Etudiant etudiant = (Etudiant) currentUser;
            welcomeLabel.setText("Bienvenue, Étudiant " + etudiant.getUtilisateur().getPrenom() + " " + etudiant.getUtilisateur().getNom());
        } else {
            System.err.println("Erreur: SessionManager contient un objet non-Etudiant ou est vide lors de l'initialisation.");
            try {
                handleLogout();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleEmargerAction() {
        String code = codeInputField.getText();
        Object currentUser = SessionManager.getCurrentUser();

        if (currentUser == null || !(currentUser instanceof Etudiant)) {
            statusLabel.setText("Statut : Erreur de session. Veuillez vous reconnecter.");
            return;
        }

        Etudiant etudiant = (Etudiant) currentUser;

        if (code.isEmpty()) {
            statusLabel.setText("Statut : Veuillez entrer le code d'émargement.");
            return;
        }

        String resultat = emargementService.emarger(etudiant, code);

        statusLabel.setText("Statut : " + resultat);

        codeInputField.clear();
    }

    @FXML
    private void handleLogout() throws IOException {
        SessionManager.clearSession();
        App.setRoot("Login");
    }
}