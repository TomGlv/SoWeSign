package com.emargement.controller;

import com.emargement.App;
import com.emargement.dao.EtudiantDAO;
import com.emargement.dao.SeanceDAO;
import com.emargement.model.Etudiant;
import com.emargement.model.Seance;
import com.emargement.model.Utilisateur;
import com.emargement.service.EmargementService;
import com.emargement.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DashboardProfesseurController {

    // TOP BAR
    @FXML private Label topDateLabel;
    @FXML private Label topSubjectLabel;

    // SIDEBAR
    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;

    // CENTER
    @FXML private ListView<Etudiant> studentListView;

    // RIGHT
    @FXML private Button codeModeButton;
    @FXML private Button qrModeButton;
    @FXML private Label codeValueLabel;
    @FXML private ImageView qrImageView;
    @FXML private Button validateButton;

    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();
    private final EmargementService emargementService = new EmargementService();

    private Seance currentSeance;

    private enum PresenceStatus { NEUTRAL, ABSENT, PRESENT }
    private final Map<Integer, PresenceStatus> presenceMap = new HashMap<>();

    @FXML
    public void initialize() throws SQLException {
        Utilisateur prof = UserSession.getInstance().getUtilisateur();
        if (prof == null) {
            try { App.setRoot("Login"); } catch (IOException ignored) {}
            return;
        }

        welcomeLabel.setText("Bonjour, " + prof.getPrenom() + " " + prof.getNom());

        Optional<Seance> nextSeanceOpt = seanceDAO.findNextByProfesseurId(prof.getId());
        if (nextSeanceOpt.isEmpty()) {
            topSubjectLabel.setText("Aucune séance à venir");
            topDateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            validateButton.setDisable(true);
            return;
        }

        currentSeance = nextSeanceOpt.get();

        topSubjectLabel.setText(
                currentSeance.getNomCours() != null ? currentSeance.getNomCours() : "Cours"
        );

        if (currentSeance.getDateDebut() != null) {
            topDateLabel.setText(
                    currentSeance.getDateDebut().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
        }

        loadStudentsForCurrentSeance();
        autoGenerateOrLoadCode();
        setCodeMode(true);
    }

    // AUTO CODE GENERATION
    private void autoGenerateOrLoadCode() {
        if (currentSeance == null) return;

        // Reuse existing non-expired code
        if (currentSeance.getCodeEmargement() != null &&
                currentSeance.getCodeEmargementExpire() != null &&
                currentSeance.getCodeEmargementExpire().isAfter(java.time.LocalDateTime.now())) {

            codeValueLabel.setText(currentSeance.getCodeEmargement());
            return;
        }

        // Generate fresh 4-digit code
        var codeOpt = emargementService.generateUniqueCode(currentSeance.getId());
        if (codeOpt.isPresent()) {
            String code = codeOpt.get();
            codeValueLabel.setText(code);

            currentSeance.setCodeEmargement(code);
            currentSeance.setCodeEmargementExpire(
                    java.time.LocalDateTime.now().plusMinutes(EmargementService.VALIDITE_MINUTES)
            );
        } else {
            codeValueLabel.setText("----");
        }
    }

    // LOAD STUDENTS
    private void loadStudentsForCurrentSeance() throws SQLException {
        var studentsList = etudiantDAO.findByCoursId(currentSeance.getCoursId());
        ObservableList<Etudiant> observableStudents = FXCollections.observableArrayList(studentsList);

        studentListView.setItems(observableStudents);

        presenceMap.clear();
        for (Etudiant e : studentsList)
            presenceMap.put(e.getId(), PresenceStatus.NEUTRAL);

        studentListView.setCellFactory(listView -> new StudentCardCell());
    }

    // STUDENT CARD CELL
    private class StudentCardCell extends ListCell<Etudiant> {
        @Override
        protected void updateItem(Etudiant student, boolean empty) {
            super.updateItem(student, empty);

            if (empty || student == null) {
                setGraphic(null);
                return;
            }

            VBox card = new VBox(2);
            Label nameLabel = new Label(student.getNomComplet());
            Label emailLabel = new Label(student.getLogin());

            card.getChildren().addAll(nameLabel, emailLabel);
            card.setStyle(styleFor(student));

            card.setOnMouseClicked(e -> {
                toggleAbsence(student);
                card.setStyle(styleFor(student));
            });

            setGraphic(card);
        }

        private String styleFor(Etudiant student) {
            PresenceStatus status = presenceMap.get(student.getId());

            String base = "-fx-padding: 10; -fx-background-radius: 20; -fx-border-radius: 20;";
            return switch (status) {
                case ABSENT -> base + " -fx-background-color: #ffb3b3;";
                case PRESENT -> base + " -fx-background-color: #b7f0b7;";
                default -> base + " -fx-background-color: #f3f3f3;";
            };
        }
    }

    // TOGGLE ABSENCE
    private void toggleAbsence(Etudiant student) {
        int id = student.getId();
        PresenceStatus current = presenceMap.get(id);

        PresenceStatus next =
                (current == PresenceStatus.ABSENT ? PresenceStatus.NEUTRAL : PresenceStatus.ABSENT);

        presenceMap.put(id, next);
        studentListView.refresh();
    }

    // SWITCH DISPLAY MODE
    @FXML private void handleCodeMode() { setCodeMode(true); }
    @FXML private void handleQrMode() { setCodeMode(false); }

    private void setCodeMode(boolean codeMode) {
        codeValueLabel.setVisible(codeMode);
        codeValueLabel.setManaged(codeMode);

        qrImageView.setVisible(!codeMode);
        qrImageView.setManaged(!codeMode);
    }

    // SAVE ATTENDANCE
    @FXML
    private void handleSaveAttendance() {
        int seanceId = currentSeance.getId();

        for (Etudiant e : studentListView.getItems()) {
            PresenceStatus status = presenceMap.get(e.getId());

            boolean present = (status != PresenceStatus.ABSENT);

            if (present) presenceMap.put(e.getId(), PresenceStatus.PRESENT);

            emargementService.setPresence(seanceId, e.getId(), present);
        }

        studentListView.refresh();
        showAlert("Succès", "Présences enregistrées !");
    }

    // LOGOUT
    @FXML
    private void handleLogout() {
        UserSession.getInstance().clearSession();
        try { App.setRoot("Login"); } catch (IOException ignored) {}
    }

    // ALERT
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
