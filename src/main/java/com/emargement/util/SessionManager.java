package com.emargement.util;
public class SessionManager {

    private static Object currentUser = null;

    public static Object getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Object user) {
        currentUser = user;
    }

    public static void clearSession() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}