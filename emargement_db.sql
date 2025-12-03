-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mer. 03 déc. 2025 à 15:59
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `emargement_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `cours`
--

CREATE TABLE `cours` (
  `id` int(11) NOT NULL,
  `nomCours` varchar(150) NOT NULL,
  `code` varchar(20) NOT NULL,
  `description` text DEFAULT NULL,
  `professeurId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `cours`
--

INSERT INTO `cours` (`id`, `nomCours`, `code`, `description`, `professeurId`) VALUES
(1, 'Mathématiques Avancées', 'MATH301', 'Cours de mathématiques niveau L3', 2),
(2, 'Programmation Java', 'INFO201', 'Introduction à la programmation orientée objet', 2);

-- --------------------------------------------------------

--
-- Structure de la table `emargement`
--

CREATE TABLE `emargement` (
  `id` int(11) NOT NULL,
  `seanceId` int(11) NOT NULL,
  `etudiantId` int(11) NOT NULL,
  `heureEmargement` timestamp NOT NULL DEFAULT current_timestamp(),
  `present` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `etudiant`
--

CREATE TABLE `etudiant` (
  `id` int(11) NOT NULL,
  `numeroEtudiant` varchar(20) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `email` varchar(150) DEFAULT NULL,
  `utilisateurId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `etudiant`
--

INSERT INTO `etudiant` (`id`, `numeroEtudiant`, `nom`, `prenom`, `email`, `utilisateurId`) VALUES
(1, '20231001', 'GALVAN', 'Tom', 'tom.galvan@edu.ece.fr', 3),
(2, '20231002', 'Bernard', 'Lucas', 'lucas.bernard@edu.ece.fr', 4),
(3, '20231003', 'Dubois', 'Emma', 'emma.dubois@edu.ece.fr', 5);

-- --------------------------------------------------------

--
-- Structure de la table `seance`
--

CREATE TABLE `seance` (
  `id` int(11) NOT NULL,
  `coursId` int(11) NOT NULL,
  `dateSeance` date NOT NULL,
  `heureDebut` time NOT NULL,
  `heureFin` time NOT NULL,
  `codeEmargement` varchar(10) DEFAULT NULL,
  `codeActif` tinyint(1) DEFAULT 0,
  `dateCreationCode` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `seance`
--

INSERT INTO `seance` (`id`, `coursId`, `dateSeance`, `heureDebut`, `heureFin`, `codeEmargement`, `codeActif`, `dateCreationCode`) VALUES
(1, 1, '2025-12-03', '08:00:00', '10:00:00', NULL, 0, '2025-12-03 08:09:06'),
(2, 2, '2025-12-03', '14:00:00', '16:00:00', NULL, 0, '2025-12-03 08:09:06');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id` int(11) NOT NULL,
  `login` varchar(50) NOT NULL,
  `motDePasseHashed` varchar(255) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `role` enum('ADMIN','PROFESSEUR','ETUDIANT') NOT NULL,
  `dateCreation` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `login`, `motDePasseHashed`, `nom`, `prenom`, `role`, `dateCreation`) VALUES
(1, 'admin', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Admin', 'Système', 'ADMIN', '2025-12-03 08:09:06'),
(2, 'prof.dupont', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Dupont', 'Jean', 'PROFESSEUR', '2025-12-03 08:09:06'),
(3, '20231001', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Martin', 'Sophie', 'ETUDIANT', '2025-12-03 08:09:06'),
(4, '20231002', '$2a$10$dummyHashForDemo', 'Bernard', 'Lucas', 'ETUDIANT', '2025-12-03 08:09:06'),
(5, '20231003', '$2a$10$dummyHashForDemo', 'Dubois', 'Emma', 'ETUDIANT', '2025-12-03 08:09:06');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `cours`
--
ALTER TABLE `cours`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `code` (`code`),
  ADD KEY `professeurId` (`professeurId`);

--
-- Index pour la table `emargement`
--
ALTER TABLE `emargement`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_presence` (`seanceId`,`etudiantId`),
  ADD KEY `etudiantId` (`etudiantId`);

--
-- Index pour la table `etudiant`
--
ALTER TABLE `etudiant`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numeroEtudiant` (`numeroEtudiant`),
  ADD KEY `utilisateurId` (`utilisateurId`);

--
-- Index pour la table `seance`
--
ALTER TABLE `seance`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `codeEmargement` (`codeEmargement`),
  ADD KEY `coursId` (`coursId`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login` (`login`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `cours`
--
ALTER TABLE `cours`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `emargement`
--
ALTER TABLE `emargement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `etudiant`
--
ALTER TABLE `etudiant`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `seance`
--
ALTER TABLE `seance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `cours`
--
ALTER TABLE `cours`
  ADD CONSTRAINT `cours_ibfk_1` FOREIGN KEY (`professeurId`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `emargement`
--
ALTER TABLE `emargement`
  ADD CONSTRAINT `emargement_ibfk_1` FOREIGN KEY (`seanceId`) REFERENCES `seance` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `emargement_ibfk_2` FOREIGN KEY (`etudiantId`) REFERENCES `etudiant` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `etudiant`
--
ALTER TABLE `etudiant`
  ADD CONSTRAINT `etudiant_ibfk_1` FOREIGN KEY (`utilisateurId`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `seance`
--
ALTER TABLE `seance`
  ADD CONSTRAINT `seance_ibfk_1` FOREIGN KEY (`coursId`) REFERENCES `cours` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
