# =========================
# Étape 1 : Builder avec Maven
# =========================
FROM maven:3.9.3-eclipse-temurin-17 AS builder
WORKDIR /app

# Copier les fichiers sources
COPY pom.xml .
COPY src ./src

# Compiler et tester l'application (tests peuvent être skip si voulu)
RUN mvn clean package -DskipTests

# =========================
# Étape 2 : Runtime Maven + Tomcat
# =========================
FROM maven:3.9.3-eclipse-temurin-17
WORKDIR /usr/local/tomcat

# Installer Tomcat
RUN apt-get update && apt-get install -y curl unzip && \
    curl -L "https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.13/bin/apache-tomcat-10.1.13.zip" -o tomcat.zip && \
    unzip tomcat.zip && mv apache-tomcat-10.1.13 tomcat && rm tomcat.zip

# Copier le WAR généré dans Tomcat
COPY --from=builder /app/target/BrockerX-0.0.1-SNAPSHOT.war /usr/local/tomcat/tomcat/webapps/ROOT.war

# Ajouter Maven au PATH (déjà présent dans cette image)
ENV PATH="/usr/local/maven/bin:${PATH}"

WORKDIR /usr/local/tomcat/tomcat

EXPOSE 8080

# Commande de démarrage
CMD ["bin/catalina.sh", "run"]
