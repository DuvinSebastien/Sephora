# Résumé

Test technique Android réalisé par Duvin Sébastien en environ 10h.

## Stack technique

### Kotlin

Langage de programmation officiel pour le développement d'application Android native. Le plus
utilisé, la communauté est grandissante.

### MVVM

Pattern structurant le mieux adapté pour l'implémentation de la logique d'une vue. Facile à tester
et bonne synergie avec Compose et Hilt.

Son implémentation est assez naturelle et pas verbeux.

### Compose

Permet l'implémentation des écrans. Écriture du code très naturelle permettant la mise à jour
automatique de l'écran en fonction des données branchées à elle.

Utiliser Compose plutôt que du XML rend la verbosité des écrans moins lourdes et permet de
s'affranchir d'un nouveau langage qu'est le XML par le Kotlin. Il nous permet d'être plus proche des
activités / fragments puisque son implémentation peut être dans la même classe ou dans le même
package.

### Hilt

Hilt est une bibliothèque d'injection de dépendances pour Android qui réduit le code récurrent
nécessaire pour injecter manuellement des dépendances dans votre projet.

### Mockito

Solution de mock pour les tests unitaires permettant de nous concentrer sur la logique qu'on
souhaite tester en nous fournissant des données mockées. Son implémentation est assez simple, il
couvre un large panel de fonctionnalités sur les mocks en test unitaire.

## Architecture

L'utilisation de Repository et de Service nous permet de bien diviser le rôle de chaque entité
dans l'acquisition de données et ainsi de pouvoir intervenir sur chacune de ses étapes. Chaque
entité a son rôle bien défini ce qui nous permet d'optimiser nos tests d'une part mais aussi de
pouvoir rendre flexible l'implémentation de nouvelles fonctionnalités.

Les ViewModels sont quelque choses de très puissant puisqu'il nous permet de porter toute la logique
de l'écran dans une seule classe et possède son propre scope. Hilt s'adapte très bien aux ViewModel
puisqu'il fournit des bibliothèque technique adapté à cette architecture.

Ils perdurent tout au long du cycle de vie de l'écran et permet une sauvegarde des données de l'
écran.

## Axe d'amélioration

+ Ajout d'une navigation (NavGraph) : Mise en place d’un système de navigation pour faciliter la
  transition entre différents écrans (détails, profil, etc.).

+ Gestion du multilingue par pays : Implémentation de fichiers strings pour gérer les traductions en
  fonction de la langue du pays. Prise en charge des différentes unités (tailles, monnaies, etc.)
  selon la localisation.

+ Centralisation des erreurs API : Création de classes abstraites pour centraliser la gestion des
  erreurs API, permettant d’ajuster les messages d’erreur ou les types d’affichage de manière
  cohérente.

+ Ajout d’un SplashScreen :

    + Côté UI : Affichage d’un écran de chargement au démarrage de l’application.
    + Côté domaine : Attente du chargement des informations nécessaires (compte abonné, profil
      complété, données principales, etc.) avant d’ouvrir l’écran d'accueil (Home).

