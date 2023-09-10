pipeline {
    agent any
    tools {
        jdk 'eclipse-temurin-17.0.5+8'
        maven 'maven-3.6.0'
    }
    stages {
        stage('Checkout') {
            steps {
                echo "Checking out..."
                git branch: 'main', url: 'https://github.com/namworkmc/doc-main-service'
            }
        }
        stage('Build') {
            steps {
                label 'doc-main-service-docker'
                sh '''
                    mvn install -Dmaven.test.failure.ignore=true
                    docker build -t hcmusdoc/doc-main-service . 
                '''
            }
        }
    }
}
