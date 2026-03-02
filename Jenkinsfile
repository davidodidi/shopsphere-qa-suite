pipeline {
    agent any

    parameters {
        choice(name: 'ENV',          choices: ['staging', 'dev', 'prod'],        description: 'Target environment')
        choice(name: 'BROWSER',      choices: ['chrome', 'firefox', 'edge'],     description: 'Browser for web tests')
        string(name: 'TAGS',         defaultValue: '@smoke',                      description: 'Cucumber tags')
        booleanParam(name: 'RUN_PERF', defaultValue: false,                      description: 'Run JMeter performance tests?')
        booleanParam(name: 'RUN_MOBILE', defaultValue: false,                    description: 'Run Appium mobile tests?')
    }

    environment {
        JAVA_HOME    = tool 'JDK-11'
        MAVEN_HOME   = tool 'Maven-3.9'
        PATH         = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${env.PATH}"
        GRID_URL     = "http://selenium-hub:4444/wd/hub"
        ALLURE_HOME  = tool 'Allure-2.24'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timeout(time: 90, unit: 'MINUTES')
        timestamps()
        ansiColor('xterm')
    }

    stages {

        stage('🔬 Unit Tests — TDD') {
            steps {
                echo "Running JUnit unit tests (core module)"
                sh 'mvn test -pl core --no-transfer-progress'
            }
            post {
                always {
                    junit 'core/target/surefire-reports/*.xml'
                }
            }
        }

        stage('🔌 API Tests — Regression') {
            steps {
                echo "Running API tests + Contract tests"
                sh """
                    mvn test -pl api-tests \\
                        -Dcucumber.filter.tags="@api" \\
                        -Denv=${params.ENV} \\
                        --no-transfer-progress
                """
            }
            post {
                always {
                    publishHTML([
                        reportDir: 'api-tests/target/cucumber-reports',
                        reportFiles: 'api-regression-report.html',
                        reportName: 'API Test Report'
                    ])
                }
            }
        }

        stage('📋 Contract Tests — Pact') {
            steps {
                sh """
                    mvn test -pl api-tests \\
                        -Dtest="ProductConsumerContractTest" \\
                        --no-transfer-progress
                """
            }
            post {
                always {
                    archiveArtifacts artifacts: 'api-tests/target/pacts/*.json', allowEmptyArchive: true
                }
            }
        }

        stage('🌐 Web Tests') {
            parallel {
                stage('💨 Smoke Tests') {
                    when { expression { params.TAGS == '@smoke' || params.TAGS == '@regression' } }
                    steps {
                        sh """
                            mvn test -pl web-tests \\
                                -Dcucumber.filter.tags="@smoke" \\
                                -Dbrowser=${params.BROWSER} \\
                                -Dgrid.url=${env.GRID_URL} \\
                                -Denv=${params.ENV} \\
                                --no-transfer-progress
                        """
                    }
                }
                stage('🧹 Sanity Tests') {
                    when { expression { params.TAGS == '@sanity' || params.TAGS == '@regression' } }
                    steps {
                        sh """
                            mvn test -pl web-tests \\
                                -Dcucumber.filter.tags="@sanity" \\
                                -Dbrowser=${params.BROWSER} \\
                                -Dgrid.url=${env.GRID_URL} \\
                                -Denv=${params.ENV} \\
                                --no-transfer-progress
                        """
                    }
                }
            }
        }

        stage('🔄 E2E / Integration Tests') {
            when {
                anyOf {
                    expression { params.TAGS == '@e2e' }
                    expression { params.TAGS == '@regression' }
                }
            }
            steps {
                sh """
                    mvn test -pl web-tests \\
                        -Dcucumber.filter.tags="@e2e" \\
                        -Dbrowser=${params.BROWSER} \\
                        -Dgrid.url=${env.GRID_URL} \\
                        -Denv=${params.ENV} \\
                        --no-transfer-progress
                """
            }
        }

        stage('✅ UAT Tests') {
            when { expression { params.TAGS == '@uat' || params.TAGS == '@regression' } }
            steps {
                sh """
                    mvn test -pl web-tests \\
                        -Dcucumber.filter.tags="@uat" \\
                        -Dbrowser=${params.BROWSER} \\
                        -Dgrid.url=${env.GRID_URL} \\
                        -Denv=${params.ENV} \\
                        --no-transfer-progress
                """
            }
        }

        stage('📱 Mobile Tests — Appium') {
            when { expression { return params.RUN_MOBILE } }
            steps {
                echo "Running Appium mobile tests"
                sh """
                    mvn test -pl mobile-tests \\
                        -Dcucumber.filter.tags="@smoke and @mobile" \\
                        -Dplatform=android \\
                        -Denv=${params.ENV} \\
                        --no-transfer-progress
                """
            }
        }

        stage('⚡ Performance Tests — JMeter') {
            when { expression { return params.RUN_PERF } }
            steps {
                echo "Running JMeter performance tests"
                sh """
                    jmeter -n \\
                        -t performance/test-plans/smoke-load.jmx \\
                        -l performance/results/jenkins-results.jtl \\
                        -e -o performance/results/jenkins-html-report
                """
            }
            post {
                always {
                    perfReport 'performance/results/jenkins-results.jtl'
                }
            }
        }

        stage('📊 Generate Allure Report') {
            steps {
                sh "${env.ALLURE_HOME}/bin/allure generate \
                    web-tests/target/allure-results \
                    api-tests/target/allure-results \
                    --clean -o allure-report"
            }
        }
    }

    post {
        always {
            allure([
                includeProperties: false,
                jdk: '',
                results: [
                    [path: 'web-tests/target/allure-results'],
                    [path: 'api-tests/target/allure-results'],
                    [path: 'mobile-tests/target/allure-results']
                ]
            ])
            publishHTML([
                reportDir: 'web-tests/target/cucumber-reports',
                reportFiles: 'regression-report.html',
                reportName: 'Cucumber Web Report'
            ])
        }
        success {
            echo '✅ All tests passed!'
            slackSend(color: 'good', message: "✅ ShopSphere QA PASSED — ${env.JOB_NAME} #${env.BUILD_NUMBER}")
        }
        failure {
            echo '❌ Tests failed!'
            slackSend(color: 'danger', message: "❌ ShopSphere QA FAILED — ${env.JOB_NAME} #${env.BUILD_NUMBER} — ${env.BUILD_URL}")
        }
        unstable {
            echo '⚠️ Tests unstable!'
        }
    }
}
