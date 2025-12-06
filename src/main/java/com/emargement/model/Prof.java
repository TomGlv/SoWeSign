package com.emargement.model;

public class Prof {

    private int id;
    private int utilisateurId;
    private String departement;
    private Utilisateur utilisateur;

    public Prof() {}

    public Prof(int id, int utilisateurId, String departement) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.departement = departement;
    }

    public int getId() { return id; }
    public int getUtilisateurId() { return utilisateurId; }
    public String getDepartement() { return departement; }
    public Utilisateur getUtilisateur() { return utilisateur; }

    public void setId(int id) { this.id = id; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    public void setDepartement(String departement) { this.departement = departement; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
}
