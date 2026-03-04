<div align="center">

# 🛒 ShopSphere QA Automation Suite

### Enterprise-Grade Full-Stack Testing Framework

<!-- GitHub Actions Badges -->
[![Smoke Tests](https://github.com/davidodidi/shopsphere-qa-suite/actions/workflows/smoke-tests.yml/badge.svg)](https://github.com/davidodidi/shopsphere-qa-suite/actions/workflows/smoke-tests.yml)
[![Regression Suite](https://github.com/davidodidi/shopsphere-qa-suite/actions/workflows/regression-suite.yml/badge.svg)](https://github.com/davidodidi/shopsphere-qa-suite/actions/workflows/regression-suite.yml)
[![Mobile Tests](https://github.com/davidodidi/shopsphere-qa-suite/actions/workflows/mobile-tests.yml/badge.svg)](https://github.com/davidodidi/shopsphere-qa-suite/actions/workflows/mobile-tests.yml)
[![API Contract Tests](https://github.com/davidodidi/shopsphere-qa-suite/actions/workflows/api-contract-tests.yml/badge.svg)](https://github.com/davidodidi/shopsphere-qa-suite/actions/workflows/api-contract-tests.yml)

<!-- Jenkins Badge — replace YOUR_JENKINS_URL and YOUR_JOB_NAME -->
[![Jenkins Build](https://img.shields.io/badge/Jenkins-passing-brightgreen?logo=jenkins&logoColor=white)](http://localhost:8080/job/shopsphere-qa-suite/)
[![Allure Report](https://img.shields.io/badge/Allure-Report-orange?logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNCAyNCI+PHBhdGggZD0iTTEyIDJMMiAyMmgyMEwxMiAyeiIgZmlsbD0id2hpdGUiLz48L3N2Zz4=&logoColor=white)](http://localhost:9090/job/shopsphere-qa-suite/allure/#)

<!-- Static Badges -->
[![Java](https://img.shields.io/badge/Java-11-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.15-43B02A?logo=selenium&logoColor=white)](https://www.selenium.dev/)
[![Appium](https://img.shields.io/badge/Appium-3.2.0-662D8C?logo=appium&logoColor=white)](http://appium.io/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.14-23D96C?logo=cucumber&logoColor=white)](https://cucumber.io/)
[![RestAssured](https://img.shields.io/badge/RestAssured-5.3.2-4DB33D)](https://rest-assured.io/)
[![Pact](https://img.shields.io/badge/Pact-4.6.5-E43C5B)](https://docs.pact.io/)
[![JMeter](https://img.shields.io/badge/JMeter-5.6.2-D22128?logo=apache&logoColor=white)](https://jmeter.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**A production-grade QA automation portfolio covering Web, Mobile, and API testing for a full e-commerce application — with Pact contract testing, JMeter performance testing, and full CI/CD via GitHub Actions and Jenkins.**

[📊 Live Allure Report](https://davidodidi.github.io/shopsphere-qa-suite/) · [🚀 Quick Start](#-quick-start) · [🏗 Architecture](#-architecture) · [📈 Latest Results](#-latest-pipeline-results)

</div>

---

## ✅ Latest Pipeline Results

> Last full Jenkins pipeline run — all stages passing.

| Suite | Tests | Passed | Failed | Pass Rate |
|-------|-------|--------|--------|-----------|
| 🔄 End-to-End Checkout Flow | 1 | 1 | 0 | ✅ 100% |
| 🔌 Product API - HTTP Protocol Validation | 9 | 9 | 0 | ✅ 100% |
| 🛍 Product Browsing | 5 | 5 | 0 | ✅ 100% |
| 🔐 User Authentication | 7 | 7 | 0 | ✅ 100% |
| 👤 User Service API Validation | 6 | 6 | 0 | ✅ 100% |
| ⚡ Performance (JMeter smoke-load) | 178 samples | 178 | 0 | ✅ 0% errors |
| **TOTAL** | **28** | **28** | **0** | **✅ 100%** |

> 📊 **[View Full Allure Report →](http://localhost:9090/job/shopsphere-qa-suite/allure/#)**
>
> **Full pipeline: 28/28 tests passing · 100% pass rate · 0 failures**
>
> **JMeter smoke-load baseline:** 178 samples · 0% error rate · p95 107ms

---

## 🎯 What This Project Demonstrates

| Skill Area | Technologies |
|---|---|
| **Web UI Automation** | Selenium WebDriver 4, Page Object Model, Page Factory (`@FindBy`) |
| **Mobile Automation** | Appium 3, `@AndroidFindBy`, `@iOSXCUITFindBy`, Android + iOS |
| **API Testing** | RestAssured, full HTTP verb coverage (GET/POST/PUT/PATCH/DELETE/HEAD/OPTIONS) |
| **BDD** | Cucumber 7, Gherkin feature files, shared across web + mobile |
| **TDD** | JUnit 5 — core utilities written test-first |
| **Contract Testing** | Pact (consumer-driven contracts), consumer + provider verification |
| **Performance Testing** | JMeter — smoke, spike, soak, and stress test plans |
| **Test Types** | Smoke · Sanity · Regression · Integration · System · E2E · UAT |
| **CI/CD** | GitHub Actions (multi-job, nightly, parallel) + Jenkins declarative pipeline |
| **Infrastructure** | Docker Compose — Selenium Grid (Hub + Chrome×2 + Firefox) + Allure server |
| **Cloud** | AWS Device Farm (mobile), S3 (reports), EC2 (CI runners) |
| **Reporting** | Allure Report published to GitHub Pages + Jenkins Allure plugin |

---

## 🏗 Architecture

```
┌──────────────────── ShopSphere Application ─────────────────────┐
│                                                                    │
│   🌐 Web (SauceDemo)    📱 Mobile (SauceLabs APK)    🔌 REST API │
│                                                                    │
└──────────────────────────────┬─────────────────────────────────── ┘
                               │
          ┌────────────────────▼─────────────────────┐
          │         shopsphere-qa-suite               │
          │                                           │
          │  ┌──────────┐ ┌──────────┐ ┌──────────┐  │
          │  │web-tests │ │mobile-   │ │api-tests │  │
          │  │Selenium  │ │tests     │ │RestAssrd │  │
          │  │Cucumber  │ │Appium    │ │Contract  │  │
          │  │POM+PF    │ │Cucumber  │ │(Pact)    │  │
          │  └────┬─────┘ └────┬─────┘ └────┬─────┘  │
          │       └────────────┴─────────────┘        │
          │                    │                       │
          │           ┌────────▼────────┐             │
          │           │     core        │             │
          │           │ ConfigReader    │             │
          │           │ DriverManager   │             │
          │           │ Models / Utils  │             │
          │           │ JUnit TDD Tests │             │
          │           └─────────────────┘             │
          │                                           │
          │  ┌────────────────────────────────────┐   │
          │  │  performance/ (JMeter)             │   │
          │  │  smoke · soak · spike · stress     │   │
          │  └────────────────────────────────────┘   │
          │                                           │
          │  ┌────────────────────────────────────┐   │
          │  │  .github/workflows/ + Jenkinsfile  │   │
          │  │  GitHub Actions + Jenkins CI/CD    │   │
          │  └────────────────────────────────────┘   │
          └───────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
shopsphere-qa-suite/
│
├── pom.xml                                   ← Maven multi-module parent
│
├── core/                                     ← Shared by all modules
│   └── src/
│       ├── main/java/com/shopsphere/
│       │   ├── config/
│       │   │   ├── ConfigReader.java         ← Singleton, env-aware props (dev/staging/prod)
│       │   │   └── DriverManager.java        ← Thread-safe WebDriver (local + Selenium Grid)
│       │   ├── constants/
│       │   │   ├── APIEndpoints.java         ← Centralised REST endpoint constants
│       │   │   ├── HTTPStatus.java           ← HTTP status codes (RFC 9110)
│       │   │   └── AppConstants.java         ← Tags, timeouts, test data constants
│       │   ├── models/                       ← Jackson POJOs: User, Product, Order, ApiResponse
│       │   └── utils/
│       │       ├── WaitUtils.java            ← Explicit/fluent waits
│       │       ├── ScreenshotUtils.java      ← Auto-capture on failure, Allure attach
│       │       ├── TestDataFactory.java      ← JavaFaker test data generation
│       │       └── JsonUtils.java            ← Serialization helpers
│       └── test/java/                        ← JUnit 5 unit tests (TDD)
│           ├── ConfigReaderTest.java
│           └── TestDataFactoryTest.java
│
├── web-tests/                                ← Selenium + Cucumber BDD
│   └── src/
│       ├── main/java/com/shopsphere/pages/
│       │   ├── BasePage.java                 ← POM base: PageFactory, Allure @Step, helpers
│       │   ├── LoginPage.java
│       │   ├── ProductsPage.java
│       │   ├── ProductDetailPage.java
│       │   ├── CartPage.java
│       │   └── CheckoutPage.java
│       └── test/
│           ├── java/com/shopsphere/web/
│           │   ├── hooks/WebHooks.java       ← @Before/@After driver lifecycle + screenshots
│           │   ├── stepdefs/                 ← LoginSteps, ProductSteps, CheckoutSteps
│           │   └── runners/
│           │       ├── SmokeTestRunner.java
│           │       ├── SanityTestRunner.java
│           │       ├── RegressionTestRunner.java
│           │       ├── E2ETestRunner.java
│           │       └── UATTestRunner.java
│           └── resources/features/
│               ├── login.feature
│               ├── product_browsing.feature
│               ├── checkout.feature
│               └── uat.feature
│
├── mobile-tests/                             ← Appium + Cucumber
│   └── src/
│       ├── main/java/com/shopsphere/
│       │   ├── config/AppiumDriverManager.java
│       │   └── screens/
│       │       ├── BaseScreen.java
│       │       ├── LoginScreen.java          ← @AndroidFindBy + @iOSXCUITFindBy
│       │       ├── ProductsScreen.java
│       │       ├── CartScreen.java
│       │       └── CheckoutScreen.java
│       └── test/resources/
│           ├── apps/SauceLabs.apk
│           └── capabilities/
│               ├── android.json
│               └── ios.json
│
├── api-tests/                                ← RestAssured + Cucumber + Pact
│   └── src/test/java/com/shopsphere/api/
│       ├── specs/
│       │   ├── ApiBaseSpec.java
│       │   ├── AuthSpec.java
│       │   └── ProductApiSpec.java
│       ├── stepdefs/ApiSteps.java
│       ├── contract/
│       │   ├── ProductConsumerContractTest.java
│       │   └── ProductProviderContractTest.java
│       └── runners/
│
├── performance/
│   ├── test-plans/
│   │   ├── smoke-load.jmx
│   │   ├── soak-test.jmx
│   │   ├── spike-test.jmx
│   │   └── stress-test.jmx
│   └── scripts/check_results.py
│
├── docker/
│   ├── docker-compose.yml
│   ├── Dockerfile.web-tests
│   └── Dockerfile.api-tests
│
├── .github/workflows/
│   ├── smoke-tests.yml
│   ├── regression-suite.yml
│   ├── api-contract-tests.yml
│   ├── performance-tests.yml
│   └── mobile-tests.yml
│
├── Jenkinsfile
└── scripts/setup.sh
```

---

## 🚀 Quick Start

### Prerequisites

- Java 11+
- Maven 3.8+
- Docker Desktop (for Selenium Grid)
- JMeter 5.6+ (for performance tests)

### 1. Clone & Setup

```bash
git clone https://github.com/davidodidi/shopsphere-qa-suite.git
cd shopsphere-qa-suite
chmod +x scripts/setup.sh && ./scripts/setup.sh
```

### 2. Start Selenium Grid

```bash
docker compose -f docker/docker-compose.yml up -d
# Grid console:  http://localhost:4444
# Allure UI:     http://localhost:5252
# Chrome VNC:    http://localhost:7900  (watch tests live)
```

### 3. Run Tests

```bash
# Unit Tests (TDD — JUnit 5)
mvn test -pl core

# Web Smoke
mvn test -pl web-tests -Dcucumber.filter.tags="@smoke"

# Web Regression
mvn test -pl web-tests -Dcucumber.filter.tags="@regression" -Dbrowser=chrome

# Web E2E
mvn test -pl web-tests -Dcucumber.filter.tags="@e2e"

# Web UAT
mvn test -pl web-tests -Dcucumber.filter.tags="@uat"

# API Tests
mvn test -pl api-tests -Dcucumber.filter.tags="@api"

# Contract Tests (Pact)
mvn test -pl api-tests -Dtest="ProductConsumerContractTest"

# Mobile Tests (requires Appium server — see Mobile Setup)
mvn test -pl mobile-tests \
  -Dplatform=android \
  -Dappium.server=http://127.0.0.1:4723 \
  -Dcucumber.filter.tags="@mobile" \
  --no-transfer-progress

# Full Suite
mvn test --no-transfer-progress

# Performance Tests
jmeter -n -t performance/test-plans/smoke-load.jmx \
       -l performance/results/results.jtl \
       -e -o performance/results/html-report
```

### 4. Generate Allure Report

```bash
mvn allure:serve -pl web-tests
```

### 5. Run via Docker

```bash
docker build -f docker/Dockerfile.web-tests -t shopsphere-web-tests .
docker run -e TAGS="@smoke" \
  -e GRID_URL="http://selenium-hub:4444/wd/hub" \
  --network shopsphere-qa_shopsphere-qa \
  shopsphere-web-tests

docker build -f docker/Dockerfile.api-tests -t shopsphere-api-tests .
docker run -e TAGS="@api" shopsphere-api-tests
```

---

## 📱 Mobile Test Setup

### Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| Node.js | 20+ | Required to run Appium |
| Appium | 3.2.0 | `npm install -g appium` |
| UIAutomator2 driver | latest | `appium driver install uiautomator2` |
| Android Studio | latest | Provides emulator + SDK |
| Android SDK | API 35 | Install via SDK Manager |
| ADB | included | Must be on PATH |

> ⚠️ **Appium 3.x:** The `/wd/hub` suffix has been removed. Use `http://127.0.0.1:4723` — not `http://127.0.0.1:4723/wd/hub`.

```bash
# 1. Install Appium
npm install -g appium
appium driver install uiautomator2

# 2. Set environment variables (Windows)
ANDROID_HOME=C:\Users\<YourUsername>\AppData\Local\Android\Sdk
# Add to PATH: %ANDROID_HOME%\platform-tools and %ANDROID_HOME%\emulator

# 3. Start emulator (Pixel 6, API 35) then confirm:
adb devices
# emulator-5554   device

# 4. Update APK path in AppiumDriverManager.java
# capabilities.setCapability("app", "C:/path/to/Android.SauceLabs.Mobile.Sample.app.apk");

# 5. Start Appium
appium  # http://127.0.0.1:4723

# 6. Run mobile tests
mvn test -pl mobile-tests \
  -Dplatform=android \
  -Dappium.server=http://127.0.0.1:4723 \
  -Dcucumber.filter.tags="@mobile" \
  --no-transfer-progress
```

**Test isolation:** `AppiumDriverManager` sets `noReset: false` — the app fully resets to login screen between sessions, preventing stale state.

**Debugging:** Use [Appium Inspector](https://github.com/appium/appium-inspector) connected to `http://127.0.0.1:4723` to inspect element trees and build locators interactively.

---

## 🧪 Test Strategy

### Test Types & Tag Mapping

| Type | Tag | Runner | Trigger |
|------|-----|--------|---------|
| **Unit** | — | JUnit 5 | Every build |
| **Smoke** | `@smoke` | SmokeTestRunner | Every PR & deploy |
| **Sanity** | `@sanity` | SanityTestRunner | Post-deployment |
| **Regression** | `@regression` | RegressionTestRunner | Nightly (sequential) |
| **Integration** | `@api` | ApiRegressionRunner | Nightly |
| **E2E** | `@e2e` | E2ETestRunner | Nightly |
| **UAT** | `@uat` | UATTestRunner | Release cycles |
| **Contract** | — | ProductConsumerContractTest | On API file changes |
| **Performance** | — | JMeter plans | Weekly (Sunday 3AM UTC) |
| **Mobile** | `@mobile` | MobileSmokeRunner | Nightly (Appium) |

### Test Pyramid

```
          /\
         /  \         ← E2E / UAT  (Selenium + Appium)
        / UI \
       /──────\
      / API    \      ← Integration / Contract  (RestAssured + Pact)
     /──────────\
    / Unit Tests \    ← TDD  (JUnit 5 — core utilities)
   /______________\
```

---

## 🔌 API Testing — Full HTTP Verb Coverage

| Method | Endpoint | Assertion |
|--------|----------|-----------|
| `GET` | `/products` | 200, JSON body, response time < 3s |
| `GET` | `/products/{id}` | 200 with fields, 404 for missing |
| `POST` | `/products` | 201 created, 401 without auth |
| `PUT` | `/products/{id}` | 200 full update |
| `PATCH` | `/products/{id}` | 200 partial update |
| `DELETE` | `/products/{id}` | 200/204 success |
| `HEAD` | `/products` | 200, empty body, headers present |
| `OPTIONS` | `/products` | Allow header includes GET, POST |

---

## 🤝 Contract Testing (Pact)

Consumer-driven contract testing prevents API breaking changes from reaching the frontend or mobile app undetected.

```
ShopSphereFrontend (Consumer)       ShopSphereProductService (Provider)
         │                                        │
         │  1. Define expected interactions        │
         │──────────────────────────────────────►  │
         │  2. Pact generates contract JSON        │
         │◄──────────────────────────────────────  │
         │  3. Provider verifies contract          │
         │──────────────────────────────────────►  │
         │         (fails fast if API changes)     │
```

Contracts are generated to `api-tests/target/pacts/` and published as CI artifacts on every run.

---

## ⚡ Performance Testing (JMeter)

| Plan | Users | Duration | Goal |
|------|-------|----------|------|
| `smoke-load.jmx` | 10 | 2 min | Baseline — p95 < 2s |
| `soak-test.jmx` | 50 | 60 min | Memory stability, no resource leaks |
| `spike-test.jmx` | 0→200 | 30s ramp | Handle sudden traffic spikes |
| `stress-test.jmx` | 10→300 | Staged | Find the breaking point |

**Latest smoke-load results:** 178 samples · 0% error rate · p95 107ms

CI thresholds enforced by `performance/scripts/check_results.py`:
- Max error rate: 1.0%
- Max p95 response time: 3,000ms

---

## 📱 Mobile Testing Strategy

The mobile test suite mirrors the web suite using the **same Gherkin feature files**. One set of business scenarios, two implementations:

```
login.feature  (shared)
       │
       ├──► WebHooks.java  +  LoginPage.java    (Selenium)
       └──► MobileHooks.java + LoginScreen.java (Appium)
```

**Supported platforms:**
- Android via UIAutomator2 (`android.json`)
- iOS via XCUITest (`ios.json`)
- AWS Device Farm via `deviceArn` capability

---

## ⚙️ CI/CD Pipelines

### GitHub Actions

| Workflow | Trigger | Jobs |
|----------|---------|------|
| `smoke-tests.yml` | Every PR + push to main | Web Smoke + API Smoke (parallel) |
| `regression-suite.yml` | Nightly 2AM UTC Mon–Fri | Unit → API → Web matrix (Chrome/Firefox) → E2E → Allure Pages |
| `api-contract-tests.yml` | On API file changes | Pact consumer + artifact upload |
| `performance-tests.yml` | Weekly Sunday 3AM UTC | JMeter smoke load + threshold check |
| `mobile-tests.yml` | Manual + Nightly 3AM UTC Mon–Fri | Android emulator (Pixel 6 API 35) |

### Jenkins Declarative Pipeline

```
🔬 Unit Tests → 🔌 API Tests → 📋 Contract Tests
                      ↓
         🌐 Web Smoke ║ 🩺 Sanity  (parallel)
                      ↓
              🔄 E2E Tests → ✅ UAT Tests
                      ↓
         📱 Mobile Tests  (if enabled)
                      ↓
         ⚡ Performance   (if enabled)
                      ↓
         📊 Allure Report Generation
```

Pipeline features:
- Parameterised builds (environment, browser, tags, mobile toggle, performance toggle)
- Parallel stage execution
- Slack notifications on pass/fail
- Allure report publishing via Jenkins plugin

---

## 🐳 Docker Infrastructure

```yaml
# Services started by docker-compose.yml
selenium-hub   → http://localhost:4444   (Grid console)
chrome-node-1  → http://localhost:7900   (VNC — watch tests live)
chrome-node-2  → Second Chrome node for parallelism
firefox-node   → Firefox cross-browser node
allure         → http://localhost:5050   (Allure API)
allure-ui      → http://localhost:5252   (Allure dashboard)
```

```bash
docker compose -f docker/docker-compose.yml up -d
```

---

## ☁️ AWS Integration

| Service | Usage |
|---------|-------|
| **AWS Device Farm** | Real device testing for Android + iOS via `deviceArn` capability |
| **Amazon S3** | Allure report archives and JMeter result storage |
| **Amazon EC2** | Self-hosted Jenkins agents for CI pipeline execution |

---

## 🛠 Tech Stack

| Category | Technology | Version |
|----------|-----------|---------|
| Language | Java | 11 |
| Build | Maven (multi-module) | 3.9 |
| Web Automation | Selenium WebDriver | 4.15.0 |
| Driver Management | WebDriverManager | 5.6.3 |
| Mobile Automation | Appium | 3.2.0 |
| API Testing | RestAssured | 5.3.2 |
| BDD Framework | Cucumber | 7.14.0 |
| Unit Testing | JUnit 5 | 5.10.0 |
| Integration Runner | TestNG | 7.8.0 |
| Contract Testing | Pact JVM | 4.6.5 |
| Performance | Apache JMeter | 5.6.2 |
| Reporting | Allure | 2.24.0 |
| Test Data | JavaFaker | 1.0.2 |
| Serialization | Jackson | 2.15.3 |
| Boilerplate | Lombok | 1.18.30 |
| Logging | Log4j2 | 2.21.1 |
| Infrastructure | Docker Compose | 3.8 |
| CI/CD | GitHub Actions + Jenkins | — |
| Cloud | AWS (Device Farm, S3, EC2) | — |

---

## 🔧 Troubleshooting

**Selenium Grid connection refused**
```bash
# Confirm hub is running
docker ps | grep selenium
curl http://localhost:4444/wd/hub/status
```

**Appium server not detected**
```bash
# Confirm Appium is running and ADB sees the emulator
adb devices
appium --version
# Ensure ANDROID_HOME is set and platform-tools is on PATH
```

**Pact consumer test fails**
- Ensure the mock server port (default 8080) is free
- Pact contracts are written to `api-tests/target/pacts/` — confirm the directory exists after a run

**JMeter threshold failure in CI**
- Review `performance/results/html-report/index.html` for p95 breakdown
- `check_results.py` exits non-zero if error rate > 1% or p95 > 3000ms

---

## 📊 Reporting

| Report | Location | Published By |
|--------|----------|--------------|
| Allure (web) | [GitHub Pages](https://davidodidi.github.io/shopsphere-qa-suite/) | GitHub Actions (nightly) |
| Allure (all modules) | Jenkins Allure Plugin | Jenkins pipeline |
| JMeter HTML report | `performance/results/html-report/` | `performance-tests.yml` |
| Pact contracts | `api-tests/target/pacts/` | CI artifact upload |
| Cucumber HTML | `target/cucumber-reports/` | Each module on every run |

---

## 👨‍💻 Author

**David Odidi** — QA Automation Engineer

Java · Selenium · Appium · RestAssured · Playwright · Cypress · Python · CI/CD (GitHub Actions · Jenkins) · AWS

[![GitHub](https://img.shields.io/badge/GitHub-davidodidi-181717?logo=github)](https://github.com/davidodidi)

---

<div align="center">

*Built to demonstrate full-stack QA automation capability for SDET and QA Automation Engineer roles*

</div>
