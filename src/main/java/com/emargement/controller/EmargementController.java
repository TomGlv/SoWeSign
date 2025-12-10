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

public class EmargementController implements Initializable {

    @FXML
    private TableView<EtudiantPresence> etudiantTableView;

    @FXML
    private TableColumn<EtudiantPresence, String> colNom;

    @FXML
    private TableColumn<EtudiantPresence, String> colNumero;

    @FXML
    private TableColumn<EtudiantPresence, Boolean> colPresence;

    private int currentSeanceId = -1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupPresenceTable();
    }

    public void loadPresenceData(int seanceId, int coursId) {
        this.currentSeanceId = seanceId;
    }


    private void setupPresenceTable() {

        colNom.setCellValueFactory(new PropertyValueFactory<>("nomComplet"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroEtudiant"));
        colPresence.setCellValueFactory(new PropertyValueFactory<>("estPresent"));

        etudiantTableView.setEditable(true);
        colPresence.setEditable(true);

        colPresence.setCellFactory(column -> {

            CheckBoxTableCell<EtudiantPresence, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);

            cell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(Integer index) {

                    EtudiantPresence etudiant = etudiantTableView.getItems().get(index);

                    boolean nouvelEtat = !etudiant.isEstPresent();

                    if (currentSeanceId == -1) {
                        System.err.println("Erreur: L'ID de la séance n'est pas chargé (currentSeanceId est -1).");
                        return etudiant.estPresentProperty();
                    }

                    boolean success = new EmargementDAO().saveOrDeleteAttendance(
                            currentSeanceId,
                            etudiant.getEtudiantId(),
                            nouvelEtat
                    );

                    if (success) {
                        etudiant.setEstPresent(nouvelEtat);
                        System.out.println("Présence de " + etudiant.getNomComplet() + " mise à jour: " + (nouvelEtat ? "Présent" : "Absent"));
                    } else {
                        System.err.println("Échec de l'écriture en base de données. L'état sera rétabli.");
                    }

                    return etudiant.estPresentProperty();
                }
            });

            return cell;
        });
    }
}