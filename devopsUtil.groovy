#!/usr/bin/env groovy

def linuxBuildBadge() {
    return addEmbeddableBadgeConfiguration(id: "linuxbuild", subject: "Linux Build");
}

def OPTIONS_REPORTS(value){
    return [junitPublisher(disabled: value),
            findbugsPublisher(disabled: value),
            openTasksPublisher(disabled: value),
            dependenciesFingerprintPublisher(disabled: value), concordionPublisher(disabled: value),
            invokerPublisher(disabled: value), jacocoPublisher(disabled: value),
            mavenLinkerPublisher(disabled: value), pipelineGraphPublisher(disabled: value)];
}

def discordChannel(){

    Map map = [:]
    map = [
            'NOME_PRODUTO' : ['TOKEN_CANAL1', 'TOKEN_CANAL2']
    ]
    return map
}

def lastCommitMessage() {
    def data = sh(returnStdout: true, script: "git log -1 --pretty=%B").trim()
    return data
}

def authorCommit() {
    def data = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%an <%ae>'").trim()
    return data
}

def lastCommitId() {
    def data = sh(returnStdout: true, script: "git rev-parse HEAD").trim()
    return data
}

def urlDiscord(){
    def data = discordChannel().find{ it -> env.JOB_BASE_NAME.contains(it.key) }?.value
    if(data)
        return data
    else discordChannel().get('default')

}

/**
 * State: running, success, failed,
 * @param token
 * @param status
 * @return
 */
def gitLabStatus(token, status){
    withCredentials([string(credentialsId: ${token}, variable: 'token')]) {
        sh 'curl --request POST --header "PRIVATE-TOKEN:  ${token}" $GITLAB_PROJECT_PATH"/statuses/$(git rev-parse HEAD)?state=${status}"'
    }
}

def mailNotification() {
    emailext subject: "[Notificação de BUILD] " + currentBuild.currentResult + " : " + env.JOB_NAME,
            body: '''${SCRIPT, template="groovy-html"}''',
            recipientProviders: [
                    [$class: 'CulpritsRecipientProvider'],
                    [$class: 'DevelopersRecipientProvider'],
                    [$class: 'RequesterRecipientProvider']
            ],
            replyTo: '$DEFAULT_REPLYTO',
            to: '$DEFAULT_RECIPIENTS',
            mimeType: 'text/html'
}

def sendNotification(project) {

    def url = 'https://discordapp.com/api/webhooks/';
    def discordImageSuccess = 'https://www.jenkins.io/images/logos/formal/256.png'
    def discordImageError = 'https://www.jenkins.io/images/logos/fire/256.png'

    def discordDesc =
            "Result: ${currentBuild.currentResult}\n" +
                    "Project: ${project}\n" +
                    "Commit: ${lastCommitId()}\n" +
                    "Author: ${authorCommit()}\n" +
                    "Message: ${lastCommitMessage()}\n"+
                    "Duration: ${currentBuild.durationString}"

    def discordFooter = "${env.JOB_BASE_NAME} (#${BUILD_NUMBER})"
    def discordTitle = "${env.JOB_BASE_NAME} (build #${BUILD_NUMBER})"

    urlDiscord().each{ value ->
        discordSend description: discordDesc,
                footer: discordFooter,
                link: env.JOB_URL,
                result: currentBuild.currentResult,
                title: discordTitle,
                webhookURL: url+value,
                successful: currentBuild.resultIsBetterOrEqualTo('SUCCESS'),
                thumbnail: 'SUCCESS'.equals(currentBuild.currentResult) ? discordImageSuccess : discordImageError
    }
}




