# =========================
# Étape 1 : build avec Maven
# =========================
FROM maven:3.9.3-eclipse-temurin-17 AS builder
WORKDIR /app

# Copier les fichiers sources
COPY pom.xml .
COPY src ./src

# Compiler l'application (skip tests pour CI/CD rapide)
RUN mvn clean package -DskipTests

# =========================
# Étape 2 : runtime Tomcat
# =========================
FROM tomcat:10.1-jdk17
WORKDIR /usr/local/tomcat/webapps

# Copier le .war généré dans Tomcat
COPY --from=builder /app/target/BrockerX-0.0.1-SNAPSHOT.war ./ROOT.war

EXPOSE 8080
