# 🚀 Guide de Démarrage Rapide

## ⚡ Avant de commencer

Assurez-vous que vous avez installé :
- ✅ Java 17 ou supérieur
- ✅ Maven 3.8+
- ✅ Git

## 📥 Installation Rapide

### Étape 1 : Cloner/Accéder au projet
```bash
cd d:\dev\Springboot\Spring\workspace\TrainingCenterManagement
```

### Étape 2 : Démarrer l'application

#### Option A : Mode Développement (Recommandé pour débuter)
```bash
mvn clean spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

L'application démarre avec une base de données **H2 en mémoire** (pas besoin de MySQL).

**Accès :**
- 🌐 Application : http://localhost:8080
- 🗄️ Console H2 : http://localhost:8080/h2-console
  - Username: `sa`
  - Password: (laisser vide)

#### Option B : Mode Production (avec MySQL)

**Prérequis :** MySQL doit être installé et démarré

```bash
# 1. Créer la base de données
mysql -u root -p < database/init.sql

# 2. Démarrer l'application
mvn clean spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

#### Option C : Docker Compose (Plus facile pour MySQL)

```bash
# Démarrer l'app + MySQL dans des conteneurs
docker-compose up

# Arrêter
docker-compose down
```

## 🌐 Accès à l'application

Une fois lancée, vous pouvez accéder à :

| Ressource | URL | Description |
|-----------|-----|-------------|
| **Accueil** | http://localhost:8080 | Page d'accueil |
| **Dashboard** | http://localhost:8080/dashboard | Tableau de bord administrateur |
| **Étudiants** | http://localhost:8080/etudiants | Gestion des étudiants |
| **API Étudiants** | http://localhost:8080/api/etudiants | API REST |
| **API Cours** | http://localhost:8080/api/cours | API REST |
| **API Inscriptions** | http://localhost:8080/api/inscriptions | API REST |

## 📊 Données d'exemple

L'application inclut des données de test :

### Étudiants
- **MAT001** : Jean Dupont (Informatique)
- **MAT002** : Marie Martin (Informatique)
- **MAT003** : Pierre Bernard (Réseaux)
- **MAT004** : Luc Moreau (Intelligence Artificielle)

### Formateurs
- **FORM001** : Mohamed Zayani (Architecture logicielle)
- **FORM002** : Ahmed Belhadj (Réseaux)
- **FORM003** : Fatima Bendjedia (Machine Learning)

### Spécialités
- Informatique
- Réseaux
- Intelligence Artificielle
- Sécurité

### Cours
- **INF101** : Architecture des Systèmes
- **INF102** : Programmation Avancée
- **RES101** : Fondamentaux des Réseaux
- **AI101** : Introduction au Machine Learning

## 🔧 Configuration

### Fichiers de configuration

**application.properties** - Configuration générale
**application-dev.properties** - Configuration développement (H2)
**application-prod.properties** - Configuration production (MySQL)

### Changer le port

Modifier dans `application.properties` :
```properties
server.port=8081
```

### Logs

Les logs sont générés dans le répertoire `logs/` en mode production.

## 🛠️ Commandes Maven utiles

```bash
# Nettoyer et construire
mvn clean install

# Juste compiler
mvn compile

# Exécuter les tests
mvn test

# Créer le package JAR
mvn package

# Lancer l'application
mvn spring-boot:run

# Nettoyer les builds
mvn clean
```

## 🐛 Résolution des problèmes

### Port 8080 déjà utilisé
```bash
# Changer le port en ligne de commande
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Erreur de connexion MySQL
```bash
# Vérifier que MySQL est démarré
mysql -u root -p

# Initialiser la base de données
mysql -u root -p trainingcenter < database/init.sql

# Vérifier les identifiants dans application-prod.properties
```

### Base de données vide en mode DEV
- En mode développement, les données de test sont créées automatiquement au démarrage
- La base H2 est réinitialisée à chaque redémarrage (c'est normal)

### Dépendances manquantes
```bash
# Forcer le téléchargement des dépendances
mvn dependency:resolve
mvn dependency:resolve-plugins
```

## 📱 Tester l'API REST

### Avec cURL

```bash
# Lister tous les étudiants
curl http://localhost:8080/api/etudiants

# Créer un nouvel étudiant
curl -X POST http://localhost:8080/api/etudiants \
  -H "Content-Type: application/json" \
  -d '{
    "matricule": "MAT005",
    "nom": "Nouveau",
    "prenom": "Etudiant",
    "email": "nouveau@example.com",
    "telephone": "+212600000000"
  }'

# Lister les cours
curl http://localhost:8080/api/cours

# Créer une inscription
curl -X POST http://localhost:8080/api/inscriptions/etudiant/1/cours/1
```

### Avec Postman

1. Importer dans Postman
2. Utiliser les endpoints listés dans [Endpoints disponibles](README.md#-endpoints-disponibles)

## 📖 Documentation supplémentaire

Pour plus d'informations, consultez :
- [README.md](README.md) - Documentation complète
- [STRUCTURE.md](STRUCTURE.md) - Structure du projet
- [database/init.sql](database/init.sql) - Schéma de la base de données

## 🎯 Prochaines étapes

Après le démarrage :

1. ✅ Naviguez vers http://localhost:8080/dashboard
2. ✅ Explorez la gestion des étudiants
3. ✅ Testez les endpoints API
4. ✅ Modifiez le code et testez les modifications
5. ✅ Lisez la documentation complète

## 💡 Conseils de développement

```bash
# Utiliser devtools pour rechargement automatique
# Modifier un fichier → F5 dans le navigateur pour recharger

# Activer les logs détaillés
export SPRING_JPA_SHOW_SQL=true
export SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true

# Déboguer
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

## 🆘 Besoin d'aide ?

- Consultez les logs : `logs/application.log`
- Vérifiez la console Maven
- Lisez les fichiers README et STRUCTURE

---

**Prêt à commencer ?** 🚀

```bash
mvn clean spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

Visitez http://localhost:8080 dans votre navigateur !
