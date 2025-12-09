-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mar. 09 déc. 2025 à 14:07
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

--
-- Déchargement des données de la table `emargement`
--

INSERT INTO `emargement` (`id`, `seanceId`, `etudiantId`, `heureEmargement`, `present`) VALUES
(739, 9, 11, '2025-12-09 13:01:33', 1),
(740, 9, 24, '2025-12-09 13:01:33', 1),
(741, 9, 13, '2025-12-09 13:01:34', 1),
(742, 9, 33, '2025-12-09 13:01:35', 1),
(743, 9, 9, '2025-12-09 13:01:36', 1),
(744, 9, 17, '2025-12-09 13:01:36', 1),
(746, 9, 5, '2025-12-09 13:04:45', 1),
(747, 9, 4, '2025-12-09 13:04:45', 1),
(748, 9, 23, '2025-12-09 13:04:46', 1),
(749, 9, 12, '2025-12-09 13:04:47', 1),
(750, 9, 1, '2025-12-09 13:04:47', 1),
(751, 9, 27, '2025-12-09 13:04:47', 1),
(752, 9, 31, '2025-12-09 13:04:48', 1),
(753, 9, 10, '2025-12-09 13:04:48', 1);

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
(1, '20231001', 'Martin', 'Sophie', 'sophie.martin@edu.ece.fr', 3),
(2, '20231002', 'Bernard', 'Lucas', 'lucas.bernard@edu.ece.fr', 4),
(3, '20231003', 'Dubois', 'Emma', 'emma.dubois@edu.ece.fr', 5),
(4, '20231004', 'Gauthier', 'Alice', 'alice.gauthier@edu.fr', 6),
(5, '20231005', 'Lambert', 'Benoit', 'benoit.lambert@edu.fr', 7),
(6, '20231006', 'Petit', 'Chloé', 'chloe.petit@edu.fr', 8),
(7, '20231007', 'Rousseau', 'David', 'david.rousseau@edu.fr', 9),
(8, '20231008', 'Roux', 'Elisa', 'elisa.roux@edu.fr', 10),
(9, '20231009', 'Riviere', 'François', 'francois.riviere@edu.fr', 11),
(10, '20231010', 'Moreau', 'Garance', 'garance.moreau@edu.fr', 12),
(11, '20231011', 'Lemoine', 'Hugo', 'hugo.lemoine@edu.fr', 13),
(12, '20231012', 'Leroy', 'Inès', 'ines.leroy@edu.fr', 14),
(13, '20231013', 'Picard', 'Jules', 'jules.picard@edu.fr', 15),
(14, '20231014', 'Perrin', 'Kelly', 'kelly.perrin@edu.fr', 16),
(15, '20231015', 'Rocher', 'Liam', 'liam.rocher@edu.fr', 17),
(16, '20231016', 'Roger', 'Manon', 'manon.roger@edu.fr', 18),
(17, '20231017', 'Ramos', 'Noah', 'noah.ramos@edu.fr', 19),
(18, '20231018', 'Rousseau', 'Olivia', 'olivia.rousseau@edu.fr', 20),
(19, '20231019', 'Pierre', 'Paul', 'paul.pierre@edu.fr', 21),
(20, '20231020', 'Garcia', 'Quentin', 'quentin.garcia@edu.fr', 22),
(21, '20231021', 'Bernard', 'Romain', 'romain.bernard@edu.fr', 23),
(22, '20231022', 'Dubois', 'Sara', 'sara.dubois@edu.fr', 24),
(23, '20231023', 'Lefevre', 'Tom', 'tom.lefevre@edu.fr', 25),
(24, '20231024', 'Moreau', 'Ugo', 'ugo.moreau@edu.fr', 26),
(25, '20231025', 'Dupont', 'Valentin', 'valentin.dupont@edu.fr', 27),
(26, '20231026', 'Durand', 'Wendy', 'wendy.durand@edu.fr', 28),
(27, '20231027', 'Leroy', 'Xavier', 'xavier.leroy@edu.fr', 29),
(28, '20231028', 'Petit', 'Yasmine', 'yasmine.petit@edu.fr', 30),
(29, '20231029', 'Thomas', 'Zacharie', 'zacharie.thomas@edu.fr', 31),
(30, '20231030', 'Simon', 'Axelle', 'axelle.simon@edu.fr', 32),
(31, '20231031', 'Michel', 'Baptiste', 'baptiste.michel@edu.fr', 33),
(32, '20231032', 'David', 'Célia', 'celia.david@edu.fr', 34),
(33, '20231033', 'Robert', 'Dylan', 'dylan.robert@edu.fr', 35);

-- --------------------------------------------------------

--
-- Structure de la table `etudiant_cours`
--

CREATE TABLE `etudiant_cours` (
  `etudiantId` int(11) NOT NULL,
  `coursId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `etudiant_cours`
--

INSERT INTO `etudiant_cours` (`etudiantId`, `coursId`) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 2),
(3, 1),
(3, 2),
(4, 1),
(4, 2),
(5, 1),
(5, 2),
(6, 1),
(6, 2),
(7, 1),
(7, 2),
(8, 1),
(8, 2),
(9, 1),
(9, 2),
(10, 1),
(10, 2),
(11, 1),
(11, 2),
(12, 1),
(12, 2),
(13, 1),
(13, 2),
(14, 1),
(14, 2),
(15, 1),
(15, 2),
(16, 1),
(16, 2),
(17, 1),
(17, 2),
(18, 1),
(18, 2),
(19, 1),
(19, 2),
(20, 1),
(20, 2),
(21, 1),
(21, 2),
(22, 1),
(22, 2),
(23, 1),
(23, 2),
(24, 1),
(24, 2),
(25, 1),
(25, 2),
(26, 1),
(26, 2),
(27, 1),
(27, 2),
(28, 1),
(28, 2),
(29, 1),
(29, 2),
(30, 1),
(30, 2),
(31, 1),
(31, 2),
(32, 1),
(32, 2),
(33, 1),
(33, 2);

-- --------------------------------------------------------

--
-- Structure de la table `professeur`
--

CREATE TABLE `professeur` (
  `id` int(11) NOT NULL,
  `utilisateur_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `professeur`
--

INSERT INTO `professeur` (`id`, `utilisateur_id`) VALUES
(1, 2);

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
  `salle` varchar(50) DEFAULT 'Non spécifiée',
  `codeEmargement` varchar(10) DEFAULT NULL,
  `codeEmargementExpire` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `seance`
--

INSERT INTO `seance` (`id`, `coursId`, `dateSeance`, `heureDebut`, `heureFin`, `salle`, `codeEmargement`, `codeEmargementExpire`) VALUES
(1, 1, '2025-12-03', '08:00:00', '10:00:00', 'Non spécifiée', 'MZNBIN', '2025-12-09 14:00:30'),
(2, 2, '2025-12-03', '14:00:00', '16:00:00', 'Non spécifiée', '8H7AYG', '2025-12-05 14:33:30'),
(3, 1, '2025-12-09', '10:00:00', '12:00:00', 'B201', NULL, NULL),
(4, 2, '2025-12-09', '16:00:00', '18:00:00', 'A105', NULL, NULL),
(5, 1, '2025-12-10', '08:00:00', '10:00:00', 'B201', NULL, NULL),
(6, 2, '2025-12-10', '10:00:00', '12:00:00', 'A105', NULL, NULL),
(7, 1, '2025-12-11', '14:00:00', '16:00:00', 'B201', NULL, NULL),
(8, 2, '2025-12-11', '16:00:00', '18:00:00', 'A105', NULL, NULL),
(9, 1, '2025-12-12', '08:00:00', '10:00:00', 'B201', '8C3BO2', '2025-12-09 14:14:50'),
(10, 2, '2025-12-12', '14:00:00', '16:00:00', 'A105', NULL, NULL);

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
(4, '20231002', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Bernard', 'Lucas', 'ETUDIANT', '2025-12-03 08:09:06'),
(5, '20231003', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Dubois', 'Emma', 'ETUDIANT', '2025-12-03 08:09:06'),
(6, '20231004', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Gauthier', 'Alice', 'ETUDIANT', '2025-12-09 12:59:46'),
(7, '20231005', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Lambert', 'Benoit', 'ETUDIANT', '2025-12-09 12:59:46'),
(8, '20231006', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Petit', 'Chloé', 'ETUDIANT', '2025-12-09 12:59:46'),
(9, '20231007', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Rousseau', 'David', 'ETUDIANT', '2025-12-09 12:59:46'),
(10, '20231008', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Roux', 'Elisa', 'ETUDIANT', '2025-12-09 12:59:46'),
(11, '20231009', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Riviere', 'François', 'ETUDIANT', '2025-12-09 12:59:46'),
(12, '20231010', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Moreau', 'Garance', 'ETUDIANT', '2025-12-09 12:59:46'),
(13, '20231011', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Lemoine', 'Hugo', 'ETUDIANT', '2025-12-09 12:59:46'),
(14, '20231012', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Leroy', 'Inès', 'ETUDIANT', '2025-12-09 12:59:46'),
(15, '20231013', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Picard', 'Jules', 'ETUDIANT', '2025-12-09 12:59:46'),
(16, '20231014', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Perrin', 'Kelly', 'ETUDIANT', '2025-12-09 12:59:46'),
(17, '20231015', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Rocher', 'Liam', 'ETUDIANT', '2025-12-09 12:59:46'),
(18, '20231016', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Roger', 'Manon', 'ETUDIANT', '2025-12-09 12:59:46'),
(19, '20231017', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Ramos', 'Noah', 'ETUDIANT', '2025-12-09 12:59:46'),
(20, '20231018', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Rousseau', 'Olivia', 'ETUDIANT', '2025-12-09 12:59:46'),
(21, '20231019', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Pierre', 'Paul', 'ETUDIANT', '2025-12-09 12:59:46'),
(22, '20231020', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Garcia', 'Quentin', 'ETUDIANT', '2025-12-09 12:59:46'),
(23, '20231021', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Bernard', 'Romain', 'ETUDIANT', '2025-12-09 12:59:46'),
(24, '20231022', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Dubois', 'Sara', 'ETUDIANT', '2025-12-09 12:59:46'),
(25, '20231023', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Lefevre', 'Tom', 'ETUDIANT', '2025-12-09 12:59:46'),
(26, '20231024', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Moreau', 'Ugo', 'ETUDIANT', '2025-12-09 12:59:46'),
(27, '20231025', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Dupont', 'Valentin', 'ETUDIANT', '2025-12-09 12:59:46'),
(28, '20231026', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Durand', 'Wendy', 'ETUDIANT', '2025-12-09 12:59:46'),
(29, '20231027', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Leroy', 'Xavier', 'ETUDIANT', '2025-12-09 12:59:46'),
(30, '20231028', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Petit', 'Yasmine', 'ETUDIANT', '2025-12-09 12:59:46'),
(31, '20231029', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Thomas', 'Zacharie', 'ETUDIANT', '2025-12-09 12:59:46'),
(32, '20231030', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Simon', 'Axelle', 'ETUDIANT', '2025-12-09 12:59:46'),
(33, '20231031', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Michel', 'Baptiste', 'ETUDIANT', '2025-12-09 12:59:46'),
(34, '20231032', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'David', 'Célia', 'ETUDIANT', '2025-12-09 12:59:46'),
(35, '20231033', '$2a$10$CCNvEoIJugmFwI9qGySm4uSOlWygGWJ/1gd2/Q7BHMaO7DyG6Okx.', 'Robert', 'Dylan', 'ETUDIANT', '2025-12-09 12:59:46');

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
  ADD UNIQUE KEY `UQ_Emargement` (`seanceId`,`etudiantId`),
  ADD KEY `etudiantId` (`etudiantId`);

--
-- Index pour la table `etudiant`
--
ALTER TABLE `etudiant`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numeroEtudiant` (`numeroEtudiant`),
  ADD KEY `utilisateurId` (`utilisateurId`);

--
-- Index pour la table `etudiant_cours`
--
ALTER TABLE `etudiant_cours`
  ADD PRIMARY KEY (`etudiantId`,`coursId`),
  ADD KEY `coursId` (`coursId`);

--
-- Index pour la table `professeur`
--
ALTER TABLE `professeur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_utilisateur_id` (`utilisateur_id`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=754;

--
-- AUTO_INCREMENT pour la table `etudiant`
--
ALTER TABLE `etudiant`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT pour la table `professeur`
--
ALTER TABLE `professeur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `seance`
--
ALTER TABLE `seance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

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
-- Contraintes pour la table `etudiant_cours`
--
ALTER TABLE `etudiant_cours`
  ADD CONSTRAINT `etudiant_cours_ibfk_1` FOREIGN KEY (`etudiantId`) REFERENCES `etudiant` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `etudiant_cours_ibfk_2` FOREIGN KEY (`coursId`) REFERENCES `cours` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `professeur`
--
ALTER TABLE `professeur`
  ADD CONSTRAINT `fk_professeur_utilisateur` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `seance`
--
ALTER TABLE `seance`
  ADD CONSTRAINT `seance_ibfk_1` FOREIGN KEY (`coursId`) REFERENCES `cours` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
