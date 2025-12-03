package com.emargement.controller;

import com.emargement.App;
import com.emargement.model.Cours;
import com.emargement.model.Etudiant;
import com.emargement.model.Seance;
import com.emargement.dao.CoursDAO;
import com.emargement.dao.SeanceDAO;
import com.emargement.service.EmargementService;
import com.emargement.session.UserSession;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Optional; // L'import manquant est ici !

public class DashboardProfesseurController {

    @FXML private Label welcomeLabel;
    @FXML private ListView<Cours> coursListView;
    @FXML private ListView<Seance> seanceListView;
    @FXML private TableView<Etudiant> etudiantTableView;
    @FXML private TableColumn<Etudiant, String> colNom;
    @FXML private TableColumn<Etudiant, String> colNumero;
    @FXML private Label codeLabel;

    private final CoursDAO coursDAO = new CoursDAO();
    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final EmargementService emargementService = new EmargementService();
    private Seance selectedSeance = null;

    private ObservableList<Etudiant> etudiants = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // Assurez-vous que l'utilisateur est bien connecté
        if (UserSession.getInstance().getUtilisateur() == null) {
            handleLogout(); // Redirige si la session est invalide
            return;
        }

        int professeurId = UserSession.getInstance().getUtilisateur().getId();
        String nom = UserSession.getInstance().getUtilisateur().getPrenom() + " " + UserSession.getInstance().getUtilisateur().getNom();
        welcomeLabel.setText("Bienvenue, Professeur " + nom + " !");

        // Configuration des colonnes de la TableView
        colNom.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getUtilisateur().getNom() + " " + cellData.getValue().getUtilisateur().getPrenom()
                )
        );
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroEtudiant"));
        etudiantTableView.setItems(etudiants);

        // Charger les cours et configurer les écouteurs de sélection
        loadProfessorsCourses(professeurId);

        coursListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldCours, newCours) -> { if (newCours != null) loadSeances(newCours.getId()); });

        seanceListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSeance, newSeance) -> {
                    selectedSeance = newSeance;
                    if (newSeance != null) {
                        // Les messages d'affichage doivent être implémentés pour Seance
                        loadEtudiantsForSeance(newSeance.getId());
                        codeLabel.setText("Code : " + (newSeance.getCodeEmargement() != null ? newSeance.getCodeEmargement() : "-----"));
                    }
                }
        );
    }

    private void loadProfessorsCourses(int professeurId) {
        ObservableList<Cours> coursList = FXCollections.observableArrayList(coursDAO.findByProfesseurId(professeurId));
        coursListView.setItems(coursList);
        // Affichage personnalisé dans la ListView pour Cours
        coursListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Cours cours, boolean empty) {
                super.updateItem(cours, empty);
                setText(empty ? null : cours.getNomCours() + " (" + cours.getCode() + ")");
            }
        });
    }

    private void loadSeances(int coursId) {
        ObservableList<Seance> seances = FXCollections.observableArrayList(seanceDAO.findByCoursId(coursId));
        seanceListView.setItems(seances);
        // Affichage personnalisé dans la ListView pour Seance
        seanceListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Seance seance, boolean empty) {
                super.updateItem(seance, empty);
                // Utilise la date et le nom du cours (s'il est disponible dans l'objet)
                setText(empty ? null : (seance.getDateDebut() != null ? seance.getDateDebut().toLocalTime() : "Date inconnue") +
                        " - " + seance.getNomCours());
            }
        });
    }

    private void loadEtudiantsForSeance(int seanceId) {
        etudiants.clear();
        etudiants.addAll(emargementService.getEtudiantsBySeance(seanceId));
        etudiantTableView.refresh();
    }


    @FXML
    private void handleGenerateCode() {
        if (selectedSeance == null) {
            showAlert("Erreur", "Veuillez sélectionner une séance d'abord.");
            return;
        }

        Optional<String> codeOpt = emargementService.generateUniqueCode(selectedSeance.getId());

        if (codeOpt.isPresent()) {
            codeLabel.setText("Code : " + codeOpt.get());
            showAlert("Succès", "Code généré : " + codeOpt.get() + ". Valide pendant " + EmargementService.VALIDITE_MINUTES + " minutes.");
        } else {
            showAlert("Erreur", "Échec de la génération du code.");
        }
    }

    @FXML
    private void handleSaveAttendance() {
        if (selectedSeance == null) {
            showAlert("Erreur", "Veuillez sélectionner une séance.");
            return;
        }

        // TODO: Implémenter la logique de sauvegarde des présences manuelles basées sur la TableView
        System.out.println("Sauvegarde des présences manuelles pour la séance ID: " + selectedSeance.getId());
        showAlert("Succès", "Présences enregistrées (Simulé).");
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}