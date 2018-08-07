package com.automation
public class AutomationBuilder {
    static def ExecuteAutomation(String suiteName) {
        script {
            echo "Executing suite: " + suiteName
        }
    }
}