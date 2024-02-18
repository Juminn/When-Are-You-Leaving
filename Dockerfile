FROM azul/zulu-openjdk:11
ADD build/libs/costCalculrator-0.0.1-SNAPSHOT.jar costCalculrator-0.0.1-SNAPSHOT.jar
EXPOSE  8080
ENTRYPOINT ["java", "-jar", "/crawler-springboot.jar"]

