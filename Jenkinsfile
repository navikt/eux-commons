#!groovy


    def VERA_UPDATE_URL = "https://vera.adeo.no/api/v1/deploylog"
    def application = "eux-commons"
    def mvnSettings = "navMavenSettingsUtenProxy"

    try {
        stage("Checkout") {
            scmInfo = checkout scm

            branchName = resolveBranchName(scmInfo.GIT_BRANCH.toString())
            commitId = scmInfo.GIT_COMMIT

            commit = sh(script: "git log -1 --oneline", returnStdout: true)
        }

        stage("Build application") {
            configFileProvider([configFile(fileId: "$mvnSettings", variable: "MAVEN_SETTINGS")]) {
                sh "mvn clean package -B -e -U -s $MAVEN_SETTINGS"
            }
        }

        stage("Publish to Nexus") {
                configFileProvider([configFile(fileId: "$mvnSettings", variable: "MAVEN_SETTINGS")]) {
                    sh "mvn -DskipTests -DdeployAtEnd=true -DretryFailedDeploymentCount=5 --settings $MAVEN_SETTINGS deploy"
                }
        }

        stage ("Send Slack-message") {
            GString message = ":clap: Siste commit på ${branchName} bygd og deployet OK til miljø ${environment}.\nCommit: ${commit}"
            sendSlackMessage("good", message)
        }

    } catch (e) {
        GString message = ":crying_cat_face: \n Siste commit på ${branchName} kunne ikke deployes til ${environment}. Se logg for mer info ${env.BUILD_URL}\nCommit ${commit}"
        sendSlackMessage("danger", message)
        throw e
    }
}

def getBuildUser(defaultUser) {
    def buildUser = defaultUser

    try {
        wrap([$class: 'BuildUser']) {
            buildUser = "${BUILD_USER} (${BUILD_USER_ID})"
        }
    } catch (e) {
        // Dersom bygg er auto-trigget, er ikke BUILD_USER variablene satt => defaultUser benyttes
        return buildUser
    }
}

def replaceInFile(oldString, newString, file) {
    sh "sed -i -e 's/${oldString}/${newString}/g' ${file}"
}

def sendSlackMessage(String color, String message) {

    try {
        slackSend color: color, message: message, tokenCredentialId: "melosys-slack-token"
    } catch (Exception exception) {
        echo("Failed to send message to Slack: ${exception.getMessage()}")
    }
}

def resolveBranchName(String branchName) {
    if (branchName.contains('/')) {
        split = branchName.split('/')
        branchName = split[split.length - 1]
    }

    return branchName
}
