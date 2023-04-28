
pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {
        stage('initialize') {
      steps {
        sh 'mvn -v'
        sh 'java -version'
        sh 'git --version'
        sh 'docker -v'
      }
        }

        stage('pull') {
      steps {
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/splitscale/mockie.git']]])
      }
        }

        stage('test') {
      steps {
        script {
          sh 'mvn clean test'
        }
      }
        }

        stage('install') {
      steps {
        script {
          sh 'mvn clean install'
        }
      }
        }

        stage('build docker image') {
      steps {
        sh 'docker build -t kasutu/mockie:latest .'
      }
        }

    stage('upload to docker hub') {
      steps {
        script {
          withCredentials([usernamePassword(credentialsId: 'docker-pwd', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
            sh "docker login -u ${USERNAME} -p ${PASSWORD}"
          }

          sh 'docker push kasutu/mockie:latest'
        }
      }
    }

        stage('deploy') {
          steps {
            script {
              try {
            runServer()
                        } catch (Exception e) {
            sh 'docker stop mockie'
            sh 'docker rm mockie'
            runServer()
              }
            }
          }
        }
    }
}

def runServer() {
  sh 'docker run --name mockie -p 5050:8080 -d kasutu/mockie:latest'
}