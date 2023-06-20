kubectl apply -f ./dashboard/dashboard.deployment.yaml
kubectl apply -f ./dashboard/dashboard.config.yaml

@REM Run this to start to dashboard proxy
@REM kubectl proxy

@REM Run this to get token
kubectl -n kubernetes-dashboard create token admin-user
