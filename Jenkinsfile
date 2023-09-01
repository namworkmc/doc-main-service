pipeline {
    agent {
        dockerContainer {
            image 'maven:3.9.3-eclipse-temurin-17'
        }
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
                sh 'mvn install -Dmaven.test.failure.ignore=true'
            }
        }
        stage('Deliver') {
            steps {
                echo 'Deliver....'
                sh '''
                echo "doing delivery stuff.."
                '''
            }
        }
    }
}
