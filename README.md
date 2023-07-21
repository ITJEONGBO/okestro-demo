# okestro-demo

오케스트로 데모

## 🚀Quickstart

### Prerequisite(s)

- JDK (OpenJDK 1.8_201)
- Tomcat (8.5.38)
- Docker

## 🎯TODO

- [ ] 소스코드 초기상태 복구
  - [x] 버전 및 의존라이브러리 목록 정리
  - [x] model 및 상수
  - [x] tomcat embedded 구성
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