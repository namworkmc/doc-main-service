# README

This repository contains instructions to setup and run the `doc-main-service` and `doc-docker` projects.

## Prerequisites

To run these projects, you will need the following installed on your machine:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

## Getting Started

1. Clone the `doc-docker` repository by running the following command in your terminal:

    ```
    git clone https://github.com/hcmus-k19-doc/doc-docker
    ```

2. Clone the `doc-main-service` repository by running the following command in your terminal:

    ```
    git clone https://github.com/hcmus-k19-doc/doc-main-service
    ```

3. Start the docker-compose.yaml file in etc/docker of repo `doc-main-service` by using the following command:

    ```
    docker compose up -d
    ```

4. Start the docker-compose.yaml file in src/main/docker of repo `doc-docker` by using the following command:

    ```
    docker compose up -d
    ```

    If you need to test security.

    Note: If you want to down containers with volumes, run the following command:

    ```
    docker compose down -v
    ```

5. Run the following command in the `doc-main-service` project:

    ```
    clean install -DskipTests=true
    ```

6. Finally, run the `doc-main-service` project.

## Conclusion

You should now be able to run the `doc-main-service` and `doc-docker` projects. If you have any questions or issues, please reach out to the project maintainers.
