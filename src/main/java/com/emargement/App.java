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

    // Constantes de taille initiale minimale (pour le démarrage avant la maximisation)
    private static final int LOGIN_WIDTH = 800;
    private static final int LOGIN_HEIGHT = 600;

    // NOTE : Les constantes DASHBOARD_WIDTH/HEIGHT sont inutiles en mode maximisé.

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Charge la vue de connexion au démarrage
        Parent root = loadFXML("Login");
        scene = new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT); // Taille initiale

        // ⭐️ IMPORTANT : Maximiser la fenêtre de Login au lancement ⭐️
        stage.setMaximized(true);

        // Ajout du chargement du CSS
        try {
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Avertissement: style.css non trouvé. Assurez-vous qu'il est dans le classpath.");
        }

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

        // Réappliquer le CSS au moment du changement de vue
        scene.getStylesheets().clear();
        try {
            scene.getStylesheets().add(App.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Avertissement: style.css non trouvé lors du changement de vue.");
        }

        scene.setRoot(root);

        // ⭐️ Si on passe au Dashboard, on s'assure que la fenêtre reste maximisée.
        // Si on revient au Login, on pourrait vouloir la laisser maximisée ou la redimensionner
        // Ici, on maintient l'état maximisé pour les deux types de vue, ce qui est cohérent.

        if (fxml.startsWith("Dashboard")) {
            primaryStage.setTitle("SoWeSign - Tableau de Bord");
        } else {
            primaryStage.setTitle("SoWeSign - Connexion");
            // Si vous souhaitez désactiver le mode maximisé pour le login:
            // primaryStage.setMaximized(false);
            // primaryStage.setWidth(LOGIN_WIDTH);
            // primaryStage.setHeight(LOGIN_HEIGHT);
            // primaryStage.sizeToScene(); // Optionnel si on définit la taille
        }

        // Cette ligne est inutile si primaryStage.setMaximized(true) est utilisé.
        // primaryStage.sizeToScene();

        primaryStage.centerOnScreen();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        // Utilisation de .getResource(fxml + ".fxml") au lieu de .getResource(fxml)
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