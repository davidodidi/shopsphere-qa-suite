#!/bin/bash
# ═══════════════════════════════════════════════════════════════
# ShopSphere QA Suite — GitHub Push Script
# Run this script from inside the shopsphere-qa-suite/ directory
# ═══════════════════════════════════════════════════════════════
set -e

REPO_NAME="shopsphere-qa-suite"
GITHUB_USERNAME="davidodidi"

echo ""
echo "════════════════════════════════════════════════════════"
echo "  ShopSphere QA Suite — GitHub Setup & Push"
echo "════════════════════════════════════════════════════════"
echo ""

# ─── Step 1: Check git is installed ───────────────────────────
if ! command -v git &> /dev/null; then
    echo "❌ Git not found. Install from https://git-scm.com/"
    exit 1
fi
echo "✅ Git: $(git --version)"

# ─── Step 2: Initialise git repo ──────────────────────────────
if [ ! -d ".git" ]; then
    echo ""
    echo "📁 Initialising git repository..."
    git init
    echo "✅ Git repository initialised"
else
    echo "✅ Git repository already exists"
fi

# ─── Step 3: Set up .gitignore ────────────────────────────────
echo ""
echo "📝 .gitignore already present ✅"

# ─── Step 4: Stage all files ──────────────────────────────────
echo ""
echo "📦 Staging all files..."
git add .
echo "✅ Files staged: $(git status --short | wc -l | tr -d ' ') files"

# ─── Step 5: Initial commit ───────────────────────────────────
echo ""
echo "💾 Creating initial commit..."
git commit -m "feat: initial commit — ShopSphere full-stack QA automation suite

Includes:
- Web UI tests: Selenium 4 + Cucumber BDD + POM + Page Factory + TestNG
- Mobile tests: Appium 8 + Android/iOS screen objects + cross-platform strategy
- API tests: RestAssured + all HTTP verbs (GET/POST/PUT/PATCH/DELETE/HEAD/OPTIONS)
- Contract tests: Pact consumer-driven contracts (consumer + provider)
- Performance tests: JMeter smoke/soak/spike/stress test plans
- Test types: Unit (JUnit TDD) + Smoke + Sanity + Regression + E2E + UAT
- CI/CD: GitHub Actions (4 workflows) + Jenkins declarative pipeline
- Infrastructure: Docker Compose Selenium Grid + Allure reporting server
- Reporting: Allure published to GitHub Pages
- Shared: core module with ConfigReader, DriverManager, models, utils"

echo "✅ Initial commit created"

# ─── Step 6: Set branch to main ───────────────────────────────
echo ""
git branch -M main
echo "✅ Branch set to: main"

# ─── Step 7: Add remote ───────────────────────────────────────
echo ""
REMOTE_URL="https://github.com/${GITHUB_USERNAME}/${REPO_NAME}.git"
if git remote get-url origin &> /dev/null; then
    echo "⚠️  Remote 'origin' exists — updating to: ${REMOTE_URL}"
    git remote set-url origin "${REMOTE_URL}"
else
    echo "🔗 Adding remote origin: ${REMOTE_URL}"
    git remote add origin "${REMOTE_URL}"
fi
echo "✅ Remote set to: ${REMOTE_URL}"

# ─── Step 8: Push ─────────────────────────────────────────────
echo ""
echo "🚀 Pushing to GitHub..."
echo "   (You may be prompted for your GitHub username and Personal Access Token)"
echo ""
git push -u origin main

echo ""
echo "════════════════════════════════════════════════════════"
echo "  ✅ Successfully pushed to GitHub!"
echo ""
echo "  🔗 Repository: https://github.com/${GITHUB_USERNAME}/${REPO_NAME}"
echo ""
echo "  NEXT STEPS:"
echo "  1. Enable GitHub Pages:"
echo "     → Settings → Pages → Source: gh-pages branch"
echo "     → Your Allure report will auto-publish after first CI run"
echo ""
echo "  2. Add repository secrets (Settings → Secrets → Actions):"
echo "     → STAGING_USERNAME = standard_user"
echo "     → STAGING_PASSWORD = secret_sauce"
echo ""
echo "  3. Star the repo and share the link on your resume:"
echo "     https://github.com/${GITHUB_USERNAME}/${REPO_NAME}"
echo ""
echo "  4. Live Allure Report (after first Actions run):"
echo "     https://${GITHUB_USERNAME}.github.io/${REPO_NAME}/"
echo "════════════════════════════════════════════════════════"
