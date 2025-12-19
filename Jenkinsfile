pipeline {
    agent any

    tools {
        jdk   'JDK17'
        maven 'Maven3'
    }

    environment {
        IMAGE_NAME = 'raghadkhedhiri/student-management'
        IMAGE_TAG  = '1.3'
        SONAR_PROJECT_KEY = 'student-management'
        DOCKER_CREDENTIALS_ID = 'dockerhub-cred'
    }

 

    stages {
        stage('Checkout') {
            steps {
                cleanWs()
                git branch: 'main', url: 'https://github.com/raghadkhedhiri/Raghad_Khedhiri_4sae9.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn -B clean test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                    archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
                }
            }
        }

        stage('SonarQube') {
            steps {
                withSonarQubeEnv('sonarqube-local') {
                    withCredentials([string(credentialsId: 'sonar-token-2', variable: 'SONAR_TOKEN')]) {
                        sh "mvn -B sonar:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY} -Dsonar.token=${SONAR_TOKEN}"
                    }
                }
            }
        }

     
    

        stage('Docker Build & Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                    sh "echo \$PASS | docker login -u \$USER --password-stdin"
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo 'Generating and Applying K8s Manifests...'
                    
                    // On crée le fichier YAML pour MySQL (si nécessaire)
                    writeFile file: 'mysql-deployment.yaml', text: """
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
spec:
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - image: mysql:8.0
        name: mysql
        env:
        - name: MYSQL_ROOT_PASSWORD
          value: root123
        - name: MYSQL_DATABASE
          value: springdb
"""
                    // On applique les fichiers
                    sh 'kubectl apply -f mysql-deployment.yaml -n devops'
                    
                    // On met à jour l'image de l'application Spring
                    sh """
                        kubectl -n devops set image deployment/spring-app \
                        spring-app=${IMAGE_NAME}:${IMAGE_TAG}
                        kubectl -n devops rollout status deployment/spring-app --timeout=180s
                    """
                }
            }
        }

        stage('Smoke Test (Local)') {
            steps {
                sh """
                    docker rm -f student-management || true
                    docker run -d --name student-management -p 8081:8080 ${IMAGE_NAME}:${IMAGE_TAG}
                    sleep 10
                    curl -fsS http://localhost:8081/actuator/health || (docker logs student-management && exit 1)
                """
            }
        }
    }

    post {
        always {
            sh 'docker rm -f student-management || true'
            sh 'docker logout'
        }
        success {
            echo "✔ Pipeline terminé avec succès !"
        }
    }
}
