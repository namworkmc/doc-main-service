# doc-main-service

The doc-main-service is an integral component of a microservices architecture, specifically designed for managing the **Document Publication and Approval System**.
This service plays a crucial role in facilitating the seamless flow of uploading and downloading files, while the actual storage and retrieval of documents are handled by another dedicated service.

## Prerequisites

Before getting started, make sure you have the following tools installed:

1. [Docker](https://www.docker.com/)
2. [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/) (Please use IntelliJ as some configurations are specific to this IDE)
3. [Eclipse Temurin JDK 17.0.5](https://adoptium.net/releases.html?variant=openjdk17&jvmVariant=hotspot) (or any 17 version, but it is recommended to use the proposed version)
4. [Maven](https://maven.apache.org/)

## Getting Started

Follow the steps below to set up and use the source code:

1. Clone the repository using the following command: 
    ```
    git clone https://github.com/hcmus-k19-doc/doc-main-service
    ```
2. Start `docker-compose.yaml` file located in `etc/docker` using the following command:
    ```
    docker compose up -d
    ```

Note:
- If you are using an older version of Docker, use:
    ```
    docker-compose up -d
    ```
- To stop the Docker containers defined in `docker-compose.yaml`, you can use:
  ```
  docker compose stop
  ```
- If you want to remove the containers along with the volumes, you can use:
  ```
  docker compose down -v
  ```
**Be careful when using this command as it will permanently remove persisted volumes**!!!

3. Before using the source code, you need to build it first (this is a required step, not optional). Run the following Maven command:
    ```
    mvn clean install -DskipTests=true
    ```
Note:
- If you want to run the unit tests, remove the `-DskipTests=true` flag.
- If you want to skip the TypeScript generator, add the `-DskipTypescriptGenerator=true` flag.

4. To start the project, you will need to set up PostgreSQL database, RabbitMQ, and Keycloak (our customized Keycloak, [refer](https://github.com/hcmus-k19-doc/doc-keycloak) for more information), and configure the project to integrate with these services. You can set up the properties in the `application-dev.yaml` file located in the `module service`, package `src\main\resources`.
5. We also provide `liquibase` scripts to create the database schema, you can find them in the `module db`.
To run the scripts, you will need to enable `liquibase` and configure the database you want to use in the `application-dev.yaml` file.

You should now be able to run the project.

## Run the Project on Docker

Follow these steps to run the project using Docker:

1. Run the `build-image.bat` file in the root of the project to build the image. If the file cannot run completely, please copy and paste each command in `build-image.bat` to your terminal to build the image.

2. Start the `docker-compose.yaml` file located in `etc/docker` using the following command:
```
docker compose --profile local-deployment up -d
```

**Note: Remember to configure PostgreSQL database, RabbitMQ, and Keycloak (our customized Keycloak, [refer](https://github.com/hcmus-k19-doc/doc-keycloak) for more information) before starting the project.**

## Run the Project on Kubernetes

Before running the project on Kubernetes, make sure you have `minikube` or `Kubernetes` on Docker installed.

Follow these steps to run the project on Kubernetes:

1. Run the `up-doc-services.bat` file.

## Contact
- [Nguyen Duc Nam](https://github.com/namworkmc)
- [Hoang Nhu Thanh](https://github.com/thanhhoang4869)
- [Le Ngoc Minh Nhat](https://github.com/minhnhat02122001)
- [Hoang Huu Giap](https://github.com/hhgiap241)
