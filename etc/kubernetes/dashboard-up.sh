#! /bin/bash

kubectl apply -f ./dashboard/dashboard.deployment.yaml
kubectl apply -f ./dashboard/dashboard.config.yaml

#kubectl proxy
#kubectl -n kubernetes-dashboard create token admin-user
