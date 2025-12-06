-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 06, 2025 at 02:26 PM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `emargement_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `cours`
--

CREATE TABLE `cours` (
  `id` int(11) NOT NULL,
  `nomCours` varchar(150) NOT NULL,
  `code` varchar(20) NOT NULL,
  `description` text DEFAULT NULL,
  `professeurId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cours`
--

INSERT INTO `cours` (`id`, `nomCours`, `code`, `description`, `professeurId`) VALUES
(1, 'Programmation Java', 'JAVA101', 'Introduction à la programmation Java', 2),
(2, 'Maths Discrètes', 'MATH201', 'Mathématiques discrètes', 3);

-- --------------------------------------------------------

--
-- Table structure for table `emargement`
--

CREATE TABLE `emargement` (
  `id` int(11) NOT NULL,
  `seanceId` int(11) NOT NULL,
  `etudiantId` int(11) NOT NULL,
  `dateHeureEmargement` timestamp NOT NULL DEFAULT current_timestamp(),
  `present` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `etudiant`
--

CREATE TABLE `etudiant` (
  `id` int(11) NOT NULL,
  `numeroEtudiant` varchar(20) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `email` varchar(150) DEFAULT NULL,
  `utilisateurId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `etudiant`
--

INSERT INTO `etudiant` (`id`, `numeroEtudiant`, `nom`, `prenom`, `email`, `utilisateurId`) VALUES
(1, '20231001', 'Galvan', 'Tom', 'tom.galvan@edu.ece.fr', 5),
(2, '20231002', 'Dubois', 'Emma', 'emma.dubois@edu.ece.fr', 6),
(3, '20231003', 'Moreau', 'Paul', 'paul.moreau@edu.ece.fr', 7);

-- --------------------------------------------------------

--
-- Table structure for table `prof`
--

CREATE TABLE `prof` (
  `id` int(11) NOT NULL,
  `utilisateurId` int(11) NOT NULL,
  `departement` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `prof`
--

INSERT INTO `prof` (`id`, `utilisateurId`, `departement`) VALUES
(1, 2, 'Informatique'),
(2, 3, 'Mathématiques'),
(3, 4, 'Electronique');

-- --------------------------------------------------------

--
-- Table structure for table `seance`
--

CREATE TABLE `seance` (
  `id` int(11) NOT NULL,
  `coursId` int(11) NOT NULL,
  `dateSeance` date NOT NULL,
  `heureDebut` time NOT NULL,
  `heureFin` time NOT NULL,
  `codeEmargement` varchar(10) DEFAULT NULL,
  `codeActif` tinyint(1) DEFAULT 0,
  `dateCreationCode` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `codeEmargementExpire` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `seance`
--

INSERT INTO `seance` (`id`, `coursId`, `dateSeance`, `heureDebut`, `heureFin`, `codeEmargement`, `codeActif`, `dateCreationCode`, `codeEmargementExpire`) VALUES
(1, 1, '2025-12-10', '08:30:00', '10:30:00', NULL, 0, '2025-12-06 13:18:45', NULL),
(2, 2, '2025-12-11', '14:00:00', '16:00:00', NULL, 0, '2025-12-06 13:18:45', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id` int(11) NOT NULL,
  `login` varchar(150) NOT NULL,
  `motDePasseHashed` varchar(255) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `role` enum('ADMIN','PROFESSEUR','ETUDIANT') NOT NULL,
  `dateCreation` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `login`, `motDePasseHashed`, `nom`, `prenom`, `role`, `dateCreation`) VALUES
(1, 'admin@system.com', '<HASH_ADMIN>', 'System', 'Admin', 'ADMIN', '2025-12-06 13:17:36'),
(2, 'jean.dupont.prof@edu.ece.fr', '$2a$10$ew8vfSJZgoaazVwD.qHx7.wVScz03XukvZERkUPzWUw8hyH5/eulS', 'Dupont', 'Jean', 'PROFESSEUR', '2025-12-06 13:17:36'),
(3, 'sarah.martin.prof@edu.ece.fr', '$2a$10$ew8vfSJZgoaazVwD.qHx7.wVScz03XukvZERkUPzWUw8hyH5/eulS', 'Martin', 'Sarah', 'PROFESSEUR', '2025-12-06 13:17:36'),
(4, 'lucas.bernard.prof@edu.ece.fr', '$2a$10$ew8vfSJZgoaazVwD.qHx7.wVScz03XukvZERkUPzWUw8hyH5/eulS', 'Bernard', 'Lucas', 'PROFESSEUR', '2025-12-06 13:17:36'),
(5, 'tom.galvan@edu.ece.fr', '$2a$10$ew8vfSJZgoaazVwD.qHx7.wVScz03XukvZERkUPzWUw8hyH5/eulS', 'Galvan', 'Tom', 'ETUDIANT', '2025-12-06 13:17:36'),
(6, 'emma.dubois@edu.ece.fr', '$2a$10$ew8vfSJZgoaazVwD.qHx7.wVScz03XukvZERkUPzWUw8hyH5/eulS', 'Dubois', 'Emma', 'ETUDIANT', '2025-12-06 13:17:36'),
(7, 'paul.moreau@edu.ece.fr', '$2a$10$ew8vfSJZgoaazVwD.qHx7.wVScz03XukvZERkUPzWUw8hyH5/eulS', 'Moreau', 'Paul', 'ETUDIANT', '2025-12-06 13:17:36');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cours`
--
ALTER TABLE `cours`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_code` (`code`),
  ADD KEY `idx_professeurId` (`professeurId`);

--
-- Indexes for table `emargement`
--
ALTER TABLE `emargement`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_presence` (`seanceId`,`etudiantId`),
  ADD KEY `idx_etudiantId` (`etudiantId`);

--
-- Indexes for table `etudiant`
--
ALTER TABLE `etudiant`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_numeroEtudiant` (`numeroEtudiant`),
  ADD KEY `idx_utilisateurId` (`utilisateurId`);

--
-- Indexes for table `prof`
--
ALTER TABLE `prof`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_prof_user` (`utilisateurId`);

--
-- Indexes for table `seance`
--
ALTER TABLE `seance`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_codeEmargement` (`codeEmargement`),
  ADD KEY `idx_coursId` (`coursId`);

--
-- Indexes for table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_login` (`login`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cours`
--
ALTER TABLE `cours`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `emargement`
--
ALTER TABLE `emargement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `etudiant`
--
ALTER TABLE `etudiant`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `prof`
--
ALTER TABLE `prof`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `seance`
--
ALTER TABLE `seance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cours`
--
ALTER TABLE `cours`
  ADD CONSTRAINT `fk_cours_prof` FOREIGN KEY (`professeurId`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `emargement`
--
ALTER TABLE `emargement`
  ADD CONSTRAINT `fk_emargement_etudiant` FOREIGN KEY (`etudiantId`) REFERENCES `etudiant` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_emargement_seance` FOREIGN KEY (`seanceId`) REFERENCES `seance` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `etudiant`
--
ALTER TABLE `etudiant`
  ADD CONSTRAINT `fk_etudiant_user` FOREIGN KEY (`utilisateurId`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `prof`
--
ALTER TABLE `prof`
  ADD CONSTRAINT `fk_prof_user` FOREIGN KEY (`utilisateurId`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `seance`
--
ALTER TABLE `seance`
  ADD CONSTRAINT `fk_seance_cours` FOREIGN KEY (`coursId`) REFERENCES `cours` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
