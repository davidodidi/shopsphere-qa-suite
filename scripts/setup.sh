#!/bin/bash
# ShopSphere QA Suite — Initial Setup Script
set -e

echo "════════════════════════════════════════"
echo "  ShopSphere QA Suite — Setup"
echo "════════════════════════════════════════"

# Check Java
if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Install Java 11+"; exit 1
fi
JAVA_VER=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
echo "✅ Java: $(java -version 2>&1 | head -1)"

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven not found. Install Maven 3.8+"; exit 1
fi
echo "✅ Maven: $(mvn -version | head -1)"

# Check Docker
if command -v docker &> /dev/null; then
    echo "✅ Docker: $(docker --version)"
    echo "Starting Selenium Grid..."
    docker compose -f docker/docker-compose.yml up -d selenium-hub chrome-node-1
    echo "✅ Selenium Grid started at http://localhost:4444"
else
    echo "⚠️  Docker not found — running tests locally (no grid)"
fi

# Check JMeter
if command -v jmeter &> /dev/null; then
    echo "✅ JMeter: $(jmeter --version 2>&1 | head -1)"
else
    echo "⚠️  JMeter not found — performance tests will be skipped"
fi

# Build
echo "Building project..."
mvn clean install -DskipTests --no-transfer-progress -q
echo "✅ Build successful"

echo ""
echo "════════════════════════════════════════"
echo "  Ready! Run tests with:"
echo "  mvn test -pl web-tests -Dcucumber.filter.tags='@smoke'"
echo "  mvn test -pl api-tests -Dcucumber.filter.tags='@api'"
echo "  mvn test -pl core      (unit tests)"
echo "════════════════════════════════════════"
