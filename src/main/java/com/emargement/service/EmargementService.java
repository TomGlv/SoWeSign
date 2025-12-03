package com.emargement.service;

import com.emargement.dao.EtudiantDAO;
import com.emargement.dao.SeanceDAO;
import com.emargement.model.Seance;
import com.emargement.model.Etudiant;
import com.emargement.model.Utilisateur;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.sql.SQLException;

public class EmargementService {

    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();

    // Durée de validité du code (10 minutes)
    public static final long VALIDITE_MINUTES = 10;

    public Optional<String> generateUniqueCode(int seanceId) {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();

        // Génère un code de 6 caractères
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

    /**
     * Tente d'émarger un étudiant avec un code unique.
     */
    public boolean emarger(String code, Utilisateur utilisateur) {
        Optional<Seance> seanceOpt = seanceDAO.findByCodeEmargement(code);

        if (seanceOpt.isEmpty()) return false;
        Seance seance = seanceOpt.get();

        // Vérification de l'expiration
        if (seance.getCodeEmargementExpire() == null || seance.getCodeEmargementExpire().isBefore(LocalDateTime.now())) {
            return false;
        }

        Optional<Etudiant> etudiantOpt = etudiantDAO.findByUtilisateurId(utilisateur.getId());
        if (etudiantOpt.isEmpty()) return false;

        // TODO: Enregistrer l'émargement dans la table 'emargement' (DAO manquant)
        System.out.println("Émargement réussi pour l'étudiant " + utilisateur.getLogin());
        return true;
    }

    public List<Etudiant> getEtudiantsBySeance(int seanceId) {
        // Simule la récupération des étudiants inscrits au cours de la séance
        return etudiantDAO.findByCoursId(0);
    }
}