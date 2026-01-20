#!/bin/bash

# Training Center Management - Script de démarrage rapide
# Ce script configure et lance l'application

set -e

echo "================================"
echo "Training Center Management"
echo "Script de Démarrage Rapide"
echo "================================"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Fonction pour afficher les messages
print_status() {
    echo -e "${GREEN}[✓]${NC} $1"
}

print_info() {
    echo -e "${YELLOW}[i]${NC} $1"
}

print_error() {
    echo -e "${RED}[✗]${NC} $1"
}

# Vérifier Java
print_info "Vérification de Java..."
if ! command -v java &> /dev/null; then
    print_error "Java n'est pas installé!"
    exit 1
fi

java_version=$(java -version 2>&1 | grep -oP 'version "\K.*?(?=")')
print_status "Java $java_version trouvé"

# Vérifier Maven
print_info "Vérification de Maven..."
if ! command -v mvn &> /dev/null; then
    print_error "Maven n'est pas installé!"
    exit 1
fi

mvn_version=$(mvn -v | head -n 1)
print_status "$mvn_version trouvé"

echo ""
echo "Options de démarrage:"
echo "1) Mode Développement (H2 en mémoire)"
echo "2) Mode Production (MySQL)"
echo "3) Construire uniquement (maven package)"
echo "4) Docker Compose"
echo ""

read -p "Choisir une option (1-4): " choice

case $choice in
    1)
        print_info "Lancement en mode Développement..."
        export SPRING_PROFILES_ACTIVE=dev
        mvn spring-boot:run
        ;;
    2)
        print_info "Lancement en mode Production..."
        
        # Vérifier MySQL
        if ! command -v mysql &> /dev/null; then
            print_error "MySQL client n'est pas installé!"
            echo "Pour configurer la base de données manuellement:"
            echo "1. Assurez-vous que MySQL est démarré"
            echo "2. Exécutez: mysql -u root -p < database/init.sql"
            exit 1
        fi
        
        # Vérifier la connexion MySQL
        print_info "Vérification de la connexion MySQL..."
        if mysql -h localhost -u root -proot -e "SELECT 1" &>/dev/null; then
            print_status "MySQL connecté"
            
            # Initialiser la base de données
            print_info "Création de la base de données..."
            mysql -u root -proot < database/init.sql
            print_status "Base de données créée"
        else
            print_error "Impossible de se connecter à MySQL"
            echo "Vérifier:"
            echo "- MySQL est démarré"
            echo "- Identifiants: root/root"
            echo "- Port: 3306"
            exit 1
        fi
        
        export SPRING_PROFILES_ACTIVE=prod
        mvn spring-boot:run
        ;;
    3)
        print_info "Construction du package..."
        mvn clean package
        print_status "Package construit: target/training-center-management-1.0.0-SNAPSHOT.jar"
        ;;
    4)
        print_info "Lancement avec Docker Compose..."
        
        if ! command -v docker &> /dev/null; then
            print_error "Docker n'est pas installé!"
            exit 1
        fi
        
        if ! command -v docker-compose &> /dev/null; then
            print_error "Docker Compose n'est pas installé!"
            exit 1
        fi
        
        docker-compose up --build
        ;;
    *)
        print_error "Option invalide!"
        exit 1
        ;;
esac

print_status "Démarrage terminé!"
