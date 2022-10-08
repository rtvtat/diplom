FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1 as aqa-shop
WORKDIR /opt/app
COPY ./aqa-shop.jar /opt/app/aqa-shop.jar
COPY ./application.properties /opt/app/application.properties
#ENTRYPOINT printenv
ENTRYPOINT ["java", "-jar", "aqa-shop.jar"]
EXPOSE 8080