# Contribuer au projet app-web-n7

## Environnement de développement

### Prérequis
- linux, windows (via WSL)
- java 17 (minimum)
- npm
- nodejs
- docker et docker compose
- vite
- make (pas nécessaire, mais scripts utiles lors du développement)

### Commencer à développer

**Cloner le repo**
```
git clone https://github.com/cyrianR/app-web-n7.git
```

**Lancer l'environnement de développement**

Deux alternatives : Un Makefile intègre toutes les commandes utiles pour lancer l'environnement rapidement et en background OU vous pouvez lancer à la main dans plusieurs terminaux les commandes (peut s'avérer utile pour debug et/ou comprendre bien ce qu'on fait).

**Option 1 : avec Makefile, commandes disponibles :**

> Note : Lancer les commandes make à la racine du projet.

- Message d'aide pour les commandes disponibles
```
make help
```  

- Lancer les serveurs de développement en background :
```
make dev
```

- Lancer les serveurs de développement, avec outputs du serveur de l'api dans le terminal :
```
make dev-verbose-api
```

- Stopper tous les serveurs de développement :
```
make stop
```

- Supprimer tous les fichiers de build :
```
make clean
```

- Lancer une query SQL dans la DB (pour debug) :
```
make exec-sql SQL="<query SQL>"
```

**Option 2 : lancer les serveurs dans plusieurs terminaux :**

**1.** Ajouter les variables d'environnement aux projets
```
cp .env.example .env
cp .env ./spring-boot-api/.env
cp .env ./vue-app/.env
```

**2.** Lancer la DB dans un container docker
```
docker compose up -d
```
> Note : pour stopper : docker compose down

**3.** Lancer l'API

Se placer dans le projet spring-boot-api :
```
cd spring-boot-api
```

Dans un premier terminal :
```
./gradlew build --continuous
```
> Note : --continuous permet de continuer à rebuild automatiquement dès qu'un changement dans les fichiers est détecté, sinon il faut relancer ./gradlew build à chaque changement

Dans un second terminal :
```
./gradlew bootRun
```
> Note : cette commande start l'api Spring Boot dans un serveur tomcat intégré

**4.** Lancer l'application VueJS
Dans un troisième terminal :
```

```


> **Finalement**, ne pas oublier de tout stopper quand vous avez fini pour le bien de votre RAM (fermer les terminaux et docker compose down OU utiliser make stop)


### Accéder aux serveurs lancés

Dans un navigateur, on peut accéder :
- à l'API : <http://localhost:8080/api>
- à l'application : <http://localhost:5173/>

A l'aide d'un client de base de données comme DBeaver ou pgAdmin, on peut visualiser les entrées de la DB. Il faut configurer ces clients avec les creds du .env pour y accéder. Cependant, vous avez la commande "make exec-sql" vue plus haut pour rapidement lancer des requêtes SQL vers la DB.

Pour tester l'API seulement (si le front associé n'a pas encore été codé), vous pouvez utiliser Postman ou curl pour envoyer des requêtes comme post ou get.

## Développement

Lire [PROJECT.md](PROJECT.md) pour comprendre la structure du code et les technologies utilisées.

**IMPORTANT** : Faire attention à l'utilisation de git ! On ne force pas un push ! On vérifie bien qu'on annule pas du code d'une autre personne lorsqu'on résoud les conflits ! Les conflits c'est normal, on travaille sur les mêmes fichiers, mais il faut proprement s'occuper des conflits en mergant correctement à la main votre code et celui des autres. (on ne fait pas bêtement "accept current change" ou "accept incoming change", il faut inclure les deux)

### Résolution de problèmes

- Quand l'environnement de dev est lancé avec le Makefile, les outputs des serveurs sont forward dans des fichiers dans le dossier /log du projet.

- Dans un projet web, un des problèmes récurrent est le formattage des données et faire en sorte qu'elles apparaissent correctement dans le front (pas tronquées ou invisibles ou autre) donc il faut bien tester dans votre navigateur les composants, et il faut dans mettre des données assez variées dans le seeding de l'api.

- Si problème avec l'environnement ou questions sur l'utilisation de gradle/vite/npm/docker/Makefile ou sur la structure du projet : demander à Cyrian.

### Conventions

1. Commentaires, noms de variables/classes/etc... en **ANGLAIS**.

2. Noms de commit qui ont du sens. Et un commit != une feature : il peut y avoir plusieurs commit par feature (surtout lorsqu'on touche à la fois au frontend et au backend).

3. Utiliser des branches si possible (une branche = une feature).