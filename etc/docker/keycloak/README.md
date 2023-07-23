# prj-doc-keycloak custom image

This is a custom Keycloak image that contains a custom User Federation.

## Prerequisites

Before getting started, make sure you have the following tools installed:

- [Docker](https://www.docker.com/)
- [IntelliJ](https://www.jetbrains.com/idea/) (Please use IntelliJ as some configurations can only be used in IntelliJ)
- [Eclipse Temurin JDK 17.0.5](https://adoptium.net/releases.html?variant=openjdk17&jvmVariant=hotspot) (or any 17 version, but it is recommended to use the proposed version)
- [Maven](https://maven.apache.org/)

## Getting Started

To use custom User Federation and get the project preconfigured in Keycloak, follow these steps:

1. Run the Dockerfile in `etc/docker/keycloak`:
    - The default Dockerfile uses deployment configuration.
    - If you want to use development configuration, switch the `deployment-realm.json` to `realm-export.json`.

2. Custom Keycloak User Federation:
   There is a package called `providers` that contains a .jar file, which is a custom Keycloak User Federation file.
    - If you want to use your own custom User Federation, put your file in the "providers" package, and then place it in `/opt/keycloak/providers` of the container.

Note:
- You can find the source-code of the custom User Federation at https://github.com/hcmus-k19-doc/doc-user-provider

## Contact
- [Nguyen Duc Nam](https://github.com/namworkmc)
- [Hoang Nhu Thanh](https://github.com/thanhhoang4869)
- [Le Ngoc Minh Nhat](https://github.com/minhnhat02122001)
- [Hoang Huu Giap](https://github.com/hhgiap241)
- 
