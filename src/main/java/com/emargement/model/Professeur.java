package com.emargement.model;

public class Professeur {
    private int id;
    private int utilisateurId;
    private Utilisateur utilisateur;

    public Professeur() {}

    // Getters
    public int getId() { return id; }
    public int getUtilisateurId() { return utilisateurId; }
    public Utilisateur getUtilisateur() { return utilisateur; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    @Override
    public String toString() {
        return utilisateur != null ? utilisateur.getPrenom() + " " + utilisateur.getNom() : "Professeur Inconnu";
    }
}