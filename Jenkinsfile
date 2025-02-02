pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/scarecrowG59/jenkins-ci-tests.git'
            }
        }

        stage('Setup Environment') {
            steps {
                script {
                    sh 'pip3 install --upgrade pip'
                    sh 'pip3 install pytest pytest-junitxml'
                }
            }
        }

        stage('Run Python Tests') {
            steps {
                script {
                    sh 'cd python_project && pytest --junitxml=pytest-report.xml'
                }
            }
        }

        stage('Run Java Tests') {
            steps {
                script {
                    sh 'cd junit-project && mvn clean test'
                }
            }
        }
    }

    post {
        always {
            junit '**/pytest-report.xml'
            junit '**/target/surefire-reports/*.xml'
        }
    }
}

