#!/bin/bash
################################################################
#
# entrypoint.sh
#
# Tomcat 실행 전 정의 된 환경변수를 기준으로 ovirt 환경정보 정의
# common.properties 변형
#
# OVIRT_IP: ovirt 클라이언트 IP주소
# OKESTRO_PORT_HTTP: (Tomcat에 할당 할) Okestro http 포트 번호
# OKESTRO_PORT_HTTPS: (Tomcat에 할당 할) Okestro https 포트 번호
#
#
echo "Inspect Docker-defined Environment Variable(s)"
echo "..."
if [ -z "${OVIRT_IP}" ]; then
    echo "'OVIRT_IP' not defined"
    export OVIRT_IP="192.168.0.50" # 기본값
else
    export OVIRT_IP=${OVIRT_IP}
fi

if [ -z "${OKESTRO_PORT_HTTP}" ]; then
    echo "'OKESTRO_PORT_HTTP' not defined"
    export OKESTRO_PORT_HTTP="8080" # 기본값
else
    export OKESTRO_PORT_HTTP=${OKESTRO_PORT_HTTP}
fi

if [ -z "${OKESTRO_PORT_HTTPS}" ]; then
    echo "'OKESTRO_PORT_HTTPS' not defined"
    export OKESTRO_PORT_HTTPS="8443" # 기본값
else
    export OKESTRO_PORT_HTTPS=${OKESTRO_PORT_HTTPS}
fi
echo "OVIRT_IP: ${OVIRT_IP}"
echo "OKESTRO_PORT_HTTP: ${OKESTRO_PORT_HTTP}"
echo "OKESTRO_PORT_HTTPS: ${OKESTRO_PORT_HTTPS}"
echo
echo


echo "Modify common.properties accordingly"
echo "..."
cd /opt
echo "okestro.ip=${OVIRT_IP}" >> common.properties
echo "okestro.port-http=${OKESTRO_PORT_HTTP}" >> common.properties
echo "okestro.port-https=${OKESTRO_PORT_HTTPS}" >> common.properties

cd /usr/local/tomcat/webapps/ROOT/webapps/WEB-INF/classes/properties
mv common.properties /opt/common-old.propreties # backup
cp /opt/common.properties common.properties
echo
echo


echo "Run Catalina Now!"
echo "..."
./usr/local/tomcat/bin/catalina.sh run