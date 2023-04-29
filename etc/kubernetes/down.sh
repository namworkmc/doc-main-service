#! /bin/bash

# ingress
kubectl delete -f ./ingress/doc-kong.yaml
kubectl delete -f ./ingress/doc-ingress.yaml

# doc-keycloak
kubectl delete -f ./doc-keycloak/doc-keycloak.config.yaml
kubectl delete -f ./doc-keycloak/doc-keycloak.deployment.yaml
kubectl delete -f ./doc-keycloak/doc-keycloak.secret.yaml

# doc-main-postgres
kubectl delete -f ./doc-main-postgres/doc-main-postgres.config.yaml
kubectl delete -f ./doc-main-postgres/doc-main-postgres.deployment.yaml
kubectl delete -f ./doc-main-postgres/doc-main-postgres.secret.yaml

# doc-main-service
kubectl delete -f ./doc-main-service/doc-main-service.config.yaml
kubectl delete -f ./doc-main-service/doc-main-service.deployment.yaml
kubectl delete -f ./doc-main-service/doc-main-service.secret.yaml

# doc-rabbitmq
kubectl delete -f ./doc-rabbitmq/doc-rabbitmq.config.yaml
kubectl delete -f ./doc-rabbitmq/doc-rabbitmq.deployment.yaml
kubectl delete -f ./doc-rabbitmq/doc-rabbitmq.secret.yaml
