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
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();
    private final EmargementDAO emargementDAO = new EmargementDAO();

    public static final long VALIDITE_MINUTES = 10;

    public Optional<String> generateUniqueCode(int seanceId) {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        String finalCode = code.toString();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(VALIDITE_MINUTES);

        try {
            seanceDAO.updateCodeEmargement(seanceId, finalCode, expiration);
            return Optional.of(finalCode);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement du code pour la séance " + seanceId + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    public String emarger(Etudiant etudiant, String code) {

        Optional<Seance> seanceOpt = seanceDAO.findByCodeEmargement(code);

        if (seanceOpt.isEmpty()) {
            return "Échec émargement pour " + etudiant.getUtilisateur().getLogin() + " : Code introuvable ou non actif.";
        }

        Seance seance = seanceOpt.get();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = seance.getCodeEmargementExpire();

        if (expirationTime == null || expirationTime.isBefore(currentTime)) {
            System.err.println("Échec émargement : Code expiré. Code: " + code + ", Heure Actuelle (Java): " + currentTime + ", Heure Expiration (DB): " + expirationTime);
            return "Échec émargement pour " + etudiant.getUtilisateur().getLogin() + " : Code expiré.";
        }

        if (emargementDAO.hasAttended(seance.getId(), etudiant.getId())) {
            System.out.println("Échec émargement pour " + etudiant.getUtilisateur().getLogin() + " : Déjà émargé.");
            return "Échec émargement pour " + etudiant.getUtilisateur().getLogin() + " : Déjà émargé.";
        }

        Emargement emargement = new Emargement();
        emargement.setSeanceId(seance.getId());
        emargement.setEtudiantId(etudiant.getId());
        emargement.setDateHeureEmargement(currentTime);

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