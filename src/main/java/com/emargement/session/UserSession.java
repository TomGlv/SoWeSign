package com.emargement.session;

import com.emargement.model.Utilisateur;

public class UserSession {

    private static UserSession instance;
    private Utilisateur utilisateur;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void clearSession() {
        utilisateur = null;
    }
}