mvn install -DskipTests=true -DskipTypescriptGenerator=true -Dmaven.test.failure.ignore=true
docker rmi hcmusdoc/doc-main-service
docker build -t hcmusdoc/doc-main-service .
