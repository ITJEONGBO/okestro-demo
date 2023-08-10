# BUILD
# FROM gradle:7.4.2-jdk11-alpine AS builder

#
# WORKDIR /
#
# COPY ./ ./
# RUN gradle monolith:war -Pprofile=staging

# RUN
FROM tomcat:8.5.38-jre8-alpine
LABEL AUTHOR chlee (chanhi2000@gmail.com)
LABEL version='0.0.5'

RUN rm -rf /usr/local/tomcat/webapps/*

ARG PORT_SSL
ARG PORT_DEFAULT

VOLUME /tmp

ADD docker/okestro/server.xml /usr/local/tomcat/conf/server.xml
ADD docker/okestro/okestro.p12 /usr/local/tomcat/keystore/okestro.p12
ADD docker/okestro/ROOT /usr/local/tomcat/webapps/ROOT

EXPOSE 8080
EXPOSE 8443

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]