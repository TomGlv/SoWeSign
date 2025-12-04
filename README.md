# SoWeSign - Application d'Émargement Électronique

## 🎓 Description du Projet

**SoWeSign** est une application de gestion d'émargement développée en **JavaFX** (pour l'interface graphique de bureau) et utilisant **MySQL** pour la persistance des données.

L'objectif principal du système est de permettre aux professeurs de générer des codes uniques pour l'émargement des séances de cours, tandis que les étudiants peuvent confirmer leur présence via ces codes. Le système est conçu pour gérer trois rôles utilisateurs distincts : Administrateur, Professeur et Étudiant.

---

## 💻 Technologies Utilisées

* **Langage :** Java 21
* **Interface Graphique :** JavaFX 21
* **Build Tool :** Apache Maven
* **Base de Données :** MySQL 8+
* **Sécurité :** Spring Security Crypto (pour le hachage BCrypt des mots de passe)

---

## 🚀 Démarrage du Projet

### Prérequis

1.  **JDK 21** (Java Development Kit)
2.  **Apache Maven**
3.  **Serveur MySQL** (avec les identifiants `root` / `password` ou ajustez les fichiers DAO)

### 1. Configuration de la Base de Données

1.  Créez la base de données : `emargement_db`.
2.  Exécutez les scripts SQL de création des tables et d'insertion des données initiales.

**(Si vous utilisez les données initiales du projet, le mot de passe de tous les utilisateurs est `password`.)**

### 2. Cloner et Importer le Projet

```bash
git clone https://github.com/TomGlv/SoWeSign.git
cd SoWeSign
