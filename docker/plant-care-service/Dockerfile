FROM openjdk:8-jre

COPY docker/plant-care-service/plant-care-conf/ /plant-care-conf

RUN find /plant-care-conf -type f -exec chmod 777 {} \;

ARG setupfile
COPY ["$setupfile", "/plant-care-service.jar"]

EXPOSE 30750

ENTRYPOINT ["/plant-care-conf/docker-entry.sh"]
