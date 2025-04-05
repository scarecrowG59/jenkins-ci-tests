pipeline {
    agent any

    stages {
        //  Clone the repository
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/scarecrowG59/jenkins-ci-tests.git'
            }
        }

        // Python Environment Setup
        stage('Setup Python Environment') {
            steps {
                sh '''
                pip3 install --upgrade pip
                pip3 install pytest
                '''
            }
        }

        //  Run Python Unit Tests
        stage('Run Python Tests') {
            steps {
                sh '''
                cd python_project
                pytest --junitxml=pytest-report.xml || true
                '''
            }
        }

        // Java Compile
        stage('Build Java Project') {
            steps {
                sh 'mvn clean compile'
            }
        }

        //  Java Tests (JUnit + JaCoCo)
        stage('Run Java Tests') {
            steps {
                sh 'mvn test'
            }
        }

        // JaCoCo Coverage Report
        stage('Code Coverage (JaCoCo)') {
            steps {
                sh 'mvn jacoco:report'
            }
        }

        // Package JAR
        stage('Package JAR') {
            steps {
                sh 'mvn clean package'
            }
        }

        // Build Docker Image
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t my-java-app:latest .'
            }
        }

        // ush Docker Image to Docker Hub
        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'jenkins-docker', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                    docker login -u $DOCKER_USER -p $DOCKER_PASS
                    docker tag my-java-app:latest zackops/jenkins-docked:latest
                    docker push zackops/jenkins-docked:latest
                    '''
                }
            }
        }

        // Trivy Security Scan
        stage('Trivy Security Scan') {
            steps {
                sh '''
                docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image --exit-code 0 --severity HIGH,CRITICAL zackops/jenkins-docked:latest
                '''
            }
        }

        // SonarQube Analysis
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

        //  Quality Gate (block pipeline if fail)
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // eploy to Kubernetes (Only with proper kubeconfig)
        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                    sh '''
                    kubectl apply -f k8s/deployment.yaml
                    kubectl apply -f k8s/service.yaml
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
        success {
            echo '✅ Build completed successfully!'
        }
        failure {
            echo '❌ Build failed!'
        }
    }
}
