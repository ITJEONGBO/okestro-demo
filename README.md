# okestro-demo

오케스트로 데모

## 🚀Quickstart

### 🧰Prerequisite(s)

- 🛠Intellij IDEA 
- ☕JDK (OpenJDK 1.8_201)
- 😺Tomcat (8.5.38)
- 🛅H2 Database
- 🐳Docker
  - `tomcat:8.5.38-jre8-alpine` (ssl: `8443`) 
  - `postgres:10.12-alpine` (port: `5432`)

### 😺Tomcat 

오케스트로는 https 프토토콜을 기본적으로 사용하기 때문에 톰캣 구성을 아래와 같이 해 준다.

> Intellij IDEA Community Edition을 사용할 경우 [Smart Tomcat 플러그인](https://github.com/zengkid/SmartTomcat) 을 활용하여 구성 

- 톰켓 환경 구성: 📁`<catalina base path>`
  - Environment Variables (환경변수) 설정: `-Dprofile=local`
  - SSL 포트: `8443`
  - p12 파일 구성: 📁`<catalina base path>/keystore/okestro.p12`) 비밀번호: `okestro2018`
  - context path: `/`
  - 📁`<catalina base path>/conf/server.xml` 수정

#### 📁`conf/server.xml`
     
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Server port="8015" shutdown="SHUTDOWN">
  <Service name="Catalina">
    <Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443"
               maxParameterCount="1000"
    />
   
    <!-- ... 생략 ...   -->
    <Connector port="8443" protocol="HTTP/1.1"
               maxThreads="150" SSLEnabled="true" scheme="https" secure="true"
               clientAuth="false" sslProtocol="TLS"
               keystoreFile="<catalina base path>/keystore/okestro.p12" keystorePass="okestro2018" />
  </Service>
</Server>
```

--- 

## 🛅H2 

| title | description |
| :---: | :--- |
| 목적 | 오케스트로 핵심정보 관리 |
| 🔌jdbc (로컬) | `jdbc:h2:<프로젝트경로>\docker\okestro\symphony?CIPER=AES` |
| 🔌jdbc (운영) | `jdbc:h2:~\.symphony\symphony?CIPER=AES` |
| 🔑id / pw | `symphony` / `symphony!123 symphony!123` |
  
[🧾자세한 정보 ... ][toH2]

---

## 🐳Docker 

### 🛠Okestro 

```sh
# war 빌드 후 (monolith/build/lib) 진행
docker build -t okestro-tomcat:0.0.1 \
  docker/okestro
```

```batch
REM war 빌드 후 (monolith/build/lib) 진행
docker build -t okestro-tomcat:0.0.1 ^
  docker/okestro
```

### ▶️Run 

```sh
# okestro
docker run -d -it \
  --name cst_tomcat \
  -p 8080:8080 \
  okestro-tomcat:0.0.1 

# postgres
docker run -d -it \
  --name cst_postgres \
  -e POSTGRES_PASSWORD=mysecretpassword \
  -e PGDATA=/var/lib/postgresql/data/pgdata \
  -v where/to/mount:/var/lib/postgresql/data \
  postgres:10.12-alpine
```

```batch
REM okestro
docker run -d -it ^
  --name cst_tomcat ^
  -p 8080:8080 ^
  -p 8443:8443 ^
  okestro-tomcat:0.0.1

REM postgres
docker run -d -it ^
  --name cst_postgres ^
  -e POSTGRES_PASSWORD=mysecretpassword ^
  -e PGDATA=/var/lib/postgresql/data/pgdata ^
  -v where/to/mount:/var/lib/postgresql/data ^
  postgres:10.12-alpine
```

## 🎯TODO

- [ ] 소스코드 초기상태 복구
  - [x] 버전 및 의존라이브러리 목록 정리
  - [x] model 및 상수
  - [x] tomcat embedded 구성
  - [ ] docker 관련 정보 수집
- [ ] package별 endpoint구현



## Dependencies 주입

| isChecked | full artifact name (w version) |
| :---: | :--- |
| 🔆 qemu의존 | `org.codehaus.mojo:animal-sniffer-annotations:1.14` |
| 🔆 qemu의존 | `com.google.code.findbugs:annotations:2.0.3` |
| 🔆 spring의존 | `aopalliance:aopalliance:1.0` |
| ✅ | `org.aspectj:aspectjrt:1.6.10` |
| ✅ | `org.aspectj:aspectjweaver:1.8.0` |
| 🔆 qemu의존 | `com.google.auto:auto-common:0.3` |
| 🔆 qemu의존 | `com.google.auto.service:auto-service:1.0-rc2` |
| ✅ | `org.webjars:bootstrap:3.3.6` |
| ✅ | `cglib:cglib-nodep:3.1` |
| 🔆 tiles의존 | `commons-beanutils:commons-beanutils:1.8.0` |
| 🔆 spring의존 | `commons-codec:commons-codec:1.9` |
| ✅ | `commons-configuration:commons-configuration:1.9` |
| ✅ | `commons-dbcp:commons-dbcp:1.4` |
| 🔆 tiles의존 | `commons-digester:commons-digester:2.0` |
| ✅ | `commons-fileupload:commons-fileupload:1.4` |
| 🔆 commons-io의존 | `commons-io:commons-io:2.7` |
| 🔆 commons-configuration의존 | `commons-lang:commons-lang:2.6` |
| ✅ | `org.apache.commons:commons-lang3:3.3.4` |
| 🔆 spring의존 | `commons-logging:commons-logging:1.1.1` |
| 🔆 commons-dbcp의존 | `commons-pool:commons-pool:1.6` |
| ✅ | `com.google.code.gson:gson:2.8.0` |
| 🔆 qemu의존 | `com.google.guava:guava:r05` |
| ✅ | `com.h2database:h2:1.4.197` |
| 🔆 spring의존 | `org.apache.httpcomponents:httpclient:4.5` |
| 🔆 spring의존 | `org.apache.httpcomponents:httpcore:4.4.1` | 
| 🔆 qemu의존 | `com.fasterxml.jackson.core:jackson-annotations:2.5.0` |
| 🔆 qemu의존 | `com.fasterxml.jackson.core:jackson-core:2.5.0` |
| 🔆 spring의존 | `org.codehaus.jackson:jackson-core-asl:1.9.13` |
| 🔆 qemu의존 | `com.fasterxml.jackson.core:jackson-databind:2.5.0` |
| 🔆 spring의존 | `org.codehaus.jackson:jackson-mapper-asl:1.9.13` |
| ✅ | `org.jasypt:jasypt:1.9.2` | 
| ✅ | `org.jasypt:jasypt-spring3:1.9.2` |
| ✅ | `javax.inject:javax.inject:1` |
| 🔆 tiles의존 | `org.slf4j:jcl-over-slf4j:1.7.6` |
| ✅ | `org.webjars:jquery:2.1.4` |
| 🔆 qemu의존 | `com.jcraft:jsch:0.1.52` |
| ✅ | `com.googlecode.json-simple:json-simple:1.1.1` |
| ✅ | `javax.servlet:jstl:1.2` |
| ✅ | `log4j:log4j:1.2.17` |
| ✅ | `org.apache.logging.log4j:log4j-api:2.17.0` |
| ✅ | `org.apache.logging.log4j:log4j-core:2.17.0` |
| ✅ | `org.apache.logging.log4j:log4j-slf4j-impl:2.17.0` |
| ✅ | `org.mybatis:mybatis:3.2.8` |
| ✅ | `org.mybatis:mybatis-spring:1.2.2` |
| ✅ | `org.postgresql:postgresql:42.1.4` |
| ✅ | `org.anarres.qemu:qemu-examples:1.0.6` |
| ✅ | `org.anarres.qemu:qemu-exec:1.0.6` |
| ✅ | `org.anarres.qemu:qemu-image:1.0.6` |
| ✅ | `org.anarres.qemu:qemu-qapi:1.0.6` |
| 🔆 spring의존 | `relaxngDatatype:relaxngDatatype:20020414` |
| ✅ | `org.ovirt.engine.api:sdk:4.2.1` |
| 🔆 spring의존 | `org.slf4j:slf4j-api:1.7.7` |
| ✅ | `org.springframework:spring-aop:4.3.14.RELEASE` |
| ✅ | `org.springframework:spring-beans:4.3.14.RELEASE` |
| ✅ | `org.springframework:spring-context:4.3.14.RELEASE` |
| ✅ | `org.springframework:spring-core:4.3.14.RELEASE` |
| ✅ | `org.springframework:spring-expression:4.3.14.RELEASE` |
| ✅ | `org.springframework:spring-jdbc:4.3.14.RELEASE` |
| ✅ | `org.springframework:spring-messaging:4.3.14.RELEASE` |
| ✅ | `org.springframework.security:spring-security-acl:4.2.2.RELEASE` |
| ✅ | `org.springframework.security:spring-security-config:4.2.2.RELEASE` |
| ✅ | `org.springframework.security:spring-security-core:4.2.2.RELEASE` |
| ✅ | `org.springframework.security:spring-security-taglibs:4.2.2.RELEASE` |
| ✅ | `org.springframework.security:spring-security-web:4.2.2.RELEASE` |
| ✅ | `org.springframework:spring-test:4.3.14.RELEASE` |
| ✅ | `org.springframework:spring-tx:4.3.14.RELEASE` |
| ✅ | `org.springframework:spring-web:4.3.14.RELEASE` |
| ✅ | `org.springframework:spring-webmvc:4.3.14.RELEASE` |
| ✅ | `org.springframework:spring-websocket:4.3.14.RELEASE` |
| 🔆 spring의존 | `javax.xml.stream:stax-api:1.0-2` |
| ✅ | `org.apache.tiles:tiles-api:3.0.5` |
| 🔆 tiles의존 | `org.apache.tiles:tiles-autotag-core-runtime:1.1.0` |
| ✅ | `org.apache.tiles:tiles-core:3.0.5` |
| ✅ | `org.apache.tiles:tiles-jsp:3.0.5` |
| 🔆 tiles의존 | `org.apache.tiles:tiles-request-api:1.0.6` |
| 🔆 tiles의존 | `org.apache.tiles:tiles-request-jsp:1.0.6` |
| 🔆 tiles의존 | `org.apache.tiles:tiles-request-servlet:1.0.6` |
| ✅ | `org.apache.tiles:tiles-servlet:3.0.5` |
| ✅ | `org.apache.tiles:tiles-template:3.0.5` |
| 🔆 spring의존 | `com.sun.xml.txw2:txw2:20110809` |


[toH2]: docs/H2.md