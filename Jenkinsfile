pipeline {
    agent any
    triggers {
        githubPush()
    }
    environment {
    }
    stages {
        stage("Deploy") {
            agent {
                docker {
                    image 'maven:3.9.3-amazoncorretto-8'
                    args '-v $HOME/.m2:/var/maven/.m2 -v $HOME/.gnupg:/var/maven/.gnupg -e HOME=/var/maven -e MAVEN_CONFIG=/var/maven/.m2'
                    // Run the container on the node specified at the
                    // top-level of the Pipeline, in the same workspace,
                    // rather than on a new node entirely:
                    reuseNode true
                }
            }
            steps {
                script {
                    withCredentials([file(credentialsId: "settings-xml-file", variable: 'settingsXmlFile')]) {
                        // do something with the file, for instance 
                        sh "mvn deploy -s $settingsXmlFile -Duser.home=/var/maven"
                    }
                }
            }
        }
    }
}