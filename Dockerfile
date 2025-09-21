# Étape 1 : build avec Maven
FROM tomcat:10.1.13-jdk17 AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests

# Étape 2 : runtime Tomcat
FROM tomcat:10.1.13-jdk17
COPY --from=builder /app/target/BrockerX-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
