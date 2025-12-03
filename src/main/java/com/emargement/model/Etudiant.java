package com.emargement.model;

public class Etudiant {
    private int id;
    private String numeroEtudiant;
    private int utilisateurId;
    private Utilisateur utilisateur; // Pour récupérer Nom/Prénom/Login

    public Etudiant() {}

    // Constructeur complet (utilisé dans les DAOs)
    public Etudiant(int id, String numeroEtudiant, int utilisateurId) {
        this.id = id;
        this.numeroEtudiant = numeroEtudiant;
        this.utilisateurId = utilisateurId;
    }

    // Getters
    public int getId() { return id; }
    public String getNumeroEtudiant() { return numeroEtudiant; }
    public int getUtilisateurId() { return utilisateurId; }
    public Utilisateur getUtilisateur() { return utilisateur; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNumeroEtudiant(String numeroEtudiant) { this.numeroEtudiant = numeroEtudiant; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
}