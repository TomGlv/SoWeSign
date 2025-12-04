package com.emargement.controller;

import com.emargement.App;
import com.emargement.model.Cours;
import com.emargement.model.Etudiant;
import com.emargement.model.Seance;
import com.emargement.model.Utilisateur;
import com.emargement.dao.CoursDAO;
import com.emargement.dao.EtudiantDAO; // Import manquant ajouté
import com.emargement.dao.SeanceDAO;
import com.emargement.service.EmargementService;
import com.emargement.session.UserSession;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty; // Import nécessaire
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private final EtudiantDAO etudiantDAO = new EtudiantDAO(); // Instanciation du DAO Etudiant
    private final EmargementService emargementService = new EmargementService();

    private Cours selectedCours = null; // Ajout pour suivre le cours sélectionné
    private Seance selectedSeance = null;

    // Méthode appelée automatiquement après le chargement du FXML
    @FXML
    public void initialize() {
        Utilisateur professeur = UserSession.getInstance().getUtilisateur();
        welcomeLabel.setText("Bienvenue, Professeur " + professeur.getPrenom() + " " + professeur.getNom() + " !");

        // Configuration des colonnes
        // ⭐️ CORRECTION/AJUSTEMENT : Utilisation de getUtilisateur().getNom() et getPrenom() pour afficher le nom complet
        colNom.setCellValueFactory(cellData -> {
            Utilisateur user = cellData.getValue().getUtilisateur();
            if (user != null) {
                return new SimpleStringProperty(user.getNom() + " " + user.getPrenom());
            }
            return new SimpleStringProperty("");
        });
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroEtudiant"));

        // 1. Gérer la SELECTION DANS LA LISTE DES COURS
        coursListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedCours = newValue;
            if (newValue != null) {
                loadSeances(newValue.getId());
                // Réinitialiser les vues de la séance précédente
                seanceListView.getSelectionModel().clearSelection(); // Efface la sélection de séance
                etudiantTableView.getItems().clear();
                codeLabel.setText("Code : N/A");
                selectedSeance = null;
            } else {
                seanceListView.getItems().clear();
            }
        });

        // 2. Gérer la SELECTION DANS LA LISTE DES SÉANCES
        seanceListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedSeance = newValue;
            if (newValue != null) {
                // Afficher le code existant de la séance ou N/A
                String code = (newValue.getCodeEmargement() != null && newValue.getCodeEmargementExpire() != null && newValue.getCodeEmargementExpire().isAfter(LocalDateTime.now()))
                        ? newValue.getCodeEmargement()
                        : "N/A";
                codeLabel.setText("Code : " + code);

                // Charger les étudiants pour la séance sélectionnée
                loadEtudiantsBySeance(newValue.getCoursId()); // On charge les étudiants par l'ID du cours de la séance
            } else {
                // Si la sélection est effacée
                etudiantTableView.getItems().clear();
                codeLabel.setText("Code : N/A");
            }
        });

        // 3. Charger les cours de l'enseignant au démarrage
        loadCours(professeur.getId());
    }

    /**
     * Charge les cours de l'enseignant.
     */
    private void loadCours(int professeurId) {
        List<Cours> coursList = coursDAO.findByProfesseurId(professeurId);
        ObservableList<Cours> cours = FXCollections.observableArrayList(coursList);
        coursListView.setItems(cours);
    }

    /**
     * Charge les séances associées au cours sélectionné.
     */
    private void loadSeances(int coursId) {
        ObservableList<Seance> seances = FXCollections.observableArrayList(seanceDAO.findByCoursId(coursId));
        seanceListView.setItems(seances);
    }

    /**
     * Charge la liste des étudiants pour le cours associé à la séance.
     */
    private void loadEtudiantsBySeance(int coursId) {
        // ⚠️ NOTE : Votre EtudiantDAO.findByCoursId actuel semble retourner TOUS les étudiants.
        // Cela devrait être corrigé dans EtudiantDAO pour joindre la table d'inscription (si elle existe).
        // En attendant, on utilise la méthode disponible.
        List<Etudiant> etudiantsList = etudiantDAO.findByCoursId(coursId);

        // TODO: Une fois la table 'Emargement' remplie, ajuster ici pour afficher l'état de présence.

        ObservableList<Etudiant> etudiants = FXCollections.observableArrayList(etudiantsList);
        etudiantTableView.setItems(etudiants);
    }

    // --- Gestion des Actions de Boutons ---

    @FXML
    private void handleGenerateCode() {
        if (selectedSeance == null) {
            showAlert("Erreur", "Veuillez sélectionner une séance d'abord.");
            return;
        }

        Optional<String> codeOpt = emargementService.generateUniqueCode(selectedSeance.getId());

        if (codeOpt.isPresent()) {
            // Mettre à jour l'objet seance local et le Label
            selectedSeance.setCodeEmargement(codeOpt.get());
            selectedSeance.setCodeEmargementExpire(LocalDateTime.now().plusMinutes(EmargementService.VALIDITE_MINUTES));
            codeLabel.setText("Code : " + codeOpt.get());

            // Recharger les séances pour mettre à jour la liste visuellement
            if (selectedCours != null) {
                loadSeances(selectedCours.getId());
                // Resélectionner la séance pour la garder visible
                seanceListView.getSelectionModel().select(selectedSeance);
            }

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