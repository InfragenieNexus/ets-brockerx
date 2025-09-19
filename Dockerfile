# Étape 1 : utiliser une image JDK pour builder/packager
FROM eclipse-temurin:21-jdk-jammy AS builder

# Définir le dossier de travail
WORKDIR /app

# Copier le pom.xml et le code source
COPY pom.xml .
COPY src ./src

# Builder le JAR avec Maven
RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests

# Étape 2 : image légère pour exécuter le JAR
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/BrockerX-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
