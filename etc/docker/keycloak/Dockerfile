FROM bitnami/keycloak:20.0.3
WORKDIR /opt/bitnami/keycloak/providers/
ADD ./providers/doc-user-provider-1.0.4.jar ./doc-user-provider-1.0.4.jar
WORKDIR /opt/bitnami/keycloak/data/import
ADD ./realms ./
EXPOSE 8180
