// src/main/java/com/emargement/App.java

package com.emargement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        // Start by loading the Login view
        scene = new Scene(loadFXML("Login"), 800, 600);
        stage.setScene(scene);
        stage.setTitle("SoWeSign - Connexion");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        primaryStage.sizeToScene();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        // FIX: Use absolute path rooted at the classpath for all FXML files
        String resourcePath = "/com/emargement/" + fxml + ".fxml";

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(resourcePath));

        if (fxmlLoader.getLocation() == null) {
            throw new IOException("FXML resource not found: " + resourcePath);
        }

        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}