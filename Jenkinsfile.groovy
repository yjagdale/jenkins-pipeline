pipeline {
    agent any
    environment {
        EMAIL_RECIPIENTS = 'jagdale0210@gmail.com'
    }
    parameters {
        string(defaultValue: "http://localhost:4444/wd/hub", description: '', name: 'HUB')
    }

    stages {
        stage('Prepration') {
            steps {
                prepareExecution("APP_CODE", "https://github.com/yjagdale/springboot.git")
            }
        }
        stage('Build') {
            steps {
                BuildApplication(true);
            }
        }

        stage('Dev Deployment') {
            steps {
                deployApplication('Dev')
            }
        }
        stage ('Prepare testing env') {
            steps {
                prepareExecution("QA", "https://yjagdale@bitbucket.org/yjagdale/loom-automation.git")
            }
        }

        stage('Automation:- Dev') {
            steps {
                AutomationSuiteRunner('Dev')
            }
        }
        stage('Test Deployment') {
            steps {
                deployApplication('Test')
            }
        }

        stage('Automation:- Test') {
            steps {
                AutomationSuiteRunner('Test')
            }
        }
        stage('Stage Deployment') {
            steps {
                deployApplication('Staging')
            }
        }

        stage('Automation:- Stage') {
            steps {
                AutomationSuiteRunner('Staging')
            }
        }
        stage('Prod Deployment') {
            steps {
                deployApplication('Production')
            }
        }
    }
    post {
        always {
            script {
                echo "always"
            }
        }
        success {
            sendEmail("Successful")
        }
        unstable {
            sendEmail("Unstable")
        }
        failure {
            sendEmail("Failed")
        }
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '3'))
        timeout(time: 25, unit: 'MINUTES')
    }
}

def BuildApplication(boolean withTest) {
    script {
        if (isUnix()) {
            BuildApplicationUnix(withTest)
        } else {
            BuildApplicationWindows(withTest);
        }
        sh "echo 'Running application build with" + withTest + "'"
        sh "sleep 10"
    }
}

def BuildApplicationWindows(boolean withTest) {
    def mvnHome = tool 'Maven 3.3.9'
    bat(/"${mvnHome}\bin\mvn" -Dintegration-tests.skip=true clean package/)
    def pom = readMavenPom file: 'pom.xml'
    print pom.version
    junit '**//*target/surefire-reports/TEST-*.xml'
    archive 'target*//*.jar'
}

def BuildApplicationUnix(boolean withTest) {
    def mvnHome = tool 'Maven 3.3.9'
    def targetVersion = getDevVersion()
    echo "target build version..."
    print targetVersion
    sh "'${mvnHome}/bin/mvn' -DskiptTest=", withTest, " -Dbuild.number = ${ targetVersion } clean package "
    def pom = readMavenPom file: 'pom.xml'
    developmentArtifactVersion = "${pom.version}-${targetVersion}"
    echo pom.version
    if(withTest) {
        junit '**//*target/surefire-reports/TEST-*.xml'
        archiveArtifacts 'target*//*.jar'
    }
}


def deployApplication(String env) {
    script {
        sh "echo 'Running application deployment " + env + "'";
        sh "sleep 10"
    }
}

def runAutomation(String suiteNumber) {
    script {
        echo "Running automation of " + suiteNumber
        sh "sleep 10"
    }
}

def AutomationSuiteRunner(String environment) {
    parallel(
            Suite1: {
                runAutomation(environment + "Suite1")
            },
            Suite2: {
                runAutomation(environment + "Suite2")
            },
            Suite3: {
                runAutomation(environment + "Suite3")
            },
            Suite4: {
                runAutomation(environment + "Suite4")
            },
            Suite5: {
                runAutomation(environment + "Suite5")
            },
            Suite6: {
                runAutomation(environment + "Suite6")
            },
            Suite7: {
                runAutomation(environment + "Suite7")
            },
            Suite8: {
                runAutomation(environment + "Suite8")
            },
            Suite9: {
                runAutomation(environment + "Suite9")
            },
            Suite10: {
                runAutomation(environment + "Suite10")
            },
            Suite11: {
                runAutomation(environment + "Suite11")
            }
    )
}

def sendEmail(String status) {
//    mail(
//            to: "jagdale0210@gmail.com",
//            subject: "Build $BUILD_NUMBER - " + status + " (${currentBuild.fullDisplayName})",
//            body: "Changes:\n " + getChangeString() + "\n\n Check console output at: $BUILD_URL/console" + "\n")
    echo status
}

def prepareExecution(String dirName, String gitUrl) {
    script {
       echo dirName + gitUrl
    }
}

def getDevVersion() {
    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
    def versionNumber
    if (gitCommit == null) {
        versionNumber = env.BUILD_NUMBER
    } else {
        versionNumber = gitCommit.take(8)
    }
    print 'build  versions...'
    print versionNumber
    return versionNumber
}

def getReleaseVersion() {
    def pom = readMavenPom file: 'pom.xml'
    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
    def versionNumber
    if (gitCommit == null) {
        versionNumber = env.BUILD_NUMBER
    } else {
        versionNumber = gitCommit.take(8)
    }
    return pom.version.replace("-SNAPSHOT", ".${versionNumber}")
}
