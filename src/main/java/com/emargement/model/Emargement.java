package com.emargement.model;

import java.time.LocalDateTime;

public class Emargement {

    private int id;
    private int etudiantId;
    private int seanceId;
    private LocalDateTime dateHeureEmargement;


    // Getters
    public int getId() { return id; }
    public int getEtudiantId() { return etudiantId; }
    public int getSeanceId() { return seanceId; }
    public LocalDateTime getDateHeureEmargement() { return dateHeureEmargement; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setEtudiantId(int etudiantId) { this.etudiantId = etudiantId; }
    public void setSeanceId(int seanceId) { this.seanceId = seanceId; }
    public void setDateHeureEmargement(LocalDateTime dateHeureEmargement) {
        this.dateHeureEmargement = dateHeureEmargement;
    }
}