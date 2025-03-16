# Projet d'Application Web - ENSEEIHT

**Ce document permet d'expliquer les choix des technologies utilisées et de la construction de l'environnement de développement**

### Prérequis
- linux, windows (via WSL)
- java 17 (minimum)
- npm
- nodejs
- docker avec docker compose
- vite
- make (pas nécessaire, mais scripts utiles lors du développement)

### Versions des frameworks
- spring boot 3
- vue 3

## Structure du projet

(TODO : photo fichiers projet avec explications de quel fichier sert à quoi)

/project-name
&emsp; /vue-app (frontend)
&emsp; /spring-boot-api (backend)

**.env.example** : Fichier contenant un exemple de fichier de configuration du projet. Il suffit de copier ce fichier et le renommer ".env" pour activer les variables d'environnement.

TODO : description autres fichiers

## Technologies : général

**docker et docker compose** : Lancer des containers (similaire à des VM mais moins lourd). On l'utilise ici principalement pour lancer un container qui fait tourner la base de données PostgreSQL. Ca évite à chacun d'installer un serveur de base de données sur son pc et ça permet d'avoir un environnement similaire pour tout le monde et qui se rapproche d'une mise en production (éviter les bugs du type "nan mais ça marche sur ma machine...").

#### Utilitaires :
Pour développer, on peut utiliser son IDE préféré et leurs fonctionnalités intégrées pour du Java/Spring Boot/Vue. En revanche tout a été fait pour avoir un environnement de développement très propre avec juste des lignes de commandes.

Pour tester seulement l'API Rest (lorsque le côté front correspondant n'est pas encore codé par exemple), on peut utiliser Postman ou curl (lignes de commandes) pour envoyer des requêtes vers l'API.

Pour interagir directement avec la base de données PostgreSQL, (vérifier que les données ont bien été enregistrées pendant des tests lors du développement, ou vérifier que les tables sont correctes ou autre) on peut utiliser un client comme DBeaver, pgAdmin ou l'outil en ligne de commande psql. Cependant, pour éviter ces installations, un script dans le makefile permet de lancer directement depuis votre terminal des commandes SQL vers le docker contenant la base de données et recevoir la réponse.

## Technologies utilisées pour vue-app

**nodejs** : Environnement d'exécution pour JavaScript (un peu comme un jre pour Java)
**npm** : Gestionnaire de dépendances pour des projets javascript sur nodejs (permet aussi de spécifier des scripts dans le package.json pour automatiser certaines tâches)
**vite** : Outil de développement web qui permet des build rapide, intègre un serveur de développement et reload en direct l'application lors de changements

#### Packages

**axios** : gérer requêtes http vers l'api (en gros c'est le controler qui sert à définir les get et post)


## Technologies utilisées pour spring-boot-api

Pour initialiser le projet spring-boot-api, on a utilisé Spring Initializr : https://start.spring.io/

**gradle** : gestion des dépendances pour un projet Java, aussi utilisé pour lancer des scripts de build. Gradle wrapper (commande : gradlew) permet d'utiliser gradle spécifiquement sur ce projet, pour éviter à tout le monde d'installer gradle sur sa machine et être sûr d'avoir la même version

#### Dépendances

- **Spring Boot DevTools** : meilleure experience de développement (restart rapide de l'api, reload de l'api en temps réel, configurations...)
- **Spring Web** : pour développer des app web RESTful avec Spring MVC et un tomcat intégré
- **Spring Data JPA** : persistence de données dans des bases SQL avec Hibernate
- **PostgreSQL Driver** : un driver JDBC qui permet au programme Java de se connecter à une base de données SQL


## Technologie de base de données : PostgreSQL

Pourquoi PostgreSQL et pas HSQLDB ?

- plus utilisé dans l'industrie et plus de documentation
- plus proche d'un environnement de production
- plus de fonctionnalités si besoin (gérer fichiers json, xml par exemple)
- HSQLDB est plus léger mais avec docker on règle ce problème
- bien entendu l'API JDBC vu en cours fonctionne sur PostgreSQL

Dans le projet on trouve un dossier db. Il contient des scripts pour initialiser la base de donnée

## Parallèles avec les technologies utilisées en cours et TP

#### Backend :
API Rest (Web Service) avec données JPA.
Tout ce qui change par rapport au cours c'est qu'on utilise Spring Boot au lieu de juste Spring. Donc on a plus de serveur tomcat à installer, Spring Boot gère un tomcat intégré.
Le reste est globalement pareil, mise à part qu'avec Spring Boot il y a moins de code à écrire et une configuration simplifié.

#### Frontend :

- langage : Java // JavaScript
- pages web gérées par : Servlet avec JSP // Vue
- lien avec l'api (controler): RestEasy // axios
- serveur de déploiement : tomcat // nodejs

#### Base de donnée :
PostgreSQL au lieu de HSQLDB.


## Bibliographie
Makefiles :
https://rosszurowski.com/log/2022/makefiles
https://blog.bjorn-eric.com/making-a-makefile

Spring :
https://start.spring.io/

TODO : ajouter autres liens
