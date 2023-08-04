FROM tomcat:8.5.38-jre8-alpine

RUN rm -rf /usr/local/tomcat/webapps/*

ADD server.xml /usr/local/tomcat/conf/server.xml
ADD okestro.p12 /usr/local/tomcat/keystore/okestro.p12
ADD okestro-monolith-0.0.1.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]