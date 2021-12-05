FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD target/Coffe-Auth-beta-1.jar app.jar
ENV JAVA_OPTS="-Xms1024m -Xmx1024m"
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom ${JAVA_OPTS} -jar app.jar
