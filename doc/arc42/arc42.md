# Calculatrice - Documentation d'Architecture
Ce document, basé sur le modèle arc42, décrit une application de courtage pour la compagnie BrockerX dans le cadre du 
LOG430.

## 1. Introduction et Objectifs

### Panorama des exigences
L'application « BrockerX » est une plateforme de courtage en ligne permettant aux investisseurs de: 
- Créer et gérer un compte utilisateur
- Authentifier et sécuriser l'accès 
- Placer, consulter et annuler des ordres d'achat/vente
- Consulter un portefeuille et l'historique des transactions

L'objectif de la phase 1 est de concevoir et livrer une architecture monolithique évolutive, tout en préparant une
transition future vers une architecture Micro-service et pour finalement ce penché vers une approche orientée événements

### Objectifs qualité
| Priorité | Objectif qualité | Scénario |
|----------|------------------|----------|
| 1 | **Testabilité** | Tests automatisés avec pytest pour toutes les fonctions |
| 2 | **Déployabilité** | Pipeline CI/CD automatisé avec GitLab |
| 3 | **Maintenabilité** | Code simple et bien structuré pour faciliter l'évolution |

### Parties prenantes (Stakeholders)
- **SoftWare engineer** : Apprendre les outils de développement modernes et les pipelines CI/CD
- **Clients** : utilisateurs via interface web
- **Opérations Back-Office**: gestion des règlements, supervision
- **Conformité/Risque** : surveillance pré- et post-trade.
- **Fournisseurs de données de marché**: cotations en temps réel.
- **Bourse externes**: simulateurs de marché pour routage d'ordres

## 2. Contraintes d'architecture

| Contrainte | Description |
|------------|-------------|
| **Technologie** | Utilisation de Python 3, Docker, pytest et GitLab CI/CD |
| **Déploiement** | Déploiement via conteneur Docker et pipeline GitLab |
| **Éducatif** | L'application doit démontrer clairement les concepts d'infrastructure et CI/CD |

## 3. Portée et contexte du système

# Priorisation des Cas d’Utilisation (MoSCoW)

| Priorité | Cas d’Utilisation (CU) | Description |
|----------|-------------------------|-------------|
| **Must have** | CU01 – Authentification | Connexion sécurisée avec MFA |
|              | CU02 – Passer un ordre | Achat/vente d’actions en bourse |
|              | CU03 – Consulter portefeuille | Solde et positions en temps réel |
|              | CU04 – Flux marché | Cotations en temps réel |
| **Should have** | CU05 – Historique transactions | Liste détaillée des ordres exécutés |
|                | CU06 – Notifications | Alertes pour exécution, risque, solde bas |
| **Could have** | CU07 – Mode sombre | Thème alternatif pour l’interface |
|                | CU08 – Export rapports | Export en PDF/Excel |
| **Won’t have (this time)** | CU09 – Trading social | Copier les ordres d’autres utilisateurs |
|                           | CU10 – Intégration crypto | Achat/vente de crypto-monnaies |


### Contexte métier
![Activity](activity.png)

Le système permet aux utilisateurs de :
- Effectuer des opérations mathématiques de base (addition, soustraction, multiplication, division)
- Exécuter l'application via ligne de commande
- Tester automatiquement les fonctionnalités

### Contexte technique
- **Application** : `calculator.py` - Script Python simple
- **Tests** : `test_calculator.py` - Tests automatisés avec pytest
- **Conteneurisation** : Docker Compose pour l'environnement de développement
- **CI/CD** : Pipeline GitLab pour tests et déploiement automatique

## 4. Stratégie de solution

| Problème | Approche de solution |
|----------|---------------------|
| **Environnement reproductible** | Utilisation de Docker et environnement virtuel Python |
| **Qualité du code** | Tests automatisés avec pytest |
| **Déploiement automatisé** | Pipeline CI/CD avec GitLab |
| **Surveillance des ressources** | Commandes système (top, free, df) |

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

| Terme | Définition |
|-------|------------|
| **CI/CD** | Continuous Integration/Continuous Deployment : pratiques d'automatisation du développement et déploiement des applications |
| **SSH** | Secure Shell : protocole de communication sécurisé pour l'accès à distance |