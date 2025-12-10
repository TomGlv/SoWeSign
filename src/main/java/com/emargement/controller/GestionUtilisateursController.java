package com.emargement.controller;

import com.emargement.App;
import com.emargement.dao.UtilisateurDAO;
import com.emargement.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GestionUtilisateursController {

    @FXML private TextField searchField;

    @FXML private TableView<Utilisateur> userTable;
    @FXML private TableColumn<Utilisateur, Integer> colId;
    @FXML private TableColumn<Utilisateur, String> colNom;
    @FXML private TableColumn<Utilisateur, String> colPrenom;
    @FXML private TableColumn<Utilisateur, String> colEmail;
    @FXML private TableColumn<Utilisateur, String> colRole;

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private ObservableList<Utilisateur> userList;

    @FXML
    public void initialize() {
        System.out.println(">>> INITIALISATION GESTION UTILISATEURS <<<");

        // Configuration des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("login"));
        colRole.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getRole().toString())
        );

        // Chargement de la BDD
        List<Utilisateur> users = utilisateurDAO.findAll();
        userList = FXCollections.observableArrayList(users);
        userTable.setItems(userList);

        // Recherche dynamique
        searchField.textProperty().addListener((obs, oldText, newText) ->
                userTable.setItems(
                        userList.filtered(u ->
                                u.getNom().toLowerCase().contains(newText.toLowerCase()) ||
                                        u.getPrenom().toLowerCase().contains(newText.toLowerCase()) ||
                                        u.getLogin().toLowerCase().contains(newText.toLowerCase())
                        )
                )
        );

        // Double clic pour éditer
        userTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleEditUser();
            }
        });
    }

    // -------------------------
    //       BOUTON AJOUTER
    // -------------------------
    @FXML
    private void handleAddUser() {
        System.out.println(">>> AJOUT UTILISATEUR <<<");

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("AddUser.fxml"));
            Stage popup = new Stage();
            popup.setTitle("Ajouter un utilisateur");
            popup.setScene(new Scene(loader.load()));
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(userTable.getScene().getWindow());
            popup.showAndWait();

            // Rafraîchir
            userList.setAll(utilisateurDAO.findAll());

        } catch (Exception e) {
            System.out.println(">>> ERREUR OUVERTURE AddUser.fxml <<<");
            e.printStackTrace();
        }
    }

    // -------------------------
    //       BOUTON MODIFIER
    // -------------------------
    @FXML
    private void handleEditUser() {
        System.out.println(">>> MODIFICATION UTILISATEUR <<<");

        Utilisateur selected = userTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            System.out.println(">>> Aucun utilisateur sélectionné.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("EditUser.fxml"));
            Stage popup = new Stage();
            popup.setTitle("Modifier un utilisateur");
            popup.setScene(new Scene(loader.load()));
            popup.initModality(Modality.APPLICATION_MODAL);

            // Charger le contrôleur lié
            EditUserController controller = loader.getController();
            controller.setUser(selected);

            popup.showAndWait();

            // Rafraîchir après modification
            userList.setAll(utilisateurDAO.findAll());

        } catch (Exception e) {
            System.out.println(">>> ERREUR OUVERTURE EditUser.fxml <<<");
            e.printStackTrace();
        }
    }

    // -------------------------
    //       BOUTON SUPPRIMER
    // -------------------------
    @FXML
    private void handleDeleteUser() {
        Utilisateur selected = userTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            System.out.println(">>> Aucun utilisateur sélectionné");
            return;
        }

        utilisateurDAO.delete(selected.getId());
        System.out.println(">>> Utilisateur supprimé : " + selected.getLogin());

        userList.setAll(utilisateurDAO.findAll());
    }

    // -------------------------
    //       BOUTON RETOUR
    // -------------------------
    @FXML
    private void handleBackToDashboard() {
        System.out.println(">>> RETOUR AU DASHBOARD <<<");

        try {
            App.setRoot("DashboardAdmin");
        } catch (IOException e) {
            System.out.println(">>> ERREUR RETOUR DashboardAdmin <<<");
            e.printStackTrace();
        }
    }
}
