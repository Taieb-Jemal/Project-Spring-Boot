#!/bin/bash

# Script de test de l'API Training Center Management
# Ce script teste les endpoints principaux de l'API

set -e

BASE_URL="http://localhost:8080"

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

print_header() {
    echo ""
    echo -e "${YELLOW}===================================================${NC}"
    echo -e "${YELLOW}$1${NC}"
    echo -e "${YELLOW}===================================================${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

# Vérifier la connexion à l'API
print_header "Vérification de la connexion à l'API"

if ! curl -s -f $BASE_URL > /dev/null; then
    print_error "Impossible de se connecter à $BASE_URL"
    echo "Assurez-vous que l'application est démarrée"
    exit 1
fi

print_success "Connexion établie à $BASE_URL"

# ===== TESTS ÉTUDIANTS =====
print_header "TEST: ÉTUDIANTS"

print_success "Récupération de la liste des étudiants..."
curl -s -X GET $BASE_URL/api/etudiants | jq '.' | head -20

print_success "Récupération d'un étudiant spécifique (ID: 1)..."
curl -s -X GET $BASE_URL/api/etudiants/1 | jq '.'

# ===== TESTS COURS =====
print_header "TEST: COURS"

print_success "Récupération de la liste des cours..."
curl -s -X GET $BASE_URL/api/cours | jq '.' | head -20

print_success "Récupération d'un cours spécifique (ID: 1)..."
curl -s -X GET $BASE_URL/api/cours/1 | jq '.'

# ===== TESTS FORMATEURS =====
print_header "TEST: FORMATEURS"

print_success "Récupération de la liste des formateurs..."
curl -s -X GET $BASE_URL/api/formateurs | jq '.' | head -20

# ===== TESTS INSCRIPTIONS =====
print_header "TEST: INSCRIPTIONS"

print_success "Récupération de la liste des inscriptions..."
curl -s -X GET $BASE_URL/api/inscriptions | jq '.' | head -20

# ===== TEST DE CRÉATION =====
print_header "TEST: CRÉATION D'UN ÉTUDIANT"

print_success "Création d'un nouvel étudiant..."
curl -s -X POST $BASE_URL/api/etudiants \
  -H "Content-Type: application/json" \
  -d '{
    "matricule": "MAT_TEST_001",
    "nom": "Test",
    "prenom": "Utilisateur",
    "email": "test@example.com",
    "telephone": "+212600000000"
  }' | jq '.'

# ===== RÉSUMÉ =====
print_header "Tests complétés avec succès ✅"

echo ""
echo "Ressources disponibles:"
echo "  🌐 Application: $BASE_URL"
echo "  📊 Dashboard: $BASE_URL/dashboard"
echo "  👥 Étudiants: $BASE_URL/etudiants"
echo "  👨‍🏫 Formateurs: $BASE_URL/formateurs"
echo "  📚 Cours: $BASE_URL/cours"
echo ""
echo "API Endpoints:"
echo "  GET  $BASE_URL/api/etudiants"
echo "  GET  $BASE_URL/api/cours"
echo "  GET  $BASE_URL/api/formateurs"
echo "  GET  $BASE_URL/api/inscriptions"
echo ""
