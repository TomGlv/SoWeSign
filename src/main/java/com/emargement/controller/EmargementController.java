package com.emargement.controller;

import com.emargement.dao.EmargementDAO;
import com.emargement.model.EtudiantPresence;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

// Ce contrôleur doit implémenter Initializable pour avoir la méthode initialize
public class EmargementController implements Initializable {

    // 1. DÉCLARATIONS FXML (Assurez-vous que ces fx:id sont corrects dans votre FXML)
    @FXML
    private TableView<EtudiantPresence> etudiantTableView;

    @FXML
    private TableColumn<EtudiantPresence, String> colNom;

    @FXML
    private TableColumn<EtudiantPresence, String> colNumero;

    @FXML
    private TableColumn<EtudiantPresence, Boolean> colPresence;

    // 2. VARIABLE DE SÉANCE (CRITIQUE pour l'appel au DAO)
    private int currentSeanceId = -1; // -1 tant qu'aucune séance n'est chargée.

    // Cette méthode est appelée lors du chargement de la vue.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Appeler la méthode de configuration du tableau
        setupPresenceTable();
    }

    // Méthode pour charger et afficher les données (à compléter selon votre logique)
    public void loadPresenceData(int seanceId, int coursId) {
        this.currentSeanceId = seanceId;
        // ⚠️ NOTE: Vous devrez ajouter la logique d'appel à EtudiantDAO pour remplir le tableau ici
        // Ex: etudiantTableView.setItems(FXCollections.observableArrayList(new EtudiantDAO().findEtudiantsPresenceForSeance(seanceId, coursId)));
    }


    private void setupPresenceTable() {

        // --- 1. Définition des propriétés de lecture ---
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomComplet"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroEtudiant"));
        colPresence.setCellValueFactory(new PropertyValueFactory<>("estPresent"));

        // --- 2. Autorisation de l'édition (CRITIQUE) ---
        etudiantTableView.setEditable(true);
        colPresence.setEditable(true);

        // --- 3. Configuration du Cell Factory pour la CheckBox et l'interaction (SOLUTION) ---
        colPresence.setCellFactory(column -> {

            CheckBoxTableCell<EtudiantPresence, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);

            // Logique exécutée lorsque la case est cliquée
            cell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(Integer index) {

                    EtudiantPresence etudiant = etudiantTableView.getItems().get(index);

                    // L'état souhaité après le clic
                    boolean nouvelEtat = !etudiant.isEstPresent();

                    // Vérification de sécurité
                    if (currentSeanceId == -1) {
                        System.err.println("Erreur: L'ID de la séance n'est pas chargé (currentSeanceId est -1).");
                        return etudiant.estPresentProperty();
                    }

                    // ⭐️ APPEL DE LA COUCHE DAO ⭐️
                    boolean success = new EmargementDAO().saveOrDeleteAttendance(
                            currentSeanceId,
                            etudiant.getEtudiantId(),
                            nouvelEtat
                    );

                    if (success) {
                        // Succès: Met à jour le modèle JavaFX pour que la case reste cochée/décochée
                        etudiant.setEstPresent(nouvelEtat);
                        System.out.println("Présence de " + etudiant.getNomComplet() + " mise à jour: " + (nouvelEtat ? "Présent" : "Absent"));
                    } else {
                        // Échec: Laisse le modèle tel quel
                        System.err.println("Échec de l'écriture en base de données. L'état sera rétabli.");
                    }

                    // Retourne la propriété pour lier la CheckBox
                    return etudiant.estPresentProperty();
                }
            });

            return cell;
        });
    }
}