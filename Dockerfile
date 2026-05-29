# ETAPA 1: Construcción (Usando una imagen oficial que ya tiene Maven instalado)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace/app

# Fíjate que ahora copiamos menos cosas. Solo necesitamos el pom y el código fuente.
COPY pom.xml .
COPY src src

# Usamos el comando 'mvn' real del sistema, ignorando el wrapper local
RUN mvn clean install -DskipTests

# ETAPA 2: Ejecución (El contenedor final ligero)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Rescatamos el archivo .jar generado en la Etapa 1
COPY --from=build /workspace/app/target/*.jar app.jar

# Exponemos el puerto
EXPOSE 8080

# Arrancamos la API
ENTRYPOINT ["java", "-jar", "app.jar"]