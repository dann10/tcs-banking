FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/tcs-transactionmovement-1.0.0.jar app.jar

EXPOSE 8081

ENV SERVER_PORT=8081
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/BANKING_TCS?useSSL=false&serverTimezone=America/Guayaquil&allowPublicKeyRetrieval=true
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=12345
ENV SPRING_JPA_HIBERNATE_DDL-AUTO=update
ENV SPRING_JPA_SHOW-SQL=true
ENV SPRING_JACKSON_TIME-ZONE=America/Guayaquil
ENV SPRING_ACTIVEMQ_BROKER-URL=tcp://activemq:61616?wireFormat.maxInactivityDuration=30000
ENV SPRING_ACTIVEMQ_USER=admin
ENV SPRING_ACTIVEMQ_PASSWORD=admin
ENV CLIENT_URL=http://client-app:8080/clientes

ENTRYPOINT ["java", "-jar", "app.jar"]