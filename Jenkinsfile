pipeline {
    agent any

    stages {
        // 🧾 Git clone
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/scarecrowG59/jenkins-ci-tests.git'
            }
        }

        // 🐍 Python ENV
        stage('Setup Environment') {
            steps {
                sh 'pip3 install --upgrade pip'
                sh 'pip3 install pytest'
            }
        }

        // 🧪 Python Tests
        stage('Run Python Tests') {
            steps {
                sh '''
                cd python_project
                pytest --junitxml=pytest-report.xml || true
                '''
            }
        }

        // ☕ Java Compile (Maven)
        stage('Build Java Project') {
            steps {
                sh '''
                mvn clean compile
                '''
            }
        }

        // ☕ Java Test (JUnit + JaCoCo)
        stage('Run Java Tests') {
            steps {
                sh '''
                mvn test
                '''
            }
        }

        // 📊 JaCoCo Coverage
        stage('Code Coverage (JaCoCo)') {
            steps {
                sh '''
                mvn jacoco:report
                '''
            }
        }

        // 📦 Package JAR
        stage('Package JAR') {
            steps {
                sh '''
                mvn package
                '''
            }
        }

        // 🐳 Docker build
        stage('Build Docker Image') {
            steps {
                sh '''
                docker build -t my-java-app:latest .
                '''
            }
        }

        // ▶️ Run Docker
        stage('Run Docker Container') {
            steps {
                sh '''
                docker run --rm my-java-app:latest
                '''
            }
        }

        // ☁️ Push Docker
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

        // 🔍 SonarQube
        stage('SonarQube Analysis') {
            environment {
                SCANNER_HOME = tool 'SonarScanner'
            }
            steps {
                withSonarQubeEnv(installationName: 'Sonar') {
                    sh '''
                    ${SCANNER_HOME}/bin/sonar-scanner \
                        -Dsonar.projectKey=jenkins-ci-tests \
                        -Dsonar.sources=src/main/java,python_project
			-Dsonar.exclusions=**/target/site/jacoco/**/*.html
                        -Dsonar.host.url=http://165.232.130.101:9000/ \
			-Dsonar.java.binaries=target/classes \
                        -Dsonar.python.version=3.10 \
                        -Dsonar.qualitygate.wait=true
                    '''
                }
            }
        }

        // ✅ Quality Gate
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    // 🧾 Post test report
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
