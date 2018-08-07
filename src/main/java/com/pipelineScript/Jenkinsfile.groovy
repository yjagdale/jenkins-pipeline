package com.pipelineScript

import com.automation.ApplicationBuilder
import com.automation.AutomationBuilder
import com.automation.Utilites


pipeline {
    // run on jenkins nodes tha has java 8 label
    agent any
    final Utilites utilites = new Utilites();
    // global env variables
    environment {
        EMAIL_RECIPIENTS = 'jagdale0210@gmail.com'
    }
    parameters {
        string(defaultValue: "http://localhost:4444/wd/hub", description: '', name: 'HUB')
    }
    stages {
        stage('Build Application') {
            ApplicationBuilder.buildApplication(true);
        }
        stage ('Automation') {
            AutomationBuilder.ExecuteAutomation("Suite 1");
            utilites.sendEmail("Yashwant")
        }
    }

}