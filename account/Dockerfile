FROM gradle:jdk17-alpine AS build

COPY . /home/gradle

RUN gradle build

FROM openjdk:17

# FIXME don't hard-code the version in the artifact file name
COPY --from=build /home/gradle/build/libs/account-0.0.1-SNAPSHOT.jar .

CMD java -jar account-0.0.1-SNAPSHOT.jar