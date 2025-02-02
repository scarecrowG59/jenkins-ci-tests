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
                    sh 'pip3 install pytest'
                }
            }
        }

        stage('Run Python Tests') {
            steps {
                script {
                    sh '''
                    cd python_project
                    pytest --junitxml=pytest-report.xml || true
                    '''
                }
            }
        }
    }

    post {
        always {
            script {
                if (fileExists('python_project/pytest-report.xml')) {
                    junit 'python_project/pytest-report.xml'
                }
            }
        }
    }
}
