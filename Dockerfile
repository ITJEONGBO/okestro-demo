# BUILD
# FROM gradle:7.4.2-jdk11-alpine AS builder

#
# WORKDIR /
#
# COPY ./ ./
# RUN gradle monolith:war -Pprofile=staging

# RUN
FROM eclipse-temurin:11-jdk-alpine
LABEL author="chlee (chanhi2000@gmail.com)"
LABEL version='0.0.5'

ARG ARG_OVIRT_IP
ARG ARG_OKESTRO_PORT_HTTP
ARG ARG_OKESTRO_PORT_HTTPS
ARG JAR_NAME=monolith-0.0.5
ARG JAR_FILE=monolith/build/libs/${JAR_NAME}.jar

ENV OVIRT_IP=${ARG_OVIRT_IP}
ENV OKESTRO_PORT_HTTP=${ARG_OKESTRO_PORT_HTTP}
ENV OKESTRO_PORT_HTTPS=${ARG_OKESTRO_PORT_HTTPS}

VOLUME /tmp

EXPOSE 8080

# ADD docker/okestro/server.xml /usr/local/tomcat/conf/server.xml
# ADD docker/okestro/okestro.p12 /usr/local/tomcat/keystore/okestro.p12
# ADD docker/okestro/ROOT /usr/local/tomcat/webapps/ROOT
# ADD docker/okestro/entrypoint.sh /opt/entrypoint
# RUN chmod +x /opt/entrypoint.sh

ADD ${JAR_FILE} $JAR_NAME

ENTRYPOINT ["java","-jar","/$JAR_NAME"]