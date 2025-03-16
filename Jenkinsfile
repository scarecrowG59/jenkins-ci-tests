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
	
	// Java step

	stage('Setup Java Environment') {
            steps {
                script {
                    sh 'echo "Setting up Java environment..."'
                }
            }
        }

	stage('Build Java Project') {
            steps {
                script {
                    sh '''
                    cd src/main/java/com/example
                    javac App.java
                    '''
                }
            }
        }

	stage('Run Java Tests') {
            steps {
                script {
                    sh '''
                    cd $WORKSPACE
                    mvn test
                    '''
                }
            }
        }

        stage('Code Coverage (JaCoCo)') {
            steps {
                script {
                    sh '''
                    cd $WORKSPACE
                    mvn jacoco:report
                    '''
                }
            }
        }

        stage('Package JAR') {
            steps {
                script {
                    sh '''
                    cd ~/jenkins-ci-tests
                    mvn package
                    '''
                }
            }
        }

	// Docker stage

        stage('Build Docker Image') {
            steps {
                script {
                    sh '''
                    docker build -t my-java-app:latest .
                    '''
                }
            }
        }
	stage('Run Docker Container') {
	    steps {
	        script {
	            sh '''
	            docker run --rm my-java-app:latest
	            '''
	        }
	    }
	}
	
	stage('Push to Docker Hub') {
	    steps {
	        script {
	            withCredentials([usernamePassword(credentialsId: 'jenkins-docker', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
	                sh '''
	                docker login -u $DOCKER_USER -p $DOCKER_PASS
	                docker tag my-java-app:latest zackops/jenkins-docked:latest
	                docker push zackops/jenkins-docked:latest
	                '''
	            }
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
