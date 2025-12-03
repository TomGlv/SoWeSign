package com.emargement.model;

import java.time.LocalDateTime;

public class Seance {
    private int id;
    private int coursId;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String codeEmargement; // Code unique pour l'émargement en ligne
    private LocalDateTime codeEmargementExpire;
    private String nomCours; // Pour l'affichage

    public Seance() {}

    // Getters
    public int getId() { return id; }
    public int getCoursId() { return coursId; }
    public LocalDateTime getDateDebut() { return dateDebut; }
    public LocalDateTime getDateFin() { return dateFin; }
    public String getCodeEmargement() { return codeEmargement; }
    public LocalDateTime getCodeEmargementExpire() { return codeEmargementExpire; }
    public String getNomCours() { return nomCours; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCoursId(int coursId) { this.coursId = coursId; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }
    public void setCodeEmargement(String codeEmargement) { this.codeEmargement = codeEmargement; }
    public void setCodeEmargementExpire(LocalDateTime codeEmargementExpire) { this.codeEmargementExpire = codeEmargementExpire; }
    public void setNomCours(String nomCours) { this.nomCours = nomCours; }
}