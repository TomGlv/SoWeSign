package com.emargement.controller;

import com.emargement.dao.UtilisateurDAO;
import com.emargement.model.Role;
import com.emargement.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUserController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private ComboBox<Role> roleCombo;

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @FXML
    public void initialize() {
        roleCombo.getItems().addAll(Role.ADMIN, Role.PROFESSEUR, Role.ETUDIANT);
    }

    @FXML
    private void handleSave() {
        String login = loginField.getText();
        String password = passwordField.getText();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        Role role = roleCombo.getValue();

        if (login.isEmpty() || password.isEmpty() || nom.isEmpty() || prenom.isEmpty() || role == null) {
            System.out.println(">>> Champs manquants.");
            return;
        }

        Utilisateur u = new Utilisateur(0, login, password, nom, prenom, role);
        utilisateurDAO.save(u);
        System.out.println(">>> Utilisateur ajouté");

        // Fermer la fenêtre
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.close();
    }
}
