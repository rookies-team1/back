FROM openjdk:17
LABEL authors="bitedradish"
COPY build/libs/alreadyemployee-0.0.1-SNAPSHOT.jar alreadyemployee-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/alreadyemployee-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
