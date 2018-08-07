pipeline {
    agent any
    environment {
        EMAIL_RECIPIENTS = 'jagdale0210@gmail.com'
    }
    parameters {
        string(defaultValue: "http://localhost:4444/wd/hub", description: '', name: 'HUB')
    }

    stages {
        stage('Build Application') {
            steps {
                BuildApplication(true);
            }
        }

        stage('Deploy application on Dev') {
            steps {
                deployApplication('Dev')
            }
        }

        stage('Automation on dev') {
            AutomationSuiteRunner('Dev')
        }
        stage('Deploy application Test') {
            steps {
                deployApplication('Test')
            }
        }

        stage('Automation on Test') {
            AutomationSuiteRunner('Test')
        }
        stage('Deploy application Staging') {
            steps {
                deployApplication('Staging')
            }
        }

        stage('Automation on Staging') {
            AutomationSuiteRunner('Staging')
        }
        stage('Deploy application On Production') {
            steps {
                deployApplication('Production')
            }
        }
    }
}

def BuildApplication(boolean withTest) {
    script {
        sh "echo 'Running application build with" + withTest + "'"
        sh "sleep 10"
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
    steps {
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
}