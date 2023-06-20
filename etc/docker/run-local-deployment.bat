docker rm -f doc-main-service
docker rmi hcmusdoc/doc-main-service
docker compose --profile local-deployment up  -d
