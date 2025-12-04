package com.emargement.model;

import java.time.LocalDateTime;

public class Emargement {
    private int id;
    private int seanceId;
    private int etudiantId;
    private LocalDateTime dateEmargement;

    public Emargement() {}

    public Emargement(int seanceId, int etudiantId, LocalDateTime dateEmargement) {
        this.seanceId = seanceId;
        this.etudiantId = etudiantId;
        this.dateEmargement = dateEmargement;
    }

    // Getters
    public int getId() { return id; }
    public int getSeanceId() { return seanceId; }
    public int getEtudiantId() { return etudiantId; }
    public LocalDateTime getDateEmargement() { return dateEmargement; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setSeanceId(int seanceId) { this.seanceId = seanceId; }
    public void setEtudiantId(int etudiantId) { this.etudiantId = etudiantId; }
    public void setDateEmargement(LocalDateTime dateEmargement) { this.dateEmargement = dateEmargement; }
}