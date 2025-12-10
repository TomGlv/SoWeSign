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

    private static final int LOGIN_WIDTH = 800;
    private static final int LOGIN_HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        Parent root = loadFXML("Login");
        scene = new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT);

        stage.setMaximized(true);

        try {
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Avertissement: style.css non trouvé. Assurez-vous qu'il est dans le classpath.");
        }

        stage.setScene(scene);
        stage.setTitle("SoWeSign - Connexion");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);

        scene.getStylesheets().clear();
        try {
            scene.getStylesheets().add(App.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Avertissement: style.css non trouvé lors du changement de vue.");
        }

        scene.setRoot(root);

        if (fxml.startsWith("Dashboard")) {
            primaryStage.setTitle("SoWeSign - Tableau de Bord");
        } else {
            primaryStage.setTitle("SoWeSign - Connexion");
        }

        primaryStage.centerOnScreen();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}