mvn clean install -P local
cp target/estafet-microservices-scrum-api-sprint-*.war $WILDFLY_INSTALL/standalone/deployments
