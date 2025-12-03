package com.emargement.model;

public class Utilisateur {
    private int id;
    private String login;
    private String motDePasseHashed;
    private String nom;
    private String prenom;
    private Role role; // CHANGEMENT : Utilisation de l'énumération Role

    public Utilisateur() {}

    public Utilisateur(int id, String login, String motDePasseHashed, String nom, String prenom, Role role) {
        this.id = id;
        this.login = login;
        this.motDePasseHashed = motDePasseHashed;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
    }

    // Getters
    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getMotDePasseHashed() { return motDePasseHashed; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public Role getRole() { return role; } // CHANGEMENT : Retourne Role

    // Setters
    public void setId(int id) { this.id = id; }
    public void setLogin(String login) { this.login = login; }
    public void setMotDePasseHashed(String motDePasseHashed) { this.motDePasseHashed = motDePasseHashed; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setRole(Role role) { this.role = role; } // CHANGEMENT : Prend Role en paramètre
}