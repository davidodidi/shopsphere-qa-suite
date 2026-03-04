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
        ALLURE_HOME  = tool 'Allure-2.24'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timeout(time: 90, unit: 'MINUTES')
        timestamps()
        ansiColor('xterm')
    }

    stages {

        stage('⚙️ Build — Install Core') {
            steps {
                echo "Installing parent POM and core module"
                sh 'mvn install -N -DskipTests --no-transfer-progress'
                sh 'mvn install -pl core -DskipTests --no-transfer-progress'
            }
        }

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
                echo "Running API tests"
                sh """
                    mvn test -pl api-tests \
                        -Dcucumber.filter.tags="@api" \
                        -Dapi.response.time.threshold=5000 \
                        -Denv=${params.ENV} \
                        --no-transfer-progress
                """
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'api-tests/target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'api-tests/target/allure-results/**', allowEmptyArchive: true
                }
            }
        }

        stage('📋 Contract Tests — Pact') {
            steps {
                sh """
                    mvn test -pl api-tests \
                        -Dtest="ProductConsumerContractTest" \
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
                            mvn test -pl web-tests \
                                -Dcucumber.filter.tags="@smoke" \
                                -DsuiteXmlFile=src/test/resources/testng-smoke.xml \
                                -Dbrowser=${params.BROWSER} \
                                -Denv=${params.ENV} \
                                --no-transfer-progress
                        """
                    }
                    post {
                        always {
                            junit allowEmptyResults: true, testResults: 'web-tests/target/surefire-reports/*.xml'
                        }
                    }
                }
                stage('🧹 Sanity Tests') {
                    when { expression { params.TAGS == '@sanity' || params.TAGS == '@regression' } }
                    steps {
                        sh """
                            mvn test -pl web-tests \
                                -Dcucumber.filter.tags="@sanity" \
                                -DsuiteXmlFile=src/test/resources/testng-regression.xml \
                                -Dbrowser=${params.BROWSER} \
                                -Denv=${params.ENV} \
                                --no-transfer-progress
                        """
                    }
                    post {
                        always {
                            junit allowEmptyResults: true, testResults: 'web-tests/target/surefire-reports/*.xml'
                        }
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
                    mvn test -pl web-tests \
                        -Dcucumber.filter.tags="@e2e" \
                        -DsuiteXmlFile=src/test/resources/testng-regression.xml \
                        -Dbrowser=${params.BROWSER} \
                        -Denv=${params.ENV} \
                        --no-transfer-progress
                """
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'web-tests/target/surefire-reports/*.xml'
                }
            }
        }

        stage('✅ UAT Tests') {
            when { expression { params.TAGS == '@uat' || params.TAGS == '@regression' } }
            steps {
                sh """
                    mvn test -pl web-tests \
                        -Dcucumber.filter.tags="@uat" \
                        -DsuiteXmlFile=src/test/resources/testng-regression.xml \
                        -Dbrowser=${params.BROWSER} \
                        -Denv=${params.ENV} \
                        --no-transfer-progress
                """
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'web-tests/target/surefire-reports/*.xml'
                }
            }
        }

        stage('📱 Mobile Tests — Appium') {
            when { expression { return params.RUN_MOBILE } }
            steps {
                echo "Running Appium mobile tests"
                sh """
                    mvn test -pl mobile-tests \
                        -Dcucumber.filter.tags="@smoke and @mobile" \
                        -Dplatform=android \
                        -Denv=${params.ENV} \
                        --no-transfer-progress
                """
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'mobile-tests/target/surefire-reports/*.xml'
                }
            }
        }

        stage('⚡ Performance Tests — JMeter') {
            when { expression { return params.RUN_PERF } }
            steps {
                echo "Running JMeter performance tests"
                sh """
                    jmeter -n \
                        -t performance/test-plans/smoke-load.jmx \
                        -l performance/results/jenkins-results.jtl \
                        -e -o performance/results/jenkins-html-report
                """
            }
            post {
                always {
                    archiveArtifacts artifacts: 'performance/results/**', allowEmptyArchive: true
                }
            }
        }

        stage('📊 Generate Allure Report') {
            steps {
                echo "Generating Allure report"
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
        }
        success {
            echo '✅ All tests passed! ShopSphere QA pipeline completed successfully.'
        }
        failure {
            echo '❌ Tests failed! Check the test reports for details.'
        }
        unstable {
            echo '⚠️ Tests unstable — some tests failed but pipeline completed.'
        }
    }
}
