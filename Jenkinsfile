pipeline {
    environment {
        registry = "davisballen/scandit_eureka"
        registryCredential = 'dockerhub'
    }
    agent {
        docker {
            image 'maven:3.6.2-jdk-8'
            args ' --privileged -u="root" -v /tmp/.m2:/root/.m2 -v /usr/bin/docker:/usr/bin/docker -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                dir("eureka-server/") {
                    sh 'mvn -B -DskipTests clean package'
                }
            }
        }
        stage('Test') {
            steps {
              dir("eureka-server/") {
                  sh 'mvn test'
              }
            }
        }
        stage('Deliver') {
            steps {
                dir("eureka-server/") {
                    script {
                        docker.build registry + ":0.0.1-SNAPSHOT"
                    }
                }
            }
        }
    }
}
