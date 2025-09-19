# Base image avec Java
FROM eclipse-temurin:17-jdk-alpine

# Créer un dossier pour l'application
WORKDIR /app

# Copier le jar dans l'image
COPY target/BrockerX-0.0.1-SNAPSHOT.jar app.jar


# Exposer le port
EXPOSE 8080

# Commande pour lancer l'application
ENTRYPOINT ["java","-jar","app.jar"]
