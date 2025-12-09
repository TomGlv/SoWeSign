package com.emargement.util;

// Les imports peuvent varier, mais le type 'Object' est souvent utilisé pour stocker l'utilisateur générique
// import com.emargement.model.Utilisateur; // Peut être nécessaire si vous utilisez des types spécifiques ailleurs

public class SessionManager {

    // Stocke l'utilisateur actuellement connecté (peut être Etudiant ou Professeur)
    private static Object currentUser = null;

    /**
     * Retourne l'utilisateur actuellement connecté.
     */
    public static Object getCurrentUser() {
        return currentUser;
    }

    /**
     * Définit l'utilisateur courant après une connexion réussie.
     */
    public static void setCurrentUser(Object user) {
        currentUser = user;
    }

    /**
     * ⭐️ CORRECTION : Ajoute la méthode clearSession pour déconnecter l'utilisateur.
     */
    public static void clearSession() {
        currentUser = null;
    }

    // Vous pouvez ajouter d'autres méthodes pour vérifier l'état de la session si nécessaire
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}