pipeline {
    agent any

    tools {
        jdk 'JDK17'       // le nom que tu as mis dans "Manage Jenkins > Tools"
        maven 'Maven3'    // idem pour Maven
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/raghadkhedhiri/Raghad_Khedhiri_4sae9.git'
            }
        }

        stage('Build & Package') {
            steps {
                
                sh 'mvn -B clean package -DskipTests'
            }
        }
    }
}
