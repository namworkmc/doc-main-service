#! /bin/bash

@REM Delete doc-main-service image before building new one
docker rm -f doc-main-service
docker rmi hcmusdoc/doc-main-service
docker compose --profile local-deployment up  -d
