package com.emargement.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Seance {
    private int id;
    private int coursId;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String salle; // ⭐️ Champ manquant ajouté
    private String codeEmargement;
    private LocalDateTime codeEmargementExpire;

    public Seance() {
    }
    public Seance(int id, int coursId, LocalDateTime dateDebut, LocalDateTime dateFin, String salle, String codeEmargement, LocalDateTime codeEmargementExpire) {
        this.id = id;
        this.coursId = coursId;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.salle = salle;
        this.codeEmargement = codeEmargement;
        this.codeEmargementExpire = codeEmargementExpire;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoursId() {
        return coursId;
    }

    public void setCoursId(int coursId) {
        this.coursId = coursId;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    // ⭐️ Getter de salle ajouté
    public String getSalle() {
        return salle;
    }

    // ⭐️ Setter de salle ajouté
    public void setSalle(String salle) {
        this.salle = salle;
    }

    public String getCodeEmargement() {
        return codeEmargement;
    }

    public void setCodeEmargement(String codeEmargement) {
        this.codeEmargement = codeEmargement;
    }

    public LocalDateTime getCodeEmargementExpire() {
        return codeEmargementExpire;
    }

    public void setCodeEmargementExpire(LocalDateTime codeEmargementExpire) {
        this.codeEmargementExpire = codeEmargementExpire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seance seance = (Seance) o;
        return id == seance.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}