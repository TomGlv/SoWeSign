package com.emargement.service;

import com.emargement.dao.EtudiantDAO;
import com.emargement.dao.SeanceDAO;
import com.emargement.dao.EmargementDAO;
import com.emargement.model.Seance;
import com.emargement.model.Etudiant;
import com.emargement.model.Utilisateur;
import com.emargement.model.Emargement;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.sql.SQLException;

public class EmargementService {

    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final EtudiantDAO etudiantDAO = new EtudiantDAO(); // Maintenu pour la logique Etudiant
    private final EmargementDAO emargementDAO = new EmargementDAO();

    public static final long VALIDITE_MINUTES = 10;

    /**
     * Génère un code d'émargement unique et l'enregistre pour la séance spécifiée.
     */
    public Optional<String> generateUniqueCode(int seanceId) {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        String finalCode = code.toString();
        // L'heure d'expiration est l'heure courante plus la validité
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(VALIDITE_MINUTES);

        try {
            // Mise à jour de la séance dans la base de données
            seanceDAO.updateCodeEmargement(seanceId, finalCode, expiration);
            return Optional.of(finalCode);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement du code pour la séance " + seanceId + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Tente d'émarger un étudiant avec un code unique.
     * ⭐️ CORRECTION CRITIQUE : Signature ajustée pour correspondre au contrôleur Étudiant.
     * Le code du contrôleur Étudiant appelait (Etudiant, String), mais la logique interne
     * de ce service semble nécessiter le code d'abord ou l'utilisateur.
     * * Pour minimiser les changements, nous allons adapter la logique pour accepter
     * (Etudiant etudiant, String code), car c'est ce que le contrôleur envoie.
     * @param etudiant L'objet Etudiant (déjà récupéré par le contrôleur depuis la session).
     * @param code Le code d'émargement saisi.
     * @return Un message de succès/échec (String) plutôt qu'un boolean (pour l'affichage UI).
     */
    public String emarger(Etudiant etudiant, String code) {

        // 1. Recherche de la séance active par le code
        Optional<Seance> seanceOpt = seanceDAO.findByCodeEmargement(code);

        if (seanceOpt.isEmpty()) {
            return "Échec émargement pour " + etudiant.getUtilisateur().getLogin() + " : Code introuvable ou non actif.";
        }

        Seance seance = seanceOpt.get();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = seance.getCodeEmargementExpire();

        // 2. Vérification de l'expiration du code
        if (expirationTime == null || expirationTime.isBefore(currentTime)) {
            System.err.println("Échec émargement : Code expiré. Code: " + code + ", Heure Actuelle (Java): " + currentTime + ", Heure Expiration (DB): " + expirationTime);
            return "Échec émargement pour " + etudiant.getUtilisateur().getLogin() + " : Code expiré.";
        }

        // 3. Vérification si l'étudiant a déjà émargé (anti-doublon)
        if (emargementDAO.hasAttended(seance.getId(), etudiant.getId())) {
            System.out.println("Échec émargement pour " + etudiant.getUtilisateur().getLogin() + " : Déjà émargé.");
            return "Échec émargement pour " + etudiant.getUtilisateur().getLogin() + " : Déjà émargé.";
        }

        // 4. Enregistrer l'émargement
        Emargement emargement = new Emargement();
        emargement.setSeanceId(seance.getId());
        emargement.setEtudiantId(etudiant.getId());
        emargement.setDateHeureEmargement(currentTime); // Utiliser l'heure actuelle pour l'enregistrement

        boolean success = emargementDAO.save(emargement);

        if (success) {
            System.out.println("Émargement réussi pour l'étudiant " + etudiant.getUtilisateur().getLogin() + " à la séance " + seance.getId());
            return "Succès : Présence enregistrée pour " + etudiant.getUtilisateur().getLogin() + ".";
        } else {
            System.out.println("Échec émargement pour " + etudiant.getUtilisateur().getLogin() + " : Erreur d'enregistrement.");
            return "Échec émargement : Erreur interne lors de l'enregistrement.";
        }
    }
}