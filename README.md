<div align="center">

# 🛒 ShopSphere QA Automation Suite (In Progress)

### Enterprise-Grade Full-Stack Testing Framework

[![Smoke Tests](https://github.com/davidodidi/shopsphere-qa-suite/actions/workflows/smoke-tests.yml/badge.svg)](https://github.com/davidodidi/shopsphere-qa-suite/actions)
[![Regression Suite](https://github.com/davidodidi/shopsphere-qa-suite/actions/workflows/regression-suite.yml/badge.svg)](https://github.com/davidodidi/shopsphere-qa-suite/actions)
[![Java](https://img.shields.io/badge/Java-11-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.15-43B02A?logo=selenium&logoColor=white)](https://www.selenium.dev/)
[![Appium](https://img.shields.io/badge/Appium-3.2.0-662D8C?logo=appium&logoColor=white)](http://appium.io/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.14-23D96C?logo=cucumber&logoColor=white)](https://cucumber.io/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**A production-grade QA automation portfolio covering Web, Mobile, and API testing layers of the same e-commerce application — with Pact contract testing, JMeter performance testing, and full CI/CD via GitHub Actions and Jenkins.**

[📊 Live Allure Report](https://davidodidi.github.io/shopsphere-qa-suite/) · [🚀 Quick Start](#-quick-start) · [🏗 Architecture](#-architecture)

</div>

---

## 🎯 What This Project Demonstrates

| Skill Area | Technologies |
|---|---|
| **Web UI Automation** | Selenium WebDriver 4, Page Object Model, Page Factory (`@FindBy`) |
| **Mobile Automation** | Appium 3, `@AndroidFindBy`, `@iOSXCUITFindBy`, Android + iOS |
| **API Testing** | RestAssured, GET/POST/PUT/PATCH/DELETE/HEAD/OPTIONS coverage |
| **BDD** | Cucumber 7, Gherkin feature files, shared across web + mobile |
| **TDD** | JUnit 5 — core utilities written test-first |
| **Contract Testing** | Pact (consumer-driven contracts), consumer + provider verification |
| **Performance Testing** | JMeter — smoke, spike, soak, and stress test plans |
| **Test Types** | Smoke · Sanity · Regression · Integration · System · E2E · UAT |
| **HTTP / SOA** | All HTTP verbs, status code validation, response time assertions |
| **CI/CD** | GitHub Actions (multi-job, nightly, parallel) + Jenkins declarative pipeline |
| **Infrastructure** | Docker Compose — Selenium Grid (Hub + Chrome×2 + Firefox) + Allure server |
| **Cloud** | AWS Device Farm (mobile), S3 (reports), EC2 (CI runners) |
| **Reporting** | Allure Report published to GitHub Pages on every run |
| **Frameworks** | TestNG (parallel execution) + JUnit 5 |

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
│       │   ├── LoginPage.java                ← @FindBy Page Factory annotations
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
│           │       ├── RegressionTestRunner.java  ← parallel=true
│           │       ├── E2ETestRunner.java
│           │       └── UATTestRunner.java
│           └── resources/
│               ├── features/
│               │   ├── login.feature         ← @smoke @sanity @regression
│               │   ├── product_browsing.feature
│               │   ├── checkout.feature      ← @e2e full purchase flow
│               │   └── uat.feature           ← @uat business sign-off
│               ├── testng-smoke.xml
│               └── testng-regression.xml     ← parallel threads=3
│
├── mobile-tests/                             ← Appium + Cucumber
│   └── src/
│       ├── main/java/com/shopsphere/
│       │   ├── config/AppiumDriverManager.java  ← Android + iOS + AWS Device Farm
│       │   └── screens/                         ← Mirror of web pages
│       │       ├── BaseScreen.java
│       │       ├── LoginScreen.java          ← @AndroidFindBy + @iOSXCUITFindBy
│       │       ├── ProductsScreen.java
│       │       ├── CartScreen.java
│       │       └── CheckoutScreen.java
│       └── test/
│           ├── java/com/shopsphere/mobile/
│           │   ├── hooks/MobileHooks.java
│           │   ├── stepdefs/                 ← Same Gherkin steps, Appium implementation
│           │   └── runners/                  ← MobileSmokeRunner, MobileRegressionRunner
│           └── resources/
│               ├── features/                 ← Shared feature files from web-tests
│               └── capabilities/
│                   ├── android.json
│                   └── ios.json
│
├── api-tests/                                ← RestAssured + Cucumber + Pact
│   └── src/test/
│       ├── java/com/shopsphere/api/
│       │   ├── specs/
│       │   │   ├── ApiBaseSpec.java          ← RequestSpec, ResponseSpec, auth helpers
│       │   │   ├── AuthSpec.java             ← POST /auth/login, /refresh
│       │   │   └── ProductApiSpec.java       ← Full CRUD + HEAD + OPTIONS
│       │   ├── stepdefs/ApiSteps.java        ← All HTTP verbs as step definitions
│       │   ├── contract/
│       │   │   ├── ProductConsumerContractTest.java  ← Pact consumer definitions
│       │   │   └── ProductProviderContractTest.java  ← Provider verification
│       │   └── runners/
│       └── resources/features/
│           ├── product_api.feature           ← GET/POST/PUT/PATCH/DELETE/HEAD/OPTIONS
│           └── user_api.feature
│
├── performance/                              ← JMeter
│   ├── test-plans/
│   │   ├── smoke-load.jmx                   ← 10 users, p95 < 2s baseline
│   │   ├── soak-test.jmx                    ← 50 users × 60 min, memory stability
│   │   ├── spike-test.jmx                   ← 0→200 users in 30s
│   │   └── stress-test.jmx                  ← Incremental load to find breaking point
│   └── scripts/check_results.py             ← CI threshold analyser (error rate, p95)
│
├── docker/
│   ├── docker-compose.yml                   ← Selenium Grid + Allure server
│   ├── Dockerfile.web-tests
│   └── Dockerfile.api-tests
│
├── .github/workflows/
│   ├── smoke-tests.yml                      ← Every PR: web smoke + API smoke (parallel)
│   ├── regression-suite.yml                 ← Nightly: unit→API→web (matrix)→E2E→Allure Pages
│   ├── api-contract-tests.yml               ← On API changes: Pact consumer + artifact upload
│   └── performance-tests.yml                ← Weekly Sunday: JMeter smoke load
│
├── Jenkinsfile                              ← Declarative pipeline with all stages
└── scripts/setup.sh                        ← One-command local setup
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
docker compose -f docker/docker-compose.yml up -d selenium-hub chrome-node-1
# Grid UI: http://localhost:4444
# Allure UI: http://localhost:5252
```

### 3. Run Tests

```bash
# Unit Tests (TDD — JUnit 5)
mvn test -pl core

# Web Smoke Tests
mvn test -pl web-tests -Dcucumber.filter.tags="@smoke"

# Web Regression (parallel, all browsers)
mvn test -pl web-tests -Dcucumber.filter.tags="@regression" -Dbrowser=chrome

# Web E2E Tests
mvn test -pl web-tests -Dcucumber.filter.tags="@e2e"

# Web UAT Tests
mvn test -pl web-tests -Dcucumber.filter.tags="@uat"

# API Tests
mvn test -pl api-tests -Dcucumber.filter.tags="@api"

# Contract Tests (Pact)
mvn test -pl api-tests -Dtest="ProductConsumerContractTest"

# Mobile Tests (requires Appium server + Android emulator — see Mobile Setup below)
mvn test -pl mobile-tests \
  -Dplatform=android \
  -Dappium.server=http://127.0.0.1:4723 \
  -Dcucumber.filter.tags="@mobile" \
  --no-transfer-progress

# Full Suite
mvn test --no-transfer-progress

# Performance Tests (requires JMeter)
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
# Web smoke tests in Grid
docker build -f docker/Dockerfile.web-tests -t shopsphere-web-tests .
docker run -e TAGS="@smoke" -e GRID_URL="http://selenium-hub:4444/wd/hub" \
  --network shopsphere-qa_shopsphere-qa shopsphere-web-tests

# API tests
docker build -f docker/Dockerfile.api-tests -t shopsphere-api-tests .
docker run -e TAGS="@api" shopsphere-api-tests
```

---

## 📱 Mobile Test Local Setup

Mobile tests run locally against an Android emulator via Appium. The following steps are required before executing the mobile test suite.

### Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| Node.js | 20+ | Required to run Appium |
| Appium | 3.2.0 | Install globally via npm |
| UIAutomator2 driver | latest | Appium driver for Android |
| Android Studio | latest | Provides emulator + SDK |
| Android SDK | API 35 (Android 16) | Install via SDK Manager in Android Studio |
| ADB | included with SDK | Must be on PATH |

### Step 1 — Install Appium and UIAutomator2 driver

```bash
npm install -g appium
appium driver install uiautomator2
```

> ⚠️ **Appium 3.x breaking change:** The `/wd/hub` suffix has been removed. The correct server URL is `http://127.0.0.1:4723` — not `http://127.0.0.1:4723/wd/hub`.

### Step 2 — Set environment variables

```bash
# Windows (set in System Environment Variables or your shell profile)
ANDROID_HOME=C:\Users\<YourUsername>\AppData\Local\Android\Sdk

# Add to PATH:
%ANDROID_HOME%\platform-tools
%ANDROID_HOME%\emulator
```

### Step 3 — Start the Android emulator

Open **Android Studio → Device Manager** and start the **Pixel 6, API 35 (Android 16)** emulator. Confirm it is detected by ADB:

```bash
adb devices
# Expected output:
# List of devices attached
# emulator-5554   device
```

### Step 4 — Configure the APK path

In `mobile-tests/src/main/java/com/shopsphere/config/AppiumDriverManager.java`, the `app` capability must point to the absolute path of the SauceLabs APK on your machine:

```java
capabilities.setCapability("app", "C:/Users/<YourUsername>/path/to/Android.SauceLabs.Mobile.Sample.app.x.x.x.apk");
```

### Step 5 — Start Appium server

```bash
appium
# Server starts at http://127.0.0.1:4723
```

### Step 6 — Run mobile tests

```bash
mvn test -pl mobile-tests \
  -Dplatform=android \
  -Dappium.server=http://127.0.0.1:4723 \
  -Dcucumber.filter.tags="@mobile" \
  --no-transfer-progress
```

### Test isolation note

`AppiumDriverManager` sets `noReset: false`, which ensures the app is fully reset to the login screen between test sessions. This prevents scenarios from inheriting stale state from a previous run.

### Debugging with Appium Inspector

[Appium Inspector](https://github.com/appium/appium-inspector) is a GUI tool for inspecting the app's element tree and building locators. Connect it to `http://127.0.0.1:4723` with the same capabilities used in `AppiumDriverManager.java` to inspect elements interactively without running a full test.

---

## 🧪 Test Strategy

### Test Types & Tag Mapping

| Type | Tag | Runner | Runs When |
|------|-----|--------|-----------|
| **Unit** | — | JUnit 5 | Every build (core module) |
| **Smoke** | `@smoke` | SmokeTestRunner | Every PR & deploy |
| **Sanity** | `@sanity` | SanityTestRunner | Post-deployment |
| **Regression** | `@regression` | RegressionTestRunner | Nightly (parallel) |
| **Integration** | `@api` | ApiRegressionRunner | Nightly |
| **E2E** | `@e2e` | E2ETestRunner | Nightly |
| **UAT** | `@uat` | UATTestRunner | Release cycles |
| **Contract** | — | ProductConsumerContractTest | On API file changes |
| **Performance** | — | JMeter plans | Weekly (Sunday 3AM UTC) |
| **Mobile** | `@mobile` | MobileSmokeRunner | Nightly (Appium) |

### Test Pyramid

```
          /\
         /  \         ← E2E / UAT (Selenium + Appium)
        / UI \        
       /──────\
      / API    \      ← Integration / Contract (RestAssured + Pact)
     /──────────\
    / Unit Tests \    ← TDD (JUnit 5 — core utilities)
   /______________\
```

---

## 🔌 API Testing — HTTP Protocol Coverage

The `api-tests` module validates all HTTP verbs defined in RFC 9110:

| Method | Endpoint | What's Tested |
|--------|----------|---------------|
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

Consumer-driven contract testing ensures the backend API never breaks the frontend or mobile app without warning.

```
ShopSphereFrontend (Consumer)       ShopSphereProductService (Provider)
         │                                        │
         │  1. Define expected interactions        │
         │──────────────────────────────────────►  │
         │                                        │
         │  2. Pact generates contract JSON        │
         │◄──────────────────────────────────────  │
         │                                        │
         │  3. Provider verifies contract          │
         │──────────────────────────────────────►  │
         │         (fails fast if API changes)     │
```

Contracts live in `api-tests/target/pacts/` and are published as CI artifacts on every run.

---

## ⚡ Performance Testing (JMeter)

Four test plans covering the full performance testing spectrum:

| Plan | Users | Duration | Goal |
|------|-------|----------|------|
| `smoke-load.jmx` | 10 | 2 min | Baseline — p95 < 2s |
| `soak-test.jmx` | 50 | 60 min | Memory stability, no resource leaks |
| `spike-test.jmx` | 0→200 | 30s ramp | Handle sudden traffic spikes |
| `stress-test.jmx` | 10→300 | Staged | Find the breaking point |

**Latest smoke-load results:** 178 samples · 0% error rate · p95 107ms

Results are validated automatically by `performance/scripts/check_results.py`:
- Max error rate: 1.0%
- Max p95 response time: 3,000ms

---

## 📱 Mobile Testing Strategy

The mobile test suite mirrors the web test suite using the same Gherkin feature files. The **same business scenarios** run against both platforms — different page/screen objects, same Cucumber steps.

```
login.feature (shared)
       │
       ├──► WebHooks.java + LoginPage.java     (Selenium)
       │
       └──► MobileHooks.java + LoginScreen.java (Appium)
```

**Supported platforms:**
- Android via UIAutomator2 (`android.json` capabilities)
- iOS via XCUITest (`ios.json` capabilities)
- AWS Device Farm via `deviceArn` capability

---

## ⚙️ CI/CD Pipelines

### GitHub Actions

| Workflow | Trigger | Jobs |
|----------|---------|------|
| `smoke-tests.yml` | Every PR + push to main | Web Smoke + API Smoke (parallel) |
| `regression-suite.yml` | Nightly 2AM UTC Mon-Fri | Unit → API → Web (matrix: Chrome/Firefox) → E2E → Allure Pages |
| `api-contract-tests.yml` | On API file changes | Pact consumer + artifact upload |
| `performance-tests.yml` | Weekly Sunday 3AM UTC | JMeter smoke load + threshold check |

### Jenkins Declarative Pipeline

```
🔬 Unit Tests → 🔌 API Tests → 📋 Contract Tests
       ↓
🌐 Web Tests (Smoke || Sanity in parallel)
       ↓
🔄 E2E Tests → ✅ UAT Tests
       ↓
📱 Mobile Tests (if enabled)
       ↓
⚡ Performance Tests (if enabled)
       ↓
📊 Allure Report Generation
```

Jenkins is configured via `Jenkinsfile` with:
- Parameterised builds (environment, browser, tags, mobile toggle, perf toggle)
- Parallel stage execution
- Slack notifications on pass/fail
- Allure report publishing plugin integration

---

## 🐳 Docker Infrastructure

```yaml
# Start the full QA infrastructure
docker compose -f docker/docker-compose.yml up -d

# Services:
#   selenium-hub     → http://localhost:4444  (Grid console)
#   chrome-node-1    → VNC at http://localhost:7900 (watch tests live)
#   chrome-node-2    → Second Chrome node for parallelism
#   firefox-node     → Firefox cross-browser node
#   allure           → http://localhost:5050  (Allure API)
#   allure-ui        → http://localhost:5252  (Allure dashboard)
```

---

## ☁️ AWS Integration

| Service | Usage |
|---------|-------|
| **AWS Device Farm** | Real device testing for Android + iOS via `deviceArn` capability |
| **Amazon S3** | Allure report archives and JMeter result storage |
| **Amazon EC2** | Self-hosted Jenkins agents for CI pipeline execution |

---

## 🛠 Tech Stack Reference

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

## 👨‍💻 Author

**David Odidi** — QA Automation Engineer  
Java • Selenium • Playwright • Cypress • RestAssured • Python • CI/CD (GitHub Actions and Jenkins)  
[github.com/davidodidi](https://github.com/davidodidi)

---

<div align="center">

*Built to demonstrate full-stack QA automation capability for SDET and QA Automation Engineer roles*
This project is tested with BrowserStack

</div>
