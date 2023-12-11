FROM openjdk:17-alpine
add target/user-service.jar user-service.jar
ENTRYPOINT [ "java", "-jar", "user-service.jar" ]
