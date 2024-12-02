# ------------------------------
# Stage 1: Build the application using Node
FROM node:18.12.1-alpine AS build-fe
WORKDIR /usr/src/app

COPY itcloud/src/main/frontend ./

RUN npm install
RUN npm run build

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
COPY util-ovirt /home/gradle/project/util-ovirt
COPY buildSrc /home/gradle/project/buildSrc
COPY itcloud /home/gradle/project/itcloud

# Copy the node build results to path (ONLY when skipNpm=true)
COPY --from=build-fe /usr/src/app/build /home/gradle/project/itcloud/src/main/resources/static
RUN ls /home/gradle/project/itcloud/src/main/resources/static

# Build the application
RUN ./gradlew itcloud:bootJar -Pprofile=prd -PskipNpm=true --parallel

# ------------------------------
# Base image for the runtime stage
FROM eclipse-temurin:11-jdk-focal

# Add labels to the runtime stage
LABEL maintainer="Chan Hee Lee <chanhi2000@gmail.com>"
LABEL description="RutilVM"
LABEL version="0.1.2"
LABEL vcs-url="https://github.com/ITJEONGBO/okestro-demo"
LABEL build-date="2024-12-02"
LABEL commit-hash="05f9e12ee27c1532623112d1df92004226d0d347"
LABEL license="Apache-2.0"
LABEL environment="production"
LABEL app-name="itcloud"

ENV ITCLOUD_VERSION=0.1.2
ENV ITCLOUD_RELEASE_DATE=2024-12-02
ENV ITCLOUD_PORT_HTTP=8080
ENV ITCLOUD_PORT_HTTPS=8443
ENV ITCLOUD_OVIRT_IP=192.168.0.70
RUN echo "================== common.properties =================="
RUN echo ""
RUN echo "version: $ITCLOUD_VERSION"
RUN echo "release-date: $ITCLOUD_RELEASE_DATE"
RUN echo "http-port: $ITCLOUD_PORT_HTTP"
RUN echo "http-ports: $ITCLOUD_PORT_HTTPS"
RUN echo "ovirt-ip: $ITCLOUD_OVIRT_IP"
RUN echo ""

ENV POSTGRES_JDBC_URL=192.168.0.70
ENV POSTGRES_JDBC_PORT=5432
ENV POSTGRES_DATASOURCE_JDBC_ID=rutil
ENV POSTGRES_DATASOURCE_JDBC_PW=rutil1!
ENV POSTGRES_JDBC_URL_ENGINE="jdbc:postgresql://$POSTGRES_JDBC_URL:$POSTGRES_JDBC_PORT/ovirt_engine_history"
ENV POSTGRES_JDBC_URL_HISTORY="jdbc:postgresql://$POSTGRES_JDBC_URL:$POSTGRES_JDBC_PORT/engine?currentSchema=public"
ENV POSTGRES_JDBC_URL_AAA="jdbc:postgresql://$POSTGRES_JDBC_URL:$POSTGRES_JDBC_PORT/engine?currentSchema=aaa_jdbc"
RUN echo "================== database.properties =================="
RUN echo ""
RUN echo ""

ENV ITCLOUD_SSL_ENABLED=${ITCLOUD_SSL_ENABLED}
ENV ITCLOUD_SSL_FILE=${ITCLOUD_SSL_FILE}
ENV ITCLOUD_SSL_PASSWORD=${ITCLOUD_SSL_PASSWORD}
ENV ITCLOUD_SSL_ALIAS=${ITCLOUD_SSL_ALIAS}
RUN echo "================== application.properties =================="
RUN echo ""
RUN echo "application.version: $ITCLOUD_VERSION"
RUN echo ""
RUN echo "server.ssl.enabled: $ITCLOUD_SSL_ENABLED"
RUN echo "server.ssl.key-store:"
RUN echo "server.ssl.key-store-password: $ITCLOUD_SSL_PASSWORD"
RUN echo "server.ssl.key-alias: $ITCLOUD_SSL_ALIAS"
RUN echo ""
RUN echo "spring.datasource.engine.url: $POSTGRES_JDBC_URL_ENGINE"
RUN echo "spring.datasource.engine.username: $POSTGRES_DATASOURCE_JDBC_ID"
RUN echo "spring.datasource.engine.password: $POSTGRES_DATASOURCE_JDBC_PW"
RUN echo ""
RUN echo "spring.datasource.history.url: $POSTGRES_JDBC_URL_HISTORY"
RUN echo "spring.datasource.history.username: $POSTGRES_DATASOURCE_JDBC_ID"
RUN echo "spring.datasource.history.password: $POSTGRES_DATASOURCE_JDBC_PW"
RUN echo ""
RUN echo "spring.datasource.aaa.url: $POSTGRES_JDBC_URL_AAA"
RUN echo "spring.datasource.aaa.username: $POSTGRES_DATASOURCE_JDBC_ID"
RUN echo "spring.datasource.aaa.password: $POSTGRES_DATASOURCE_JDBC_PW"
RUN echo ""

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot jar file from the build stage
COPY --from=build /home/gradle/project/itcloud/build/libs/*.jar app.jar

EXPOSE 8080
EXPOSE 8443

# ENTRYPOINT [ "java", "-jar", "app.jar"]
ENTRYPOINT [ "java", "-jar", "app.jar" ]
CMD [\
#  "--server.ssl.enabled=${ITCLOUD_SSL_ENABLED}", \
#  "--server.ssl.key-store-password=${ITCLOUD_SSL_PASSWORD}", \
#  "--server.ssl.key-alias=${ITCLOUD_SSL_ALIAS}", \
  "--spring.datasource.engine.url=${POSTGRES_JDBC_URL_ENGINE}", \
  "--spring.datasource.engine.username:${POSTGRES_DATASOURCE_JDBC_ID}", \
  "--spring.datasource.engine.password:${POSTGRES_DATASOURCE_JDBC_PW}", \
  "--spring.datasource.history.url=${POSTGRES_JDBC_URL_HISTORY}", \
  "--spring.datasource.history.username=${POSTGRES_DATASOURCE_JDBC_ID}", \
  "--spring.datasource.history.password=${POSTGRES_DATASOURCE_JDBC_PW}", \
  "--spring.datasource.aaa.url=${POSTGRES_JDBC_URL_AAA}", \
  "--spring.datasource.aaa.username=${POSTGRES_DATASOURCE_JDBC_ID}", \
  "--spring.datasource.aaa.password=${POSTGRES_DATASOURCE_JDBC_PW}" \
]
