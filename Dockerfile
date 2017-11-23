FROM openjdk:8u131-jre-alpine

EXPOSE 8080

WORKDIR /opt/wcapture

VOLUME /opt/wcapture/captures

COPY target/*.jar ./wcapture.jar

CMD ["java", "-jar", "-Xms128m", "-Xmx128m", "wcapture.jar"]


