package com.emargement.model;

public class Cours {
    private int id;
    private String nomCours;
    private String code;
    private String description;
    private int professeurId;

    public Cours() {}

    public Cours(int id, String nomCours, String code, String description, int professeurId) {
        this.id = id;
        this.nomCours = nomCours;
        this.code = code;
        this.description = description;
        this.professeurId = professeurId;
    }

    // Getters
    public int getId() { return id; }
    public String getNomCours() { return nomCours; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public int getProfesseurId() { return professeurId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNomCours(String nomCours) { this.nomCours = nomCours; }
    public void setCode(String code) { this.code = code; }
    public void setDescription(String description) { this.description = description; }
    public void setProfesseurId(int professeurId) { this.professeurId = professeurId; }

    /**
     * ⭐️ CORRECTION : Redéfinit toString() pour un affichage lisible dans la ListView.
     * Ex: "Java Avancé (JAV-A)"
     */
    @Override
    public String toString() {
        return nomCours + " (" + code + ")";
    }
}