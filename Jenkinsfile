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
        stage('Build and build') {
            steps {
                script {
                    sh '''
                    mvn install -DskipTests=true -Dmaven.test.failure.ignore=true
                    docker build -t hcmusdoc/doc-main-service:${BUILD_TAG} .
                    '''

                    withDockerRegistry(credentialsId: 'prj-doc-docker-registry') {
                            sh 'docker push hcmusdoc/doc-main-service:${BUILD_TAG}'
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                echo 'Deploying'
            }
        }
    }
}
