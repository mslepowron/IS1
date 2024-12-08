# Usa una imagen de OpenJDK 21 como base
FROM openjdk:21-jdk-slim AS build

# Instala Maven
RUN apt-get update && apt-get install -y maven

# Establece el directorio de trabajo
WORKDIR /home/app

# Copia los archivos de configuración de Maven
COPY pom.xml .

# Resuelve las dependencias de Maven
RUN mvn dependency:resolve dependency:resolve-plugins

# Copia el resto de tu código
COPY . .

# Compila el proyecto
RUN mvn package -DskipTests

# Etapa de despliegue con JRE 21
FROM eclipse-temurin:21-jre AS deploy
WORKDIR /home/app
COPY --from=build /home/app/target/product-example-0.0.1-SNAPSHOT.jar /home/app/product-example-0.0.1-SNAPSHOT.jar
COPY ./src/main/resources /home/app/back/src/main/resources
EXPOSE 9500
ENTRYPOINT ["java", "-jar", "/home/app/product-example-0.0.1-SNAPSHOT.jar"]
