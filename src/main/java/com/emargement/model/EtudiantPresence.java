package com.emargement.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class EtudiantPresence {

    private int etudiantId;
    private final StringProperty nomComplet;
    private final StringProperty numeroEtudiant;
    private final StringProperty statutPresence;
    private final BooleanProperty estPresent;

    public EtudiantPresence(int etudiantId, String nomComplet, String numeroEtudiant, boolean estPresent) {
        this.etudiantId = etudiantId;
        this.nomComplet = new SimpleStringProperty(nomComplet);
        this.numeroEtudiant = new SimpleStringProperty(numeroEtudiant);
        this.estPresent = new SimpleBooleanProperty(estPresent);
        this.statutPresence = new SimpleStringProperty(estPresent ? "Présent" : "Absent");

        // L'écouteur met à jour le statut texte (colPresence) quand le booléen (colEditPresence) change
        this.estPresent.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean oldValue, Boolean newValue) {
                if (newValue != null) {
                    statutPresence.set(newValue ? "Présent" : "Absent");
                }
            }
        });
    }

    // Constructeur par défaut
    public EtudiantPresence() {
        this(0, "", "", false);
    }

    // --- Getters et Setters du Modèle ---
    public int getEtudiantId() { return etudiantId; }
    public void setEtudiantId(int etudiantId) { this.etudiantId = etudiantId; }

    public String getNomComplet() { return nomComplet.get(); }
    public void setNomComplet(String nomComplet) { this.nomComplet.set(nomComplet); }
    public String getNumeroEtudiant() { return numeroEtudiant.get(); }
    public void setNumeroEtudiant(String numeroEtudiant) { this.numeroEtudiant.set(numeroEtudiant); }
    public String getStatutPresence() { return statutPresence.get(); }
    public void setStatutPresence(String statutPresence) { this.statutPresence.set(statutPresence); }

    public boolean isEstPresent() { return estPresent.get(); }
    public void setEstPresent(boolean estPresent) { this.estPresent.set(estPresent); }
    public StringProperty nomCompletProperty() { return nomComplet; }
    public StringProperty numeroEtudiantProperty() { return numeroEtudiant; }
    public StringProperty statutPresenceProperty() { return statutPresence; }
    public BooleanProperty estPresentProperty() { return estPresent; }
}