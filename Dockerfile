# ------------------------------
# Stage 1: Build the application using Gradle
FROM node:18.12.1-alpine AS build-fe
WORKDIR /usr/src/app

COPY itcloud/src/main/frontend ./

RUN npm i
RUN npm run-script build

# ------------------------------
# Stage 2: Build the application using Gradle
FROM gradle:7.4.2-jdk11-focal AS build
WORKDIR /home/gradle/project

# Add labels to the build stage
LABEL maintainer="Chan Hee Lee <chanhi2000@gmail.com>"
LABEL build-stage="gradle"

# Copy the Gradle wrapper and build files
COPY gradle /home/gradle/project/gradle
COPY gradlew /home/gradle/project/
COPY build.gradle.kts /home/gradle/project/
COPY settings.gradle.kts /home/gradle/project/
COPY gradle.properties /home/gradle/project/

# Ensure the gradlew script has execute permissions
RUN chmod +x gradlew

# Copy the rest of the application source code
COPY common /home/gradle/project/common
COPY util /home/gradle/project/util
COPY buildSrc /home/gradle/project/buildSrc
COPY itcloud /home/gradle/project/itcloud

# Copy the node build results to path (ONLY when skipNpm=true)
COPY --from=build-fe /usr/src/app/build /home/bradle/project/itcloud/src/main/resources/static

# Build the application
RUN ./gradlew itcloud:bootJar -Pprofile=prd -PskipNpm=true --parallel

# ------------------------------
# Base image for the runtime stage
FROM eclipse-temurin:11-jdk-focal

# Add labels to the runtime stage
LABEL maintainer="Chan Hee Lee <chanhi2000@gmail.com>"
LABEL description="Spring Boot Docker Image for itcloud project"
LABEL version="0.0.2"
LABEL vcs-url="https://github.com/ITJEONGBO/okestro-demo"
LABEL build-date="2024-07-08"
LABEL commit-hash="abc123def456"
LABEL license="Apache-2.0"
LABEL environment="production"
LABEL app-name="itcloud"

RUN echo "================== common.properties =================="
ARG ITCLOUD_VERSION=0.0.2
ARG ITCLOUD_RELEASE_DATE=2024-07-08
ARG ITCLOUD_PORT_HTTP=8080
ARG ITCLOUD_PORT_HTTPS=8443
ARG ITCLOUD_OVIRT_IP=192.168.0.80
RUN echo "version: $ITCLOUD_VERSION"
RUN echo "release-date: $ITCLOUD_RELEASE_DATE"
RUN echo "http-port: $ITCLOUD_PORT_HTTP"
RUN echo "http-ports: $ITCLOUD_PORT_HTTPS"
RUN echo "ovirt-ip: $ITCLOUD_OVIRT_IP"
RUN echo ""
ENV ITCLOUD_VERSION=${ITCLOUD_VERSION}
ENV ITCLOUD_RELEASE_DATE=${ITCLOUD_RELEASE_DATE}
ENV ITCLOUD_PORT_HTTP=${ITCLOUD_PORT_HTTP}
ENV ITCLOUD_PORT_HTTPS=${ITCLOUD_PORT_HTTPS}
ENV ITCLOUD_OVIRT_IP=${ITCLOUD_OVIRT_IP}

RUN echo "================== database.properties =================="
ARG POSTGRES_JDBC_URL=192.168.0.80
ARG POSTGRES_JDBC_PORT=5432
RUN echo "jdbc:postgresql://$POSTGRES_JDBC_URL:$POSTGRES_JDBC_PORT/engine"
RUN echo "jdbc:postgresql://$POSTGRES_JDBC_URL:$POSTGRES_JDBC_PORT/ovirt_engine_history"
ARG POSTGRES_DATASOURCE_JDBC_ID=okestro
ARG POSTGRES_DATASOURCE_JDBC_PW=okestro2018
RUN echo "access acct: $POSTGRES_DATASOURCE_JDBC_ID / $POSTGRES_DATASOURCE_JDBC_PW"

ENV POSTGRES_JDBC_URL=${POSTGRES_JDBC_URL}
ENV POSTGRES_JDBC_PORT=${POSTGRES_JDBC_PORT}
ENV POSTGRES_DATASOURCE_JDBC_ID=${POSTGRES_DATASOURCE_JDBC_ID}
ENV POSTGRES_DATASOURCE_JDBC_PW=${POSTGRES_DATASOURCE_JDBC_PW}

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot jar file from the build stage
COPY --from=build /home/gradle/project/itcloud/build/libs/*.jar app.jar

EXPOSE 8080

# ENTRYPOINT ["java","-jar","app.jar", "--server.ssl.enabled=true", "--server.ssl.key-store=/app/config/ssl/your-certificate.pem", "--server.ssl.key-store-password=yourpassword"]
# ENTRYPOINT ["java", "-jar", "app.jar", "--server.ssl.enabled=true", "--server.ssl.key-store=/app/config/ssl/your-certificate.pem", "--server.ssl.key-store-password=${SSL_KEY_STORE_PASSWORD}"]
ENTRYPOINT ["java","-jar","app.jar"]