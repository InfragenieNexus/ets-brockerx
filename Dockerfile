FROM tomcat:10.1.13-jdk17

# Supprimer la webapp par défaut
RUN rm -rf /usr/local/tomcat/webapps/*

# Copier ton WAR généré
COPY target/BrockerX-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

# Tomcat démarre automatiquement
