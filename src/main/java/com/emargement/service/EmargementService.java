package com.emargement.service;

import com.emargement.dao.EmargementDAO;
import com.emargement.dao.EtudiantDAO;
import com.emargement.dao.SeanceDAO;
import com.emargement.model.Seance;
import com.emargement.model.Etudiant;
import com.emargement.model.Utilisateur;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.sql.SQLException;

public class EmargementService {

    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();
    private final EmargementDAO emargementDAO = new EmargementDAO();

    public static final long VALIDITE_MINUTES = 10;

    /** PROF : génère un code unique pour la séance. */
    public Optional<String> generateUniqueCode(int seanceId) {
        SecureRandom random = new SecureRandom();

        // Generate a 4-digit code (ex: 0042, 1936, 8209)
        int codeValue = random.nextInt(10000);
        String finalCode = String.format("%04d", codeValue);

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(VALIDITE_MINUTES);

        try {
            seanceDAO.updateCodeEmargement(seanceId, finalCode, expiration);
            return Optional.of(finalCode);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement du code pour la séance " + seanceId + ": " + e.getMessage());
            return Optional.empty();
        }
    }


    /** ÉTUDIANT : émarge avec code. */
    public boolean emarger(String code, Utilisateur utilisateur) {
        try {
            Optional<Seance> seanceOpt = seanceDAO.findByCodeEmargement(code);

            if (seanceOpt.isEmpty()) {
                System.out.println("DEBUG SERVICE: Code non trouvé.");
                return false;
            }

            Seance seance = seanceOpt.get();

            if (seance.getCodeEmargementExpire() == null ||
                    seance.getCodeEmargementExpire().isBefore(LocalDateTime.now())) {
                System.out.println("DEBUG SERVICE: Code expiré.");
                return false;
            }

            Optional<Etudiant> etudiantOpt = etudiantDAO.findByUtilisateurId(utilisateur.getId());
            if (etudiantOpt.isEmpty()) {
                System.out.println("DEBUG SERVICE: Utilisateur non trouvé comme étudiant.");
                return false;
            }
            Etudiant etudiant = etudiantOpt.get();

            boolean success = emargementDAO.createEmargement(seance.getId(), etudiant.getId());
            if (success) {
                System.out.println("DEBUG SERVICE: Émargement réussi pour " + utilisateur.getLogin());
            } else {
                System.out.println("DEBUG SERVICE: Échec de l'émargement (probablement déjà enregistré).");
            }
            return success;

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'émargement : " + e.getMessage());
            return false;
        }
    }

    /** PROF : présence/absence manuelle (utilisée dans DashboardProfesseur). */
    public boolean setPresence(int seanceId, int etudiantId, boolean present) {
        try {
            emargementDAO.upsertPresence(seanceId, etudiantId, present);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde de présence manuelle : " + e.getMessage());
            return false;
        }
    }
}
