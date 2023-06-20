kubectl delete -f ./doc-ingress/doc-kong.yaml
kubectl delete -f ./doc-ingress/doc-ingress.yaml

kubectl delete -f ./doc-keycloak/doc-keycloak.config.yaml
kubectl delete -f ./doc-keycloak/doc-keycloak.deployment.yaml
kubectl delete -f ./doc-keycloak/doc-keycloak.secret.yaml

kubectl delete -f ./doc-db/doc-db.config.yaml
kubectl delete -f ./doc-db/doc-db.secret.yaml

kubectl delete -f ./doc-main-service/doc-main-service.config.yaml
kubectl delete -f ./doc-main-service/doc-main-service.deployment.yaml
kubectl delete -f ./doc-main-service/doc-main-service.secret.yaml

kubectl delete -f ./doc-rabbitmq/doc-rabbitmq.config.yaml
kubectl delete -f ./doc-rabbitmq/doc-rabbitmq.deployment.yaml
kubectl delete -f ./doc-rabbitmq/doc-rabbitmq.secret.yaml

kubectl delete -f ./doc-file-service/doc-file-service.config.yaml
kubectl delete -f ./doc-file-service/doc-file-service.secret.yaml
kubectl delete -f ./doc-file-service/doc-file-service.deployment.yaml

kubectl delete -f ./doc-main-service/doc-front.deployment.yaml
