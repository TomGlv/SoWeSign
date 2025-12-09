package com.emargement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage; // Référence à la scène principale

    // Constantes de taille
    private static final int LOGIN_WIDTH = 800;
    private static final int LOGIN_HEIGHT = 600;
    private static final int DASHBOARD_WIDTH = 1100; // Augmenté pour un look pro
    private static final int DASHBOARD_HEIGHT = 700; // Grande hauteur

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        // Charge la vue de connexion au démarrage
        Parent root = loadFXML("Login");
        scene = new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT);

        // ⭐️ IMPORTANT : Ajout du chargement du CSS pour l'écran de connexion
        // Utile si vous voulez styliser l'écran de Login plus tard.
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("SoWeSign - Connexion");
        stage.show();
    }

    /**
     * Change la vue principale de l'application (le contenu de la scène).
     * @param fxml Le nom du fichier FXML (ex: "DashboardProfesseur").
     */
    public static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);

        // ⭐️ ÉTAPE CLÉ : Réappliquer le CSS au moment du changement de vue
        // Cela garantit que le nouveau FXML (ex: DashboardProfesseur) utilise le style.
        scene.getStylesheets().clear(); // Nettoyer les styles précédents (si besoin)
        scene.getStylesheets().add(App.class.getResource("style.css").toExternalForm());

        scene.setRoot(root);

        // ⭐️ Gestion des grandes tailles pour le Dashboard
        if (fxml.startsWith("Dashboard")) {
            primaryStage.setWidth(DASHBOARD_WIDTH);
            primaryStage.setHeight(DASHBOARD_HEIGHT);
            primaryStage.setTitle("SoWeSign - Tableau de Bord");
        } else {
            // Revenir à la taille initiale pour le login/autres
            primaryStage.setWidth(LOGIN_WIDTH);
            primaryStage.setHeight(LOGIN_HEIGHT);
            primaryStage.setTitle("SoWeSign - Connexion");
        }

        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Méthode main standard pour lancer l'application JavaFX.
     */
    public static void main(String[] args) {
        launch(args);
    }
}