FROM openjdk:10.0.2-jre

EXPOSE 8080

WORKDIR /opt/wcapture

VOLUME /opt/wcapture/captures

COPY ./target/wcapture.jar ./wcapture.jar

COPY ./docker/docker-compose.yml /opt/dorefactor/docker-compose.yml

CMD ["java", "-jar", "-Xms64m", "-Xmx64m", "wcapture.jar"]
