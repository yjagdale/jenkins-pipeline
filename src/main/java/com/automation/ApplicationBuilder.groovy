package com.automation
public class ApplicationBuilder {
    public static def buildApplication(boolean withUnitTest) {
        script {
            echo "Building Application Builder. Build Application"
            def dockerHome = tool 'dockerTool'
            if (isUnix()) {
                sh "sudo '${dockerHome}/bin/docker' pull elgalu/selenium;sudo '${dockerHome}/bin/docker' pull dosel/zalenium"
                try {
                    sh "sudo '${dockerHome}/bin/docker' run -d --rm -ti --name zalenium -p 4444:4444 -v /var/run/docker.sock:/var/run/docker.sock -v /tmp/videos:/home/seluser/videos --privileged dosel/zalenium start"
                    echo 'docker started'
                } catch (Exception ee) {
                    echo 'Docker was already running'
                }
            }
        }
    }
}