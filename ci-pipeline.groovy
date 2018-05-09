node("maven") {

	def project = "dev"
	def microservice = "sprint-api"

	stage("checkout") {
		git branch: "master", url: "https://github.com/Estafet-LTD/estafet-microservices-scrum-api-sprint"
	}

	stage("build and execute unit tests") {
		sh "mvn clean test"
		junit "**/target/surefire-reports/*.xml"
	}

	stage("update the database schema") {
		sh "oc get pods --selector app=postgresql -o json -n ${project} > pods.json"
		def json = readFile('pods.json');
		def pod = new groovy.json.JsonSlurper().parseText(json).items[0].metadata.name
		sh "oc rsync --no-perms=true --include=\"*.ddl\" --exclude=\"*\" ./ ${pod}:/tmp -n dev"
		sh "oc exec ${pod}  -n ${project} -- /bin/sh -i -c \"psql -d ${microservice} -U postgres -f /tmp/drop-${microservice}-db.ddl\""
		sh "oc exec ${pod}  -n ${project} -- /bin/sh -i -c \"psql -d ${microservice} -U postgres -f /tmp/create-${microservice}-db.ddl\""
	}

	stage("build & deploy container") {
		openshiftBuild namespace: project, buildConfig: microservice, showBuildLogs: "true",  waitTime: "3000000"
		sh "oc set env dc/${microservice} JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616 -n ${project}"
	}
  	  
	stage("verify container deployment") {
		openshiftVerifyDeployment namespace: project, depCfg: microservice, replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"	
	}

	stage("execute the container tests") {
		withEnv(
			[	"SPRINT_API_JDBC_URL=jdbc:postgresql://postgresql.${project}.svc:5432/${microservice}", 
				"SPRINT_API_DB_USER=postgres", 
				"SPRINT_API_DB_PASSWORD=welcome1",
				"SPRINT_API_SERVICE_URI=http://${microservice}-api.${project}.svc:8080",
				"JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616",
				"JBOSS_A_MQ_BROKER_USER=amq",
				"JBOSS_A_MQ_BROKER_PASSWORD=amq"
			]) {
			sh "mvn verify -P integration-test"
		}
		sh "oc set env dc/${microservice} JBOSS_A_MQ_BROKER_URL=tcp://localhost:61616 -n ${project}"
		junit "**/target/failsafe-reports/*.xml"
	}

}

