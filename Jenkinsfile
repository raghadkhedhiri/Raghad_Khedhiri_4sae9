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
  }

  options {
    timestamps()
    skipDefaultCheckout(true)
    buildDiscarder(logRotator(numToKeepStr: '20'))
  }

  stages {

    stage('Checkout') {
      steps {
        cleanWs()
        git branch: 'main',
            url: 'https://github.com/raghadkhedhiri/Raghad_Khedhiri_4sae9.git'
      }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn -B clean test'
        sh 'ls -l target/site/jacoco/jacoco.xml || true'
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
            sh """
              mvn -B sonar:sonar \
                -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                -Dsonar.token=$SONAR_TOKEN
            """
          }
        }
      }
    }

    stage('Quality Gate') {
      steps {
        // nécessite le plugin "SonarQube Scanner for Jenkins"
        timeout(time: 5, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
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
        sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
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
          //sh 'docker logout'
        }
      }
    }

    stage('Deploy to Kubernetes (devops)') {
  steps {
    sh """
      set -e
      kubectl -n devops set image deployment/spring-app \
        spring-app=${IMAGE_NAME}:${IMAGE_TAG}
           echo 'Applying Deployments...'
                // Using 'sh' instead of 'bat'
                sh 'kubectl apply -f mysql-deployment.yaml -n devops'
                sh 'kubectl apply -f spring-deployment.yaml -n devops'

                echo 'Restarting Spring Pods...'
                sh 'kubectl rollout restart deployment/spring-app -n devops'
 
    """
  }
}    // kubectl -n devops rollout status deployment/spring-app --timeout=180s
    //  kubectl -n devops get pods -l app=spring-app -o wide


    stage('Run Container (test)') {
      steps {
        sh """
          docker rm -f student-management || true
          docker run -d --name student-management -p 8081:8080 ${IMAGE_NAME}:${IMAGE_TAG}
        """
      }
    }

    stage('Smoke Test (container)') {
      steps {
        sh """
          set -e
          # attendre que l'app réponde
          for i in \$(seq 1 30); do
            curl -fsS http://localhost:8081/actuator/health && exit 0 || true
            sleep 2
          done
          echo "App did not become healthy on :8081"
          docker logs --tail=200 student-management || true
          exit 1
        """
      }
    }

  }

  post {
    always {
      sh 'docker logs --tail=200 student-management || true'
      sh 'docker rm -f student-management || true'
    }
  }
}
