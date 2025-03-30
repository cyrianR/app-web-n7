# Projet d'Application Web - ENSEEIHT

**Ce document permet d'expliquer les choix des technologies utilisées et de la construction de l'environnement de développement**

### Versions des frameworks
- spring boot 3
- vue 3

## Structure du projet

### Général

**.env.example** : Fichier contenant un exemple de fichier de configuration du projet. Il suffit de copier ce fichier et le renommer ".env" pour activer les variables d'environnement.

### API Spring-Boot

### Application Vue

Je vous conseille de lire de la doc sur Vue ou des vidéos parce que c'est un framework que vous avez jamais utilisé (moi non plus d'ailleurs).

(TODO : photo fichiers projet avec explications de quel fichier sert à quoi)

/project-name
&emsp; /vue-app (frontend)
&emsp; /spring-boot-api (backend)

TODO : description autres fichiers

## Technologies : général

**docker et docker compose** : Lancer des containers (similaire à des VM mais moins lourd). On l'utilise ici principalement pour lancer un container qui fait tourner la base de données PostgreSQL. Ca évite à chacun d'installer un serveur de base de données sur son pc et ça permet d'avoir un environnement similaire pour tout le monde et qui se rapproche d'une mise en production (éviter les bugs du type "nan mais ça marche sur ma machine...").

#### Utilitaires :
Pour développer, on peut utiliser son IDE préféré et leurs fonctionnalités intégrées pour du Java/Spring Boot/Vue. En revanche tout a été fait pour avoir un environnement de développement très propre avec juste des lignes de commandes.

Pour tester seulement l'API Rest (lorsque le côté front correspondant n'est pas encore codé par exemple), on peut utiliser Postman ou curl (lignes de commandes) pour envoyer des requêtes vers l'API.

Pour interagir directement avec la base de données PostgreSQL, (vérifier que les données ont bien été enregistrées pendant des tests lors du développement, ou vérifier que les tables sont correctes ou autre) on peut utiliser un client comme DBeaver, pgAdmin ou l'outil en ligne de commande psql. Cependant, pour éviter ces installations, un script dans le makefile permet de lancer directement depuis votre terminal des commandes SQL vers le docker contenant la base de données et recevoir la réponse.

## Technologies utilisées pour vue-app

Pour initialiser le projet, on a utilisé :
```
npm create vite@latest vue-app -- --template vue-ts
```
Puis pour installer les dépendances :
```
npm install bootstrap@5.3.3 jquery popper.js axios vue-router@4
```

**nodejs** : Environnement d'exécution pour JavaScript (un peu comme un jre pour Java)

**npm** : Gestionnaire de dépendances pour des projets javascript sur nodejs (permet aussi de spécifier des scripts dans le package.json pour automatiser certaines tâches)

**vite** : Outil de développement web qui permet des build rapide, intègre un serveur de développement et reload en direct l'application lors de changements

#### Packages

**axios** : Gérer requêtes http vers l'api (en gros c'est le controler qui sert à définir les get et post).
> En particulier, voir le fichier *http-common.ts* qui initialise axios et nous mettrons les fichiers de service qui controllent les requêtes pour chacunes de nos entities dans le dossier *services/*.

**vue-router** : Ajoute un moyen simple de créer des routes pour naviguer à travers les pages web.
> En particulier, voir le fichier *router.js* et les balises *router-link* et *router-view* dans les fichiers vue du projet.

**bootstrap** : Framework très utile pour dev du front facilement, moins de css à écrire et des composants responsive rapides.


## Technologies utilisées pour spring-boot-api

Pour initialiser le projet spring-boot-api, on a utilisé Spring Initializr : https://start.spring.io/

**gradle** : gestion des dépendances pour un projet Java, aussi utilisé pour lancer des scripts de build. Gradle wrapper (commande : gradlew) permet d'utiliser gradle spécifiquement sur ce projet, pour éviter à tout le monde d'installer gradle sur sa machine et être sûr d'avoir la même version

#### Dépendances

- **Spring Boot DevTools** : meilleure experience de développement (restart rapide de l'api, reload de l'api en temps réel, configurations...)
- **Spring Web** : pour développer des app web RESTful avec Spring MVC et un tomcat intégré
- **Spring Data JPA** : persistence de données dans des bases SQL avec Hibernate
- **PostgreSQL Driver** : un driver JDBC qui permet au programme Java de se connecter à une base de données SQL
- **Faker** : permet de générer des données de test comme du texte sous différents formats


## Technologie de base de données : PostgreSQL

Pourquoi PostgreSQL et pas HSQLDB ?

- plus utilisé dans l'industrie et plus de documentation
- plus proche d'un environnement de production classique, plus robuste
- plus de fonctionnalités si besoin (gérer fichiers json, xml par exemple)
- HSQLDB est plus léger mais avec docker on règle ce problème de postgreSQL
- bien entendu l'API JDBC vu en cours fonctionne sur PostgreSQL

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

# Exemples

## Petit exemple pour l'api : HelloWorld

Dans le projet spring-boot-api, vous pouvez trouver le fichier **HelloWorld.java** (dans le package **controller**) qui map seulement un seul endpoint à /api/messages/hello et qui affiche un message. Vous pouvez y accéder par <http://localhost:8080/api/messages/hello>.

## Exemple complet pour illustrer la structure du code et l'environnement

J'ai implémenté un exemple complet pour l'api et l'app dans le projet (on pourras le retirer à terme, c'est juste pour avoir une idée de la structure du projet). Cet exemple consiste à une gestion de tutoriels. Chaque tutoriel a un titre, une description, et un booléen indiquant si il a été publié ou pas. Il y auras quelques refs dans la bibliographie vers les sites que j'ai utilisé pour cet exemple.

>Note : lire la section "Structure du projet" avant cet exemple.

#### API Spring-Boot

Pour implémenter les tutoriels, nous avons dans le projet spring-boot-api :

- **un modèle** qui définit l'entitée : **Tutorial.java** dans le package **model**. Notez bien les annotations utilisées (@Id, @Entity, ...).
<br/>
- **un repository** pour intéragir avec la base de données : **TutorialRepository.java** dans le package **repository**. Lorsque l'on extend de JpaRepository, plusieurs query de base sont disponibles sur ce repository comme "findAll", "save" etc... mais on peut aussi en spécifier d'autres selon un schéma de nom de fonctions précis comme "findByPublished" ou "findByTitleContaining" dans notre exemple. Voir la doc pour savoir comment ajouter de telles query : https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
<br/>
- **un controller** pour définir les endpoints de notre API : **TutorialController.java** dans le package **controller**. Les controller sont la porte de sortie de notre API, ce sont ces endpoints définis par les annotations @PostMapping, @GetMapping, @PutMapping, @DeleteMapping, ... qui permettent d'intéragir avec les données depuis l'extérieur (typiquement l'application Vue va requêter sur ces endpoints pour manipuler les données)
<br/>
- **des données de test** : dans **SeedDatasourceDev.java** dans le package **datasource**. Au fur et à mesure que nous créerons des entités, il faudra ajouter des données de test correspondantes dans ce fichier. Il sert à populer la base de donnée à chaque fois qu'on lance l'environnement de developpement. Comme vous pouvez le voir dans ce fichier, on utilise le package Faker pour créer des données de test aléatoires rapidement. Ici par exemple les tutoriels ont pour titre un nom d'un jeu Zelda aléatoire et comme description un lorem ipsum.

Globalement, pour chaque nouvelle entitée, il faudra un model, un repository et un controller associé. Et le controller doit être pensé pour fournir les endpoints nécessaires à notre frontend.

Pour développer notre api, n'hésitez pas à vous référencer à la doc de l'api REST (jakarta) et surtout les annotations.

Vous pouvez tester l'implémentation des tutoriels dans l'api en lançant le serveur de développement et en faisant des requêtes sur les endpoints (avec l'outil que vous voulez : curl, postman, ...). Par exemple :

- pour ajouter un nouveau tutoriel :
```
curl --json '{"title": "Comment se marier à Hatsune Miku ?", "description": "Ce tutoriel explique le processus utilisé par certains japonais pour se marier à Hatsune Miku."}' http://localhost:8080/api/tutorials
```

- pour lire les tutoriels dont qui ont été publiés :
```
curl http://localhost:8080/api/tutorials/published
```

#### APPLICATION VUE

Maintenant qu'on a une API qui fonctionne pour les tutoriels, nous pouvons implémenter quelques interfaces web dans le projet vue-app pour pouvoir gérer et afficher ces tutoriels.

Les routes sont gérées par **vue-router**, on les a définies dans le fichier **router.js**. Pour les routes des tutoriels, on a :
- http://localhost:5173/tutorials/






TODO :
- montrer une query sql avec make exec-sql


## Bibliographie
Makefiles :
https://rosszurowski.com/log/2022/makefiles
https://blog.bjorn-eric.com/making-a-makefile

Spring :
https://start.spring.io/

JPA :
https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

TODO : ajouter autres liens
