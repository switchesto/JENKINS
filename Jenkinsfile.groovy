pipeline {
    agent any
    
    environment {
        DIRECTORY_PATH = '/path/to/code/directory'
        TESTING_ENVIRONMENT = 'Staging'
        PRODUCTION_ENVIRONMENT = 'Production'
    }
    
    stages {
        stage('Build') {
            steps {
                echo 'Building the code...'
                // sh 'mvn clean install' // Uncomment this for Maven build command
            }
        }
        
        stage('Unit and Integration Tests') {
            steps {
                echo 'Running unit tests...'
                // sh 'mvn test' // Uncomment for running unit tests with Maven
                echo 'Running integration tests...'
                // sh 'mvn integration-test' // Uncomment for running integration tests
            }
            post {
                success {
                    retry(2) {
                        sleep(time: 10, unit: 'SECONDS')
                        emailext(
                            body: "Test Stage: Success\n\nPlease find attached test logs.",
                            subject: "Pipeline Test Status: Success",
                            to: 'albinfedrick@gmail.com',
                            attachmentsPattern: "sample.log"
                        )
                    }
                }
                failure {
                    retry(2) {
                        sleep(time: 10, unit: 'SECONDS')
                        emailext(
                            body: "Test Stage: Failure\n\nPlease find attached test logs.",
                            subject: "Pipeline Test Status: Failure",
                            to: 'albinfedrick@gmail.com',
                            attachmentsPattern: "sample.log"
                        )
                    }
                }
            }
        }
        
        stage('Code Analysis') {
            steps {
                echo 'Analyzing code...'
                // sh 'sonar-scanner' // Uncomment for SonarQube code analysis
            }
        }
        
        stage('Security Scan') {
            steps {
                echo 'Performing security scan...'
                // sh 'owasp-zap -scan' // Uncomment for OWASP ZAP security scan
            }
            post {
                success {
                    retry(2) {
                        sleep(time: 10, unit: 'SECONDS')
                        emailext(
                            body: "Security Scan Stage: Success\n\nPlease find attached security scan logs.",
                            subject: "Pipeline Security Scan Status: Success",
                            to: 'albinfedrick@gmail.com',
                            attachmentsPattern: "*.log"
                        )
                    }
                }
                failure {
                    retry(2) {
                        sleep(time: 10, unit: 'SECONDS')
                        emailext(
                            body: "Security Scan Stage: Failure\n\nPlease find attached security scan logs.",
                            subject: "Pipeline Security Scan Status: Failure",
                            to: 'albinfedrick@gmail.com',
                            attachmentsPattern: "*.log"
                        )
                    }
                }
            }
        }
        
        stage('Deploy to Staging') {
            steps {
                echo 'Deploying to staging environment...'
                // sh 'ssh user@staging-server "deploy-script.sh"' // Uncomment for deployment to staging server
            }
        }
        
        stage('Integration Tests on Staging') {
            steps {
                echo 'Running integration tests on staging...'
                // sh 'mvn verify' // Uncomment for running tests on staging environment
            }
        }
        
        stage('Deploy to Production') {
            steps {
                echo 'Deploying to production environment...'
                // sh 'ssh user@production-server "deploy-script.sh"' // Uncomment for deployment to production server
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline completed.'
        }
        success {
            retry(2) {
                sleep(time: 10, unit: 'SECONDS')
                emailext(
                    body: 'Pipeline succeeded. All stages completed successfully.',
                    subject: 'Pipeline Status: Success',
                    to: 'albinfedrick@gmail.com'
                )
            }
        }
        failure {
            retry(2) {
                sleep(time: 10, unit: 'SECONDS')
                emailext(
                    body: 'Pipeline failed. Check logs for details.',
                    subject: 'Pipeline Status: Failure',
                    to: 'albinfedrick@gmail.com'
                )
            }
        }
    }
}
