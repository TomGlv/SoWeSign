package com.emargement.controller;

import com.emargement.App;
import com.emargement.dao.EtudiantDAO;
import com.emargement.model.Etudiant;
import com.emargement.model.Seance;
import com.emargement.session.UserSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionController {

    @FXML private VBox studentList;
    @FXML private Label codeLabel;
    @FXML private Label expirationLabel;
    @FXML private Label headerSubjectLabel;
    @FXML private Label headerTimeLabel;
    @FXML private Label headerDateLabel;
    @FXML private Label statusSummaryLabel;
    @FXML private Button tabCodeBtn;
    @FXML private Button tabQRBtn;
    @FXML private VBox qrDisplay;
    @FXML private VBox codeDisplay;
    @FXML private Button saveBtn;
    @FXML private Button endSessionBtn;

    // --- Data and DAOs ---
    private final Map<Integer, Boolean> studentPresenceMap = new HashMap<>();
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();
    private Seance currentSeance;

    // Duration for display only (used for the countdown placeholder)
    private static final long DISPLAY_VALIDITE_MINUTES = 10;

    /** Generates a random 4-digit numeric code (1000-9999). */
    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        // Generate a number between 1000 and 9999 (inclusive)
        return String.valueOf(random.nextInt(9000) + 1000);
    }


    @FXML
    public void initialize() {
        currentSeance = UserSession.getInstance().getCurrentSeance();

        // 1. Check for valid session data (Error handling)
        if (currentSeance == null || currentSeance.getNomCours() == null) {
            headerSubjectLabel.setText("Session non trouvée");
            // Display random code if the session object is NULL
            codeLabel.setText(generateRandomCode());
            return;
        }

        // 2. Initialize header UI
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        headerSubjectLabel.setText(currentSeance.getNomCours());
        headerDateLabel.setText(currentSeance.getDateDebut().format(DateTimeFormatter.ofPattern("EEEE d MMMM yyyy")));
        headerTimeLabel.setText(currentSeance.getDateDebut().format(timeFmt) + " - " + currentSeance.getDateFin().format(timeFmt));

        // 3. Load Students (Required for attendance marking)
        loadStudents();

        // 4. Generate and Display the first code using the random generator
        handleGenerateCode();

        // 5. Tab and Button Actions setup
        tabCodeBtn.setOnAction(e -> handleTabSelection("code"));
        tabQRBtn.setOnAction(e -> handleTabSelection("qr"));
        saveBtn.setOnAction(e -> handleSaveAndQuit());
        endSessionBtn.setOnAction(e -> handleCancelSession());
    }

    private void handleTabSelection(String type) {
        tabCodeBtn.getStyleClass().clear();
        tabQRBtn.getStyleClass().clear();

        tabCodeBtn.getStyleClass().add("tab-btn");
        tabQRBtn.getStyleClass().add("tab-btn");

        if ("code".equals(type)) {
            tabCodeBtn.getStyleClass().add("tab-btn-active");
            qrDisplay.setVisible(false); qrDisplay.setManaged(false);
            codeDisplay.setVisible(true); codeDisplay.setManaged(true);
        } else if ("qr".equals(type)) {
            tabQRBtn.getStyleClass().add("tab-btn-active");
            codeDisplay.setVisible(false); codeDisplay.setManaged(false);
            qrDisplay.setVisible(true); qrDisplay.setManaged(true);
        }
    }


    /** Displays a new random code (4 digits) and updates the expiration time.
     * This method is called by the "Regénérer le code" button.
     */
    @FXML
    private void handleGenerateCode() {
        String code = generateRandomCode();

        codeLabel.setText(code);

        LocalDateTime expiry = LocalDateTime.now().plusMinutes(DISPLAY_VALIDITE_MINUTES);
        expirationLabel.setText("Valable jusqu'à " + expiry.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }


    private void loadStudents() {
        try {
            List<Etudiant> etudiants = etudiantDAO.findByCoursId(currentSeance.getCoursId());

            Platform.runLater(() -> {
                studentList.getChildren().clear();
                etudiants.forEach(s -> {
                    studentList.getChildren().add(createStudentRow(s));
                    studentPresenceMap.put(s.getId(), null);
                });
                updateStatusSummary();
            });

        } catch (Exception e) {
            Platform.runLater(() -> {
                new Alert(AlertType.ERROR, "Erreur lors du chargement des étudiants. La liste est vide.").showAndWait();
            });
            e.printStackTrace();
        }
    }

    // Remaining helper methods (createStudentRow, updateStudentStatus, etc.)

    private Parent createStudentRow(Etudiant student) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/emargement/StudentCard.fxml")
            );
            Parent row = loader.load();

            Label nameLabel = (Label) row.lookup("#studentNameLabel");
            Label emailLabel = (Label) row.lookup("#studentEmailLabel");
            Label statusPill = (Label) row.lookup("#statusPill");
            Button presentBtn = (Button) row.lookup("#presentBtn");
            Button absentBtn = (Button) row.lookup("#absentBtn");
            HBox cardRoot = (HBox) row.lookup("#studentCardRoot");

            nameLabel.setText(student.getNomComplet());
            emailLabel.setText(student.getLogin());

            presentBtn.setOnAction(e -> updateStudentStatus(student.getId(), true, cardRoot, statusPill));
            absentBtn.setOnAction(e -> updateStudentStatus(student.getId(), false, cardRoot, statusPill));

            return row;
        } catch (IOException e) {
            e.printStackTrace();
            return new Label("Error loading student card for " + student.getNomComplet());
        }
    }

    private void updateStudentStatus(int studentId, boolean present, HBox cardRoot, Label statusPill) {
        studentPresenceMap.put(studentId, present);

        String bgColor = present ? "#f0fff0" : "#fff0f0";

        statusPill.setText(present ? "Présent" : "Absent");
        statusPill.getStyleClass().setAll(present ? "status-pill-present" : "status-pill-absent");

        cardRoot.setStyle("-fx-border-color: " + (present ? "#34c759" : "#ff3b30") + "; -fx-border-width: 2px; -fx-background-color: " + bgColor + ";");

        updateStatusSummary();
    }

    private void updateStatusSummary() {
        long present = studentPresenceMap.values().stream().filter(Boolean.TRUE::equals).count();
        long absent = studentPresenceMap.values().stream().filter(Boolean.FALSE::equals).count();
        long pending = studentPresenceMap.values().stream().filter(status -> status == null).count();

        statusSummaryLabel.setText(String.format("%d Présent / %d Absent / %d En attente", present, absent, pending));
    }


    @FXML
    private void handleSaveAndQuit() {
        if (currentSeance == null) return;

        new Alert(AlertType.INFORMATION, "La présence a été marquée localement. (Sauvegarde BDD désactivée pour ce test).").showAndWait();

        try {
            UserSession.getInstance().clearCurrentSeance();
            App.setRoot("DashboardProfesseur");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelSession() {
        new Alert(AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir annuler la session et perdre les marques de présence?").showAndWait();

        try {
            UserSession.getInstance().clearCurrentSeance();
            App.setRoot("DashboardProfesseur");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}