pipeline {
    agent any

    tools {
        jdk   'JDK17'
        maven 'Maven3'
    }

    environment {
        IMAGE_NAME = 'raghadkhedhiri/student-management'
        IMAGE_TAG  = '1.3'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/raghadkhedhiri/Raghad_Khedhiri_4sae9.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn -B clean test'
            }
        }

        stage('MVN SONARQUBE') {
            steps {
                withSonarQubeEnv('SonarQube') {
                        sh '''
                            mvn -B sonar:sonar \
                              -Dsonar.projectKey=student-management \
                              -Dsonar.token=$SONAR_TOKEN
                        '''
                }
            }
        }

        stage('Package JAR') {
            steps {
                sh 'mvn -B package -DskipTests'
            }
        }

        stage('Archive livrable') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Docker Build') {
            steps {
                sh """
                    docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                """
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-cred',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh 'docker logout'
                }
            }
        }

        stage('Run Container (test)') {
            steps {
                sh """
                    docker rm -f student-management || true
                    docker run -d --name student-management \
                      -p 8081:8080 \
                      ${IMAGE_NAME}:${IMAGE_TAG}
                """
            }
        }
    }
}
