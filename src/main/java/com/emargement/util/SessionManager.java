// Fichier : src/main/java/com/emargement/util/SessionManager.java

package com.emargement.util;

import com.emargement.model.Utilisateur;

/**
 * Gère la session utilisateur actuelle de l'application.
 */
public class SessionManager {

    private static Utilisateur currentUser;

    /**
     * Définit l'utilisateur actuellement connecté.
     * @param user L'utilisateur après une authentification réussie.
     */
    public static void setCurrentUser(Utilisateur user) {
        currentUser = user;
    }

    /**
     * Récupère l'utilisateur actuellement connecté.
     * @return L'objet Utilisateur, ou null si personne n'est connecté.
     */
    public static Utilisateur getCurrentUser() {
        return currentUser;
    }

    /**
     * Vérifie si un utilisateur est connecté.
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Déconnecte l'utilisateur.
     */
    public static void cleanSession() {
        currentUser = null;
    }
}