// Fichier : src/main/java/com/emargement/service/AuthService.java (Version Sécurisée)

package com.emargement.service;

import com.emargement.dao.UtilisateurDAO;
import com.emargement.model.Utilisateur;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class AuthService {

    // Instanciation de l'encodeur BCrypt
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    /**
     * Tente d'authentifier un utilisateur.
     */
    public Optional<Utilisateur> authenticate(String login, String rawPassword) {
        Optional<Utilisateur> userOpt = utilisateurDAO.findByLogin(login);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            String storedHashedPassword = user.getMotDePasseHashed();

            // ⭐️ Rétablissement de la VÉRIFICATION SÉCURISÉE (BCrypt)
            if (passwordEncoder.matches(rawPassword, storedHashedPassword)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /**
     * Hache un mot de passe pour le stockage en base de données.
     */
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}