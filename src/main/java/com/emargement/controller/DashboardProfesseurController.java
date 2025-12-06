package com.emargement.controller;

import com.emargement.App;
import com.emargement.model.Cours;
import com.emargement.model.Seance;
import com.emargement.model.Utilisateur;
import com.emargement.dao.CoursDAO;
import com.emargement.dao.SeanceDAO;
import com.emargement.dao.EtudiantDAO;
import com.emargement.session.UserSession;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class DashboardProfesseurController {

    @FXML private Label welcomeLabel;
    @FXML private Label dateLabel;
    @FXML private Label subjectLabel;
    @FXML private Label sessionTimeLabel;
    @FXML private Label studentCountLabel;
    @FXML private Button startBtn;
    @FXML private Button themeToggleBtn;

    private final CoursDAO coursDAO = new CoursDAO();
    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();

    private boolean isDarkMode = false;
    private Cours nextCourse;
    private Seance nextSeance;

    private static final long SESSION_DURATION_HOURS = 2;
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        Utilisateur user = UserSession.getInstance().getUtilisateur();
        welcomeLabel.setText("Bonjour, Professeur " + user.getPrenom() + " " + user.getNom());

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy · HH:mm");
        dateLabel.setText(LocalDateTime.now().format(dateFormat));

        themeToggleBtn.setOnAction(e -> toggleTheme());

        startBtn.setDisable(true);
        loadNextScheduledSession(user.getId());
    }

    private void loadNextScheduledSession(int userId) {
        new Thread(() -> {
            try {
                List<Cours> profCourses = coursDAO.findByProfesseurId(userId);

                if (profCourses.isEmpty()) {
                    Platform.runLater(() -> subjectLabel.setText("Aucun cours n'est assigné à votre compte."));
                    return;
                }

                // Find the absolute NEXT scheduled session across ALL courses
                Optional<Seance> nextSessionOpt = profCourses.stream()
                        .flatMap(cours -> {
                            try {
                                // Use getSeancesByCoursId which throws SQLException, requiring the try-catch
                                return seanceDAO.getSeancesByCoursId(cours.getId()).stream()
                                        .filter(s -> s.getDateDebut() != null && s.getDateDebut().isAfter(LocalDateTime.now()))
                                        .map(s -> {
                                            s.setNomCours(cours.getNomCours());
                                            return s;
                                        });
                            } catch (SQLException e) {
                                System.err.println("Erreur de chargement des séances pour le cours ID: " + cours.getId() + ": " + e.getMessage());
                                return null;
                            }
                        })
                        .filter(s -> s != null)
                        .sorted((s1, s2) -> s1.getDateDebut().compareTo(s2.getDateDebut()))
                        .findFirst();

                Platform.runLater(() -> {
                    if (nextSessionOpt.isPresent()) {
                        this.nextSeance = nextSessionOpt.get();
                        this.nextCourse = profCourses.stream()
                                .filter(c -> c.getId() == nextSeance.getCoursId())
                                .findFirst().orElse(null);

                        if (this.nextCourse == null) {
                            subjectLabel.setText("Erreur: Cours Inconnu");
                            startBtn.setDisable(true);
                            return;
                        }

                        updateDashboardDetails(nextCourse, nextSeance);
                        startBtn.setDisable(false);
                    } else {
                        subjectLabel.setText("Aucune séance planifiée prochainement.");
                        sessionTimeLabel.setText("Veuillez vérifier votre emploi du temps.");
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    subjectLabel.setText("Erreur de connexion à la base de données.");
                    new Alert(AlertType.ERROR, "Erreur lors du chargement des données initiales.").showAndWait();
                });
                e.printStackTrace();
            }
        }).start();
    }

    private void updateDashboardDetails(Cours cours, Seance seance) {
        subjectLabel.setText(cours.getNomCours() + " (" + cours.getCode() + ")");

        LocalDateTime dateDebut = seance.getDateDebut();
        LocalDateTime dateFin = seance.getDateFin();

        // Ensure dateFin is valid, otherwise default to 2 hours
        if (dateFin == null || dateFin.isEqual(dateDebut)) {
            dateFin = dateDebut.plusHours(SESSION_DURATION_HOURS);
            sessionTimeLabel.setText(
                    dateDebut.format(DATE_TIME_FORMAT) + " → " + dateFin.format(TIME_FORMAT) + " (Durée estimée)"
            );
        } else {
            sessionTimeLabel.setText(
                    dateDebut.format(DATE_TIME_FORMAT) + " → " + dateFin.format(TIME_FORMAT)
            );
        }

        try {
            int studentCount = etudiantDAO.findByCoursId(cours.getId()).size();
            studentCountLabel.setText(String.valueOf(studentCount));
        } catch (Exception e) {
            studentCountLabel.setText("Erreur");
        }
    }

    @FXML
    private void handleStartSession() {
        if (nextSeance == null || startBtn.isDisable()) {
            new Alert(AlertType.WARNING, "Impossible de démarrer. Aucune séance n'est planifiée.").showAndWait();
            return;
        }
        try {
            UserSession.getInstance().setCurrentSeance(nextSeance);
            App.setRoot("SessionView");
        } catch (IOException e) {
            new Alert(AlertType.ERROR, "Impossible de charger la vue de session. Vérifiez SessionView.fxml.").showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleTheme() {
        Scene scene = themeToggleBtn.getScene();
        isDarkMode = !isDarkMode;

        String cssPath = isDarkMode ?
                "/com/emargement/styles-dark.css" :
                "/com/emargement/styles.css";

        scene.getStylesheets().clear();

        try {
            scene.getStylesheets().add(
                    getClass().getResource(cssPath).toExternalForm()
            );
            themeToggleBtn.setText(isDarkMode ? "☀️ Mode clair" : "🌗 Mode sombre");
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erreur de chargement du thème.").showAndWait();
            e.printStackTrace();
            try {
                scene.getStylesheets().add(getClass().getResource("/com/emargement/styles.css").toExternalForm());
            } catch (Exception ignore) {}
        }
    }

    @FXML
    private void handleLogout() throws IOException {
        UserSession.getInstance().clearSession();
        App.setRoot("Login");
    }
}