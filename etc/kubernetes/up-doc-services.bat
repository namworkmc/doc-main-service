kubectl apply -f ./doc-ingress/doc-kong.yaml
kubectl apply -f ./doc-ingress/doc-ingress.yaml

kubectl apply -f ./doc-keycloak/doc-keycloak.config.yaml
kubectl apply -f ./doc-keycloak/doc-keycloak.secret.yaml
kubectl apply -f ./doc-keycloak/doc-keycloak.deployment.yaml
kubectl apply -f ./doc-keycloak/doc-keycloak.database.yaml

kubectl apply -f ./doc-db/doc-db.config.yaml
kubectl apply -f ./doc-db/doc-db.secret.yaml

kubectl apply -f ./doc-rabbitmq/doc-rabbitmq.config.yaml
kubectl apply -f ./doc-rabbitmq/doc-rabbitmq.secret.yaml
kubectl apply -f ./doc-rabbitmq/doc-rabbitmq.deployment.yaml

kubectl apply -f ./doc-main-service/doc-main-service.config.yaml
kubectl apply -f ./doc-main-service/doc-main-service.secret.yaml
kubectl apply -f ./doc-main-service/doc-main-service.deployment.yaml

kubectl apply -f ./doc-file-service/doc-file-service.config.yaml
kubectl apply -f ./doc-file-service/doc-file-service.secret.yaml
kubectl apply -f ./doc-file-service/doc-file-service.deployment.yaml

kubectl apply -f ./doc-main-service/doc-front.deployment.yaml
