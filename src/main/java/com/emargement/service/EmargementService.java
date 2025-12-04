package com.emargement.service;

import com.emargement.dao.EmargementDAO;
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
    private final EmargementDAO emargementDAO = new EmargementDAO(); // ⭐️ Ajout du DAO

    // Durée de validité du code (10 minutes)
    public static final long VALIDITE_MINUTES = 10;

    /**
     * Génère un code unique de 6 caractères et l'enregistre pour la séance.
     */
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
     * Gère la vérification du code et de l'expiration.
     */
    public boolean emarger(String code, Utilisateur utilisateur) {
        try {
            Optional<Seance> seanceOpt = seanceDAO.findByCodeEmargement(code);

            if (seanceOpt.isEmpty()) {
                System.out.println("DEBUG SERVICE: Code non trouvé.");
                return false;
            }

            Seance seance = seanceOpt.get();

            // Vérification de l'expiration
            if (seance.getCodeEmargementExpire() == null || seance.getCodeEmargementExpire().isBefore(LocalDateTime.now())) {
                System.out.println("DEBUG SERVICE: Code expiré.");
                return false;
            }

            Optional<Etudiant> etudiantOpt = etudiantDAO.findByUtilisateurId(utilisateur.getId());
            if (etudiantOpt.isEmpty()) {
                System.out.println("DEBUG SERVICE: Utilisateur non trouvé comme étudiant.");
                return false;
            }
            Etudiant etudiant = etudiantOpt.get();

            // ⭐️ Enregistrer l'émargement dans la table 'emargement'
            boolean success = emargementDAO.createEmargement(seance.getId(), etudiant.getId());
            if (success) {
                System.out.println("DEBUG SERVICE: Émargement réussi pour l'étudiant " + utilisateur.getLogin());
            } else {
                System.out.println("DEBUG SERVICE: Échec de l'émargement (probablement déjà enregistré).");
            }
            return success;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'émargement : " + e.getMessage());
            return false;
        }
    }

    // TODO: Ajouter une méthode pour récupérer les étudiants et leur statut de présence pour une séance.
}