pipeline {
    agent any

    tools {
        jdk 'JDK17'        // le nom EXACT configuré dans Manage Jenkins > Tools
        maven 'Maven3'     // idem pour Maven
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/raghadkhedhiri/Raghad_Khedhiri_4sae9.git'
            }
        }

        stage('Build & Tests') {
            steps {
                sh 'mvn clean test package'
            }
        }
    }
}
