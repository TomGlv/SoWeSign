package com.emargement.controller;

import com.emargement.App;
import com.emargement.dao.CoursDAO;
import com.emargement.dao.SeanceDAO;
import com.emargement.dao.EtudiantDAO;
import com.emargement.dao.EmargementDAO;
import com.emargement.model.Cours;
import com.emargement.model.EtudiantPresence;
import com.emargement.model.Utilisateur;
import com.emargement.model.Seance;
import com.emargement.service.EmargementService;
import com.emargement.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.time.LocalDateTime;

public class DashboardProfesseurController implements Initializable {

    // --- Déclarations FXML ---
    @FXML private Label welcomeLabel;
    @FXML private Label codeLabel;
    @FXML private Label seanceDetailsLabel;
    @FXML private ListView<Cours> coursListView;
    @FXML private ListView<Seance> seanceListView;
    @FXML private TableView<EtudiantPresence> etudiantTableView;
    @FXML private TableColumn<EtudiantPresence, String> colNom;
    @FXML private TableColumn<EtudiantPresence, String> colNumero;
    @FXML private TableColumn<EtudiantPresence, String> colPresence;
    @FXML private TableColumn<EtudiantPresence, Boolean> colEditPresence;
    @FXML private Button generateCodeButton;

    // --- Déclarations DAO et Services ---
    private final CoursDAO coursDAO = new CoursDAO();
    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();
    private final EmargementService emargementService = new EmargementService();
    private final EmargementDAO emargementDAO = new EmargementDAO();

    // --- Variables d'État ---
    private Utilisateur utilisateurConnecte;
    private ObservableList<EtudiantPresence> etudiantsData;
    private Cours coursSelectionne = null;
    private Seance seanceSelectionnee = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Object currentUser = SessionManager.getCurrentUser();

        if (currentUser instanceof Utilisateur) {
            this.utilisateurConnecte = (Utilisateur) currentUser;

            welcomeLabel.setText("Bienvenue, Professeur " + this.utilisateurConnecte.getPrenom() + " " + this.utilisateurConnecte.getNom());

            setupTableView();
            loadCours();
            setupListeners();
            if (generateCodeButton != null) {
                generateCodeButton.setDisable(true);
            }
        } else {
            System.err.println("Erreur de session: L'utilisateur connecté n'est pas un Utilisateur valide.");
            try {
                handleLogout();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // --- Méthodes de chargement ---
    private void loadCours() {
        if (this.utilisateurConnecte != null) {
            List<Cours> coursList = coursDAO.findByProfesseurId(this.utilisateurConnecte.getId());
            coursListView.setItems(FXCollections.observableArrayList(coursList));
        }
    }

    private void loadSeances(Cours cours) {
        List<Seance> seances = seanceDAO.findByCoursId(cours.getId());
        seanceListView.setItems(FXCollections.observableArrayList(seances));
    }

    private void loadEtudiantsPresence(Seance seance) {
        List<EtudiantPresence> presenceList = etudiantDAO.findEtudiantsPresenceForSeance(seance.getId(), coursSelectionne.getId());
        etudiantsData.setAll(presenceList);
    }

    private void setupListeners() {
        coursListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            coursSelectionne = newVal;
            if (newVal != null) {
                loadSeances(newVal);
                seanceListView.getSelectionModel().clearSelection();
                etudiantTableView.getItems().clear();
                seanceSelectionnee = null;
                seanceDetailsLabel.setText("Sélectionnez une séance pour le cours : " + newVal.getNomCours());
                codeLabel.setText("Code actuel : --");
                if (generateCodeButton != null) {
                    generateCodeButton.setDisable(true);
                }
            }
        });

        seanceListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            seanceSelectionnee = newVal;
            if (newVal != null) {
                loadEtudiantsPresence(newVal);
                seanceDetailsLabel.setText("Séance sélectionnée : " + coursSelectionne.getNomCours() +
                        "\nDébut: " + newVal.getDateDebut().toLocalTime().toString() + ", Salle: " + newVal.getSalle());
                updateCodeDisplay(newVal);
                if (generateCodeButton != null) {
                    generateCodeButton.setDisable(false);
                }
            }
        });

        seanceListView.setCellFactory(lv -> new ListCell<Seance>() {
            @Override
            protected void updateItem(Seance seance, boolean empty) {
                super.updateItem(seance, empty);
                if (empty || seance == null) {
                    setText(null);
                } else {
                    setText(seance.getDateDebut().toLocalDate() + " de " +
                            seance.getDateDebut().toLocalTime() + " à " + seance.getDateFin().toLocalTime());
                }
            }
        });
    }

    // --- Configuration du Tableau ---
    private void setupTableView() {

        colNom.setCellValueFactory(new PropertyValueFactory<>("nomComplet"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroEtudiant"));

        // 1. Colonne Statut Texte
        colPresence.setCellValueFactory(new PropertyValueFactory<>("statutPresence"));
        colPresence.setCellFactory(column -> new TableCell<EtudiantPresence, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setStyle(item.equals("Présent") ? "-fx-text-fill: #2ecc71;" : "-fx-text-fill: #e74c3c;");
                }
            }
        });

        // 2. Colonne d'Édition CheckBox (LOGIQUE CORRIGÉE ET SIMPLIFIÉE)
        etudiantTableView.setEditable(true);
        colEditPresence.setEditable(true);

        colEditPresence.setCellValueFactory(new PropertyValueFactory<>("estPresent"));

        // Utilisation de la CellFactory standard qui se lie à la PropertyValueFactory
        colEditPresence.setCellFactory(CheckBoxTableCell.forTableColumn(
                // Ceci est la meilleure façon d'assurer la liaison CheckBox -> Modèle
                param -> etudiantTableView.getItems().get(param).estPresentProperty()
        ));

        // Intercepter l'événement APRÈS que la CheckBox a mis à jour le modèle
        colEditPresence.setOnEditCommit(event -> {

            EtudiantPresence etudiant = event.getRowValue();
            Boolean nouvelEtat = event.getNewValue();

            if (seanceSelectionnee == null || seanceSelectionnee.getId() <= 0) {
                System.err.println("ERREUR CRITIQUE: Veuillez sélectionner une séance valide.");

                // Annuler le changement dans le modèle et rafraîchir la vue
                etudiant.setEstPresent(!nouvelEtat);
                etudiantTableView.refresh();
                return;
            }

            // APPEL DU DAO (Sauvegarde/Suppression)
            boolean success = emargementDAO.saveOrDeleteAttendance(
                    seanceSelectionnee.getId(),
                    etudiant.getEtudiantId(),
                    nouvelEtat
            );

            if (success) {
                System.out.println("Présence de " + etudiant.getNomComplet() + " mise à jour: " + (nouvelEtat ? "Présent" : "Absent"));
                // Le modèle est déjà mis à jour par JavaFX. On rafraîchit la ligne pour la couleur et le texte
                etudiantTableView.refresh();

            } else {
                // Échec du DAO : on annule la modification pour que la CheckBox revienne en arrière
                System.err.println("Échec de l'écriture en base de données pour " + etudiant.getNomComplet() + ". Changement annulé.");

                // ⭐️ LA CLÉ : Rétablir la valeur opposée pour annuler l'édition ⭐️
                etudiant.setEstPresent(!nouvelEtat);
                etudiantTableView.refresh();
            }
        });


        etudiantsData = FXCollections.observableArrayList();
        etudiantTableView.setItems(etudiantsData);
    }

    // --- Méthodes FXML (Handlers) ---

    private void updateCodeDisplay(Seance seance) {
        if (seance.getCodeEmargement() != null && seance.getCodeEmargementExpire() != null && seance.getCodeEmargementExpire().isAfter(LocalDateTime.now())) {
            codeLabel.setText("Code actuel : " + seance.getCodeEmargement());
            codeLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
        } else {
            codeLabel.setText("Code actuel : Expired ou Non Généré");
            codeLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    @FXML
    private void handleGenerateCode() {
        if (seanceSelectionnee != null) {
            Optional<String> newCodeOpt = emargementService.generateUniqueCode(seanceSelectionnee.getId());

            if (newCodeOpt.isPresent()) {
                seanceSelectionnee.setCodeEmargement(newCodeOpt.get());
                seanceSelectionnee.setCodeEmargementExpire(LocalDateTime.now().plusMinutes(EmargementService.VALIDITE_MINUTES));

                updateCodeDisplay(seanceSelectionnee);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Nouveau code généré : " + newCodeOpt.get(), ButtonType.OK);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la génération du code.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleActualiser() {
        if (seanceSelectionnee != null) {
            Optional<Seance> updatedSeanceOpt = seanceDAO.findById(seanceSelectionnee.getId());
            if (updatedSeanceOpt.isPresent()) {
                seanceSelectionnee = updatedSeanceOpt.get();
                updateCodeDisplay(seanceSelectionnee);
            }

            loadEtudiantsPresence(seanceSelectionnee);
        }
    }

    @FXML
    private void handleSaveAttendance() {
        if (seanceSelectionnee == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une séance d'abord.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Le bouton sert d'actualisation car les modifications sont instantanées.
        loadEtudiantsPresence(seanceSelectionnee);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Les modifications sont enregistrées en temps réel. Liste actualisée.", ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() throws IOException {
        SessionManager.clearSession();
        App.setRoot("Login");
    }
}