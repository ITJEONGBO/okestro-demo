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

ARG ARG_OVIRT_IP
ARG ARG_OKESTRO_PORT_HTTP
ARG ARG_OKESTRO_PORT_HTTPS

ENV OVIRT_IP=${ARG_OVIRT_IP}
ENV OKESTRO_PORT_HTTP=${ARG_OKESTRO_PORT_HTTP}
ENV OKESTRO_PORT_HTTPS=${ARG_OKESTRO_PORT_HTTPS}

RUN rm -rf /usr/local/tomcat/webapps/*

VOLUME /tmp

ADD docker/okestro/server.xml /usr/local/tomcat/conf/server.xml
ADD docker/okestro/okestro.p12 /usr/local/tomcat/keystore/okestro.p12
ADD docker/okestro/ROOT /usr/local/tomcat/webapps/ROOT
ADD docker/okestro/entrypoint.sh /opt/entrypoint
RUN chmod +x /opt/entrypoint.sh

ENTRYPOINT ["/opt/entrypoint.sh"]

# OKESTRO_PORT_HTTP 와 같도록
EXPOSE 8080
# OKESTRO_PORT_HTTPS 와 같도록
EXPOSE 8443

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]