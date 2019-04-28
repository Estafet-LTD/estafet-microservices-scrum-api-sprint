node('maven') {

	def project = "dev"
	def microservice = "sprint-api"
	
	currentBuild.description = "Build a container from the source, then execute unit and container integration tests before promoting the container as a release candidate for acceptance testing."

	stage("checkout") {
		git branch: "master", url: "https://github.com/Estafet-LTD/estafet-microservices-scrum-api-sprint"
	}

	stage("unit tests") {
		withMaven(mavenSettingsConfig: 'microservices-scrum') {
	      sh "mvn clean test"
	    } 
	}

	stage("reset a-mq to purge topics") {
		openshiftDeploy namespace: project, depCfg: "broker-amq", showBuildLogs: "true",  waitTime: "3000000"
		openshiftVerifyDeployment namespace: project, depCfg: "broker-amq", replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"
	}

	stage("build & deploy container") {
		sh "oc start-build ${project} --follow --wait --env JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616 -n ${project}"
		sleep time:120 
	}

	stage("execute the container tests") {
		withEnv(
			[	"SPRINT_API_JDBC_URL=jdbc:postgresql://postgresql.${project}.svc:5432/${project}-${microservice}", 
				"SPRINT_API_DB_USER=postgres", 
				"SPRINT_API_DB_PASSWORD=welcome1",
				"SPRINT_API_SERVICE_URI=http://${microservice}.${project}.svc:8080",
				"JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616",
				"JBOSS_A_MQ_BROKER_USER=amq",
				"JBOSS_A_MQ_BROKER_PASSWORD=amq"
			]) {
			withMaven(mavenSettingsConfig: 'microservices-scrum') {
				try {
					sh "mvn clean verify -P integration-test"
				} finally {
					sh "oc set env dc/${microservice} JBOSS_A_MQ_BROKER_URL=tcp://localhost:61616 -n ${project}"
				}
    		} 
    	}
	}
	
	stage("deploy snapshots") {
		withMaven(mavenSettingsConfig: 'microservices-scrum') {
 			sh "mvn clean deploy -Dmaven.test.skip=true"
		} 
	}	
	
	stage("promote to test") {
		openshiftTag namespace: project, srcStream: microservice, srcTag: 'latest', destinationNamespace: 'test', destinationStream: microservice, destinationTag: 'PrepareForTesting'
	}

}

