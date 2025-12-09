package com.emargement.controller;

import com.emargement.dao.UtilisateurDAO;
import com.emargement.model.Role;
import com.emargement.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUserController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private ComboBox<Role> roleCombo;

    private Utilisateur utilisateur;
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public void setUser(Utilisateur u) {
        this.utilisateur = u;

        loginField.setText(u.getLogin());
        nomField.setText(u.getNom());
        prenomField.setText(u.getPrenom());
        roleCombo.getItems().addAll(Role.ADMIN, Role.PROFESSEUR, Role.ETUDIANT);
        roleCombo.setValue(u.getRole());
    }

    @FXML
    private void handleSave() {
        utilisateur.setLogin(loginField.getText());
        utilisateur.setNom(nomField.getText());
        utilisateur.setPrenom(prenomField.getText());
        utilisateur.setRole(roleCombo.getValue());

        // Si un nouveau mot de passe a été entré
        if (!passwordField.getText().isEmpty()) {
            utilisateur.setMotDePasseHashed(passwordField.getText());
        }

        utilisateurDAO.update(utilisateur);

        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.close();
    }
}
