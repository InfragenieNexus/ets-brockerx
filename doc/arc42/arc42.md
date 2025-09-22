# Calculatrice - Documentation d'Architecture

Ce document, basé sur le modèle arc42, décrit une application de courtage pour
la compagnie BrockerX dans le cadre du
LOG430.

## 1. Introduction et Objectifs

### Panorama des exigences

L'application « BrockerX » est une plateforme de courtage en ligne permettant
aux investisseurs de:

- Créer et gérer un compte utilisateur
- Authentifier et sécuriser l'accès
- Placer, consulter et annuler des ordres d'achat/vente
- Consulter un portefeuille et l'historique des transactions

L'objectif de la phase 1 est de concevoir et livrer une architecture
monolithique évolutive, tout en préparant une
transition future vers une architecture Micro-service et pour finalement ce
penché vers une approche orientée événements

### Objectifs qualité

| Priorité | Objectif qualité          | Scénario concret                                                                                                            |
|----------|---------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| 1        | **Performance**           | Latence P95 ≤ 500 ms (monolithe), ≤ 250 ms (microservices), ≤ 100 ms (event-driven). Débit ≥ 1200 ordres/s en phase finale. |
| 2        | **Disponibilité**         | 90 % (mono) → 95,5 % (MS) → 99,9 % (event-driven). Service accessible même en cas de panne d’un composant.                  |
| 3        | **Observabilité**         | Logs structurés + métriques (4 Golden Signals) dès phase 2, traces distribuées à partir de la phase 3.                      |
| 4        | **Sécurité / Conformité** | MFA obligatoire pour login, contrôles KYC/AML, idempotence pour ordres, audit trail immuable.                               |
| 5        | **Maintenabilité**        | Architecture modulaire permettant le passage progressif mono → microservices → event-driven sans réécriture complète.       |

### Parties prenantes (Stakeholders)

- **SoftWare engineer** : Apprendre les outils de développement modernes et les
  pipelines CI/CD
- **Clients** : utilisateurs via interface web
- **Opérations Back-Office**: gestion des règlements, supervision
- **Conformité/Risque** : surveillance pré- et post-trade.
- **Fournisseurs de données de marché**: cotations en temps réel.
- **Bourse externes**: simulateurs de marché pour routage d'ordres

## 2. Contraintes d'architecture

### Contraintes d’architecture

| Contrainte                 | Description                                                                                               |
|----------------------------|-----------------------------------------------------------------------------------------------------------|
| **Environnement cible**    | L’application doit être déployée sur une machine virtuelle fournie par l’école/labo.                      |
| **Conteneurisation**       | Tous les services doivent être packagés dans des conteneurs Docker pour assurer portabilité et isolation. |
| **CI/CD automatisé**       | Le déploiement doit être automatisé via un pipeline GitLab CI/CD (build, tests, déploiement).             |
| **Tests automatisés**      | Des tests unitaires et d’intégration doivent être exécutés automatiquement dans le pipeline.              |
| **Observabilité minimale** | Journaux applicatifs et monitoring basique doivent être activés dès la première version.                  |

## 3. Portée et contexte du système

# Priorisation des Cas d’Utilisation (MoSCoW)

| Priorité                   | Cas d’Utilisation (CU)                         | Description                                                               |
|----------------------------|------------------------------------------------|---------------------------------------------------------------------------|
| **Must have**              | CU01 – Inscription & Vérification d'identité   | Connexion sécurisée avec MFA                                              |
|                            | CU02 – Authentification                        | Garantir un accès sérurisé                                                |
|                            | CU03 – Portefeuille (dépot virtuel)            | Créditer le portefeuille virtuel                                          |
|                            | CU05 – Placement d'un ordre                    | Soumettre des ordres d’achat ou de vente                                  |
|                            | CU07 – Appariement interne & Exécution         | Exécution automatique des ordres                                          |
| **Should have**            | CU04 – Abonnement aux données de marché        | Accès en temps réel au cotations et carnets d'ordres                      |
|                            | CU05 – Historique transactions                 | Liste détaillée des ordres exécutés                                       |
| **Could have**             | CU06 – Modification / Annulation d'un ordre    | Flexibilité tant que l'ordre n'est pas totalement exécuté                 |
|                            | CU08 – Confirmation d'exécution & Notification | Renvoit et notifie le clien de la réussite ou l'echec de ses transactions |
| **Won’t have (this time)** | CU09 – Trading social                          | Copier les ordres d’autres utilisateurs                                   |
|                            | CU10 – Intégration crypto                      | Achat/vente de crypto-monnaies                                            |

### Contexte métier

![Activity](
activity.png)

Le système permet aux utilisateurs de :

- Inscription/login utilisateur (MFA optionnelle), simulation de financement.
- Consultation portefeuille et solde.
- Abonnement aux données de marché (liste de titres).
- Placement, modification et annulation d’ordres ; carnet d’ordres par symbole.
- Routage vers moteur interne (phase 1), puis vers bourse externe simulée
  (phase 2+).
- Mise à jour des positions suite aux exécutions ; confirmations et
  notifications.
- Rapports quotidiens (EOD snapshot), traçabilité complète (audit trail).

### Contexte technique

| Composant                   | Technologie / Détail                                                                     |
|-----------------------------|------------------------------------------------------------------------------------------|
| **Application**             | Spring Boot (Java 17+) avec JSP pour le front web                                        |
| **Base de données**         | PostgreSQL 15, accès via JPA/Hibernate                                                   |
| **Sécurité**                | Authentification email/mot de passe, OTP pour vérification, MFA TOTP optionnel           |
| **Conteneurisation**        | Docker + Docker Compose sur VM Ubuntu Linux self-hosted                                  |
| **CI/CD**                   | GitHub Actions pour build, tests et déploiement                                          |
| **Gestion des dépendances** | Maven (pom.xml) pour librairies Java (Spring Security, JJWT, Google Authenticator, etc.) |
| **Monitoring / Logs**       | Spring Boot Actuator (health, métriques)                                                 |
| **Front-end**               | JSP + JSTL avec layout global pour header/footer                                         |
| **Sessions / État**         | Session HTTP pour gérer l’utilisateur connecté et MFA                                    |
| **Environnement**           | VM Ubuntu Linux, ports exposés 8080 (app) et 5432 (DB)                                   |

## 4. Stratégie de solution

| Problème                        | Approche de solution                                  |
|---------------------------------|-------------------------------------------------------|
| **Environnement reproductible** | Utilisation de Docker et environnement virtuel Python |
| **Qualité du code**             | Tests automatisés avec pytest                         |
| **Déploiement automatisé**      | Pipeline CI/CD avec GitLab                            |
| **Surveillance des ressources** | Commandes système (top, free, df)                     |

## 5. Vue des blocs de construction

![Class](class.png)

## 6. Vue d'exécution

![Use Case](use_case.png)

## 7. Vue de déploiement

![Deployment](deployment.png)

## 8. Concepts transversaux

- Conteneurisation avec Docker
- Tests automatisés avec pytest
- Pipeline CI/CD avec GitLab
- Déploiement via SSH
- Surveillance des ressources système

## 9. Décisions d'architecture

Veuillez consulter le fichier `/docs/adr/adr001.md`.

## 10. Exigences qualité

### Testabilité

- Tests automatisés pour toutes les fonctions mathématiques
- Exécution des tests dans le pipeline CI/CD
- Signalement claire des erreurs par pytest

### Déployabilité

- Pipeline automatisé GitLab avec étapes setup et checkout
- Déploiement automatique via SSH après succès des tests
- Vérification des ressources système sur le serveur cible

### Maintenabilité

- Code simple et bien documenté
- Structure modulaire pour faciliter l'évolution
- Conventions de nommage cohérentes

## 11. Risques et dettes techniques

Non applicable pour cette application.

## 12. Glossaire

| Terme     | Définition                                                                                                                 |
|-----------|----------------------------------------------------------------------------------------------------------------------------|
| **CI/CD** | Continuous Integration/Continuous Deployment : pratiques d'automatisation du développement et déploiement des applications |
| **SSH**   | Secure Shell : protocole de communication sécurisé pour l'accès à distance                                                 |