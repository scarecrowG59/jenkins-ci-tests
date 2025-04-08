pipeline {
    agent any

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
        IMAGE_NAME = 'zackops/jenkins-docked:latest'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/scarecrowG59/jenkins-ci-tests.git'
            }
        }

        stage('Setup Python Environment') {
            steps {
                sh '''
                    pip3 install --upgrade pip
                    pip3 install pytest
                '''
            }
        }

        stage('Run Python Tests') {
            steps {
                sh '''
                    cd python_project
                    pytest --junitxml=pytest-report.xml || true
                '''
            }
        }

        stage('Build Java Project') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Run Java Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Code Coverage (JaCoCo)') {
            steps {
                sh 'mvn jacoco:report'
            }
        }

        stage('Package JAR') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Read Dynamic Port') {
            steps {
                script {
                    if (fileExists('target/test-application-port.txt')) {
                        env.APP_PORT = readFile('target/test-application-port.txt').trim()
                    } else {
                        env.APP_PORT = '8080'
                    }
                    echo "🛠️ Using application port: ${env.APP_PORT}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build --build-arg APP_PORT=${env.APP_PORT} -t ${env.IMAGE_NAME} ."
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'jenkins-docker', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${IMAGE_NAME}
                    '''
                }
            }
        }

        stage('Trivy Security Scan') {
            steps {
                sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image --exit-code 0 --severity HIGH,CRITICAL ${IMAGE_NAME}"
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SCANNER_HOME = tool 'SonarScanner'
            }
            steps {
                withSonarQubeEnv('Sonar') {
                    sh '''
                        ${SCANNER_HOME}/bin/sonar-scanner \
                            -Dsonar.projectKey=jenkins-ci-tests \
                            -Dsonar.sources=src/main/java,python_project \
                            -Dsonar.exclusions=**/target/site/jacoco/**/*.html \
                            -Dsonar.host.url=http://165.232.130.101:9000/ \
                            -Dsonar.java.binaries=target/classes \
                            -Dsonar.python.version=3.10 \
                            -Dsonar.qualitygate.wait=true
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

	stage('Deploy to Kubernetes') {
	    steps {
	        sh 'kubectl apply -f k8s/deployment.yaml'
		sh 'kubectl get pods -o wide'
	    }
	}
    }
	
    post {
        always {
            script {
                if (fileExists('python_project/pytest-report.xml')) {
                    junit 'python_project/pytest-report.xml'
                }
                if (fileExists('target/surefire-reports')) {
                    junit 'target/surefire-reports/*.xml'
                }
            }
            echo '🧹 Cleanup: removing Docker image to save space.'
            sh 'docker rmi ${IMAGE_NAME} || true'
        }
        success {
            echo '✅ Build completed successfully!'
        }
        failure {
            echo '❌ Build failed!'
        }
    }
}

