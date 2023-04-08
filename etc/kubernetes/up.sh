#! /bin/bash

# doc-keycloak
kubectl apply -f ./doc-keycloak/doc-keycloak.config.yaml
kubectl apply -f ./doc-keycloak/doc-keycloak.secret.yaml

# doc-main-postgres
kubectl apply -f ./doc-main-postgres/doc-main-postgres.config.yaml
kubectl apply -f ./doc-main-postgres/doc-main-postgres.deployment.yaml
kubectl apply -f ./doc-main-postgres/doc-main-postgres.secret.yaml

# doc-main-service
kubectl apply -f ./doc-main-service/doc-main-service.config.yaml
kubectl apply -f ./doc-main-service/doc-main-service.deployment.yaml
kubectl apply -f ./doc-main-service/doc-main-service.secret.yaml

# doc-rabbitmq
kubectl apply -f ./doc-rabbitmq/doc-rabbitmq.config.yaml
kubectl apply -f ./doc-rabbitmq/doc-rabbitmq.deployment.yaml
kubectl apply -f ./doc-rabbitmq/doc-rabbitmq.secret.yaml
