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

        // 🚀 Новый этап для SonarQube
        stage('SonarQube Analysis') {
            environment {
                SCANNER_HOME = tool 'SonarScanner' // Используем SonarScanner из Jenkins
            }
            steps {
		echo "SonarQube Stage Started"
		sh "echo SCANNER_HOME=${SCANNER_HOME}"
                withSonarQubeEnv(installationName: 'Sonar') {
                    sh '''
                    ${SCANNER_HOME}/bin/sonar-scanner \
                        -Dsonar.projectKey=jenkins-ci-tests \
                        -Dsonar.sources=. \
                        -Dsonar.host.url=http://165.232.130.101:9000/ \
                        -Dsonar.login=squ_18b9c114894e7aab8cf109b5a7d24d7d60ff658b \
                        -Dsonar.python.version=3.10 \
			-Dsonar.qualitygate.wait=true
                    '''
                }
            }
        }

        // 🚨 Quality Gate
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    // 📊 Post Actions: Отчёт о тестах и сканировании
    post {
        always {
            script {
                if (fileExists('python_project/pytest-report.xml')) {
                    junit 'python_project/pytest-report.xml'  // Python тесты
                }
            }
        }
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo ' Build failed!'
        }
    }
}
