FROM azul/zulu-openjdk:11

EXPOSE 8080

WORKDIR /opt/wcapture

COPY ./target/wcapture.jar ./wcapture.jar

COPY ./docker/docker-compose.yml /opt/dorefactor/docker-compose.yml

RUN useradd -ms /bin/bash wcapture \
    && chown -R wcapture:wcapture /opt/wcapture \
    && chown -R wcapture:wcapture /opt/dorefactor

USER wcapture

CMD ["java", "-jar", "-Xms64m", "-Xmx64m", "wcapture.jar"]
