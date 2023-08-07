FROM tomcat:8.5.38-jre8-alpine

RUN rm -rf /usr/local/tomcat/webapps/*

ARG PORT_SSL
ARG PORT_DEFAULT

ADD server.xml /usr/local/tomcat/conf/server.xml
ADD okestro.p12 /usr/local/tomcat/keystore/okestro.p12
ADD ROOT /usr/local/tomcat/webapps/ROOT

EXPOSE 8080
EXPOSE 8443

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]