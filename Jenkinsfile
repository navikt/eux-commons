#!groovy


node {

    def application = "eux-commons"

    try {
        stage("Checkout") {
            scmInfo = checkout scm

            branchName = resolveBranchName(scmInfo.GIT_BRANCH.toString())
            commitId = scmInfo.GIT_COMMIT

            commit = sh(script: "git log -1 --oneline", returnStdout: true)
        }

        stage("Build application") {
                sh "mvn clean package -B -e -U"
        }
        stage("Publish to Nexus") {                
                    sh "mvn deploy -X"                
        }
        
    } catch (e) {
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

def resolveBranchName(String branchName) {
    if (branchName.contains('/')) {
        split = branchName.split('/')
        branchName = split[split.length - 1]
    }

    return branchName
}
