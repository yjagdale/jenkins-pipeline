
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
                stage {
                    BuildApplication(true);
                }
            }
        }

        stage('Deploy application') {
            steps {
                deployApplication('Dev')
            }
        }

        stage('Automation on dev') {
            steps {
                parallel(
                        Suite1: {
                            runAutomation("Suite1")
                        },
                        Suite2: {
                            runAutomation("Suite2")
                        },
                        Suite3: {
                            runAutomation("Suite3")
                        },
                        Suite4: {
                            runAutomation("Suite4")
                        },
                        Suite5: {
                            runAutomation("Suite5")
                        },
                        Suite6: {
                            runAutomation("Suite6")
                        },
                        Suite7: {
                            runAutomation("Suite7")
                        },
                        Suite8: {
                            runAutomation("Suite8")
                        },
                        Suite9: {
                            runAutomation("Suite9")
                        },
                        Suite10: {
                            runAutomation("Suite10")
                        }
                )
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