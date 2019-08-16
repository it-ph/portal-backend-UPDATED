FROM openjdk:8
ADD /target/portal.jar portal.jar 
EXPOSE 8081
ENTRYPOINT ["java","-jar","portal.jar"]