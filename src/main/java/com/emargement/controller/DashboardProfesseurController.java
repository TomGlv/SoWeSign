package com.emargement.controller;

import com.emargement.App;
import com.emargement.dao.CoursDAO;
import com.emargement.dao.SeanceDAO;
import com.emargement.dao.EtudiantDAO;
import com.emargement.dao.EmargementDAO;
import com.emargement.model.Cours;
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
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.time.LocalDateTime;
import javafx.beans.property.*;

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
    // La déclaration @FXML pour colEditPresence a été retirée.
    @FXML private Button generateCodeButton;

    // --- Déclarations DAO et Services ---
    private final CoursDAO coursDAO = new CoursDAO();
    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();
    private final EmargementService emargementService = new EmargementService();

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

    // --- Méthodes de chargement (Inchangées) ---
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

        if (etudiantsData == null) {
            etudiantsData = FXCollections.observableArrayList();
            etudiantTableView.setItems(etudiantsData);
        }

        etudiantsData.clear();
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


    // --- Configuration du Tableau (Logique de Clic sur la Ligne) ---
    private void setupTableView() {

        colNom.setCellValueFactory(new PropertyValueFactory<>("nomComplet"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroEtudiant"));
        colPresence.setCellValueFactory(new PropertyValueFactory<>("statutPresence"));

        // 1. Mise en place des styles de ligne et de cellule
        colPresence.setCellFactory(column -> new TableCell<EtudiantPresence, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll("presence-present", "presence-absent");
                TableRow<EtudiantPresence> row = getTableRow();
                if (row != null) {
                    row.getStyleClass().removeAll("presence-present-row", "presence-absent-row");
                }

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);

                    boolean isPresent = item.equals("Présent");

                    String rowStyle = isPresent ? "presence-present-row" : "presence-absent-row";
                    String cellStyle = isPresent ? "presence-present" : "presence-absent";

                    getStyleClass().add(cellStyle);

                    if (row != null) {
                        row.getStyleClass().add(rowStyle);
                    }
                }
            }
        });

        // 2. LOGIQUE CLÉ : RowFactory pour la gestion du clic
        etudiantTableView.setRowFactory(tv -> {
            TableRow<EtudiantPresence> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {

                    EtudiantPresence etudiant = row.getItem();
                    Boolean nouvelEtat = !etudiant.isEstPresent();

                    if (seanceSelectionnee == null || seanceSelectionnee.getId() <= 0) {
                        System.err.println("ERREUR CRITIQUE: Veuillez sélectionner une séance valide.");
                        return;
                    }

                    // --- APPEL DU DAO ---
                    EmargementDAO emargementDAO = new EmargementDAO();
                    boolean success = emargementDAO.saveOrDeleteAttendance(
                            seanceSelectionnee.getId(),
                            etudiant.getEtudiantId(),
                            nouvelEtat
                    );

                    if (success) {
                        System.out.println("Présence de " + etudiant.getNomComplet() + " mise à jour en DB par clic: " + (nouvelEtat ? "Présent" : "Absent"));

                        etudiant.setEstPresent(nouvelEtat);
                        etudiant.setStatutPresence(nouvelEtat ? "Présent" : "Absent");

                        etudiantTableView.refresh();

                    } else {
                        System.err.println("ÉCHEC CRITIQUE: L'écriture en base de données a échoué.");
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Échec de l'enregistrement en base de données.", ButtonType.OK);
                        alert.showAndWait();
                    }
                }
            });
            return row;
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
        loadEtudiantsPresence(seanceSelectionnee);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Liste de présence actualisée.", ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() throws IOException {
        SessionManager.clearSession();
        App.setRoot("Login");
    }


    /**
     * CLASSE INTERNE : Modèle de données pour la TableView de présence.
     */
    public static class EtudiantPresence {
        private final IntegerProperty etudiantId;
        private final StringProperty nomComplet;
        private final StringProperty numeroEtudiant;
        private final StringProperty statutPresence;
        private final BooleanProperty estPresent;

        public EtudiantPresence(int etudiantId, String nomComplet, String numeroEtudiant, boolean present) {
            this.etudiantId = new SimpleIntegerProperty(etudiantId);
            this.nomComplet = new SimpleStringProperty(nomComplet);
            this.numeroEtudiant = new SimpleStringProperty(numeroEtudiant);
            this.estPresent = new SimpleBooleanProperty(present);
            this.statutPresence = new SimpleStringProperty(present ? "Présent" : "Absent");
        }

        public int getEtudiantId() { return etudiantId.get(); }
        public String getNomComplet() { return nomComplet.get(); }
        public String getNumeroEtudiant() { return numeroEtudiant.get(); }
        public String getStatutPresence() { return statutPresence.get(); }

        public boolean isEstPresent() { return estPresent.get(); }

        public void setEstPresent(boolean present) { this.estPresent.set(present); }
        public void setStatutPresence(String statut) { this.statutPresence.set(statut); }

        public BooleanProperty estPresentProperty() { return estPresent; }
    }
}