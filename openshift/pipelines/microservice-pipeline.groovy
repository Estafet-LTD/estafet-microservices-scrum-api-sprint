@NonCPS
def getVersions(json) {
	def tags = new groovy.json.JsonSlurper().parseText(json).status.tags
	def versions = []
	for (int i = 0; i < tags.size(); i++) {
		versions << tags[i]['tag']
	}
	return versions
}

def recentVersion(versions) {
	def size = versions.size()
	return versions[size-1]
}

def getLatestVersion(microservice) {
	sh "oc get is ${microservice} -o json -n cicd > image.json"
	def image = readFile('image.json')
	def versions = getVersions(image)
	if (versions.size() == 0) {
		error("There are no images for ${microservice}")
	}
	return recentVersion(versions)
}

node("maven") {

	properties([
	  parameters([
	     string(name: 'GITHUB'), string(name: 'PROJECT'),
	  ])
	])

	def project = params.GITHUB
	def microservice = "sprint-api"	
	def version

	stage("get the latest version") {
		version = getLatestVersion microservice
	}

	stage("checkout") {
		checkout scm: [$class: 'GitSCM', 
      userRemoteConfigs: [[url: "https://github.com/${params.GITHUB}/estafet-microservices-scrum-api-sprint"]], 
      branches: [[name: "refs/tags/${version}"]]], changelog: false, poll: false		
	}
	
	stage("prepare the database") {
		withMaven(mavenSettingsConfig: 'microservices-scrum') {
	      sh "mvn clean package -P prepare-db -Dmaven.test.skip=true -Dproject=${project}"
	    } 
	}

	stage("create deployment config") {
		sh "oc process -n ${project} -f openshift/templates/${microservice}-config.yml -p NAMESPACE=${project} -p ENV=${env} -p DOCKER_NAMESPACE=${project} -p DOCKER_IMAGE_LABEL=${version} | oc apply -f -"
		sh "oc set env dc/${env}${microservice} JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.mq-${env}.svc:61616 JAEGER_AGENT_HOST=jaeger-agent.${project}.svc JAEGER_SAMPLER_MANAGER_HOST_PORT=jaeger-agent.${project}.svc:5778 JAEGER_SAMPLER_PARAM=1 JAEGER_SAMPLER_TYPE=const -n ${project}"	
	}
	
	stage("execute deployment") {
		openshiftDeploy namespace: project, depCfg: "${env}${microservice}",  waitTime: "3000000"
		openshiftVerifyDeployment namespace: project, depCfg: "${env}${microservice}", replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000" 
	}

}

