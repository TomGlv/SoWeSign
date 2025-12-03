package com.emargement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
// ⭐️ Import nécessaire pour Parent, qui corrige les erreurs de compilation précédentes
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage; // Référence à la scène principale

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        // Charge la vue de connexion au démarrage
        scene = new Scene(loadFXML("Login"), 800, 600);
        stage.setScene(scene);
        stage.setTitle("SoWeSign - Connexion");
        stage.show();
    }

    /**
     * Change la vue principale de l'application (le contenu de la scène).
     * @param fxml Le nom du fichier FXML (ex: "DashboardAdmin").
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        // Adapte la taille de la fenêtre à la nouvelle scène
        primaryStage.sizeToScene();
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