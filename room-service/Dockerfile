FROM openjdk:17

# /home/ubuntu/app
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} room.jar

#ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/accommodation.jar"]

ENTRYPOINT ["java","-jar","/room.jar"]