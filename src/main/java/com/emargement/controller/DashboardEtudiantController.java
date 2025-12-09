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

    // Éléments FXML
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

        // ⭐️ VÉRIFICATION ROBUSTE AU DÉMARRAGE ⭐️
        if (currentUser instanceof Etudiant) {
            Etudiant etudiant = (Etudiant) currentUser;
            welcomeLabel.setText("Bienvenue, Étudiant " + etudiant.getUtilisateur().getPrenom() + " " + etudiant.getUtilisateur().getNom());
        } else {
            // Cas où la session est corrompue ou vide. Rediriger pour éviter les erreurs.
            System.err.println("Erreur: SessionManager contient un objet non-Etudiant ou est vide lors de l'initialisation.");
            try {
                // Tenter la déconnexion et le retour à l'écran de login
                handleLogout();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gère l'action du bouton "Émarger".
     */
    @FXML
    private void handleEmargerAction() {
        String code = codeInputField.getText();
        Object currentUser = SessionManager.getCurrentUser();

        // ⭐️ VÉRIFICATION ROBUSTE LORS DE L'ÉMARGEMENT ⭐️
        if (currentUser == null || !(currentUser instanceof Etudiant)) {
            statusLabel.setText("Statut : Erreur de session. Veuillez vous reconnecter.");
            return;
        }

        Etudiant etudiant = (Etudiant) currentUser; // Le casting est maintenant sûr.

        if (code.isEmpty()) {
            statusLabel.setText("Statut : Veuillez entrer le code d'émargement.");
            return;
        }

        // Appel au service pour traiter l'émargement
        String resultat = emargementService.emarger(etudiant, code);

        // Mise à jour de l'étiquette de statut avec le résultat
        statusLabel.setText("Statut : " + resultat);

        // Effacer le champ après l'essai
        codeInputField.clear();
    }

    /**
     * Gère l'action de déconnexion.
     */
    @FXML
    private void handleLogout() throws IOException {
        SessionManager.clearSession();
        // Retour à la page de Login
        App.setRoot("Login");
    }
}