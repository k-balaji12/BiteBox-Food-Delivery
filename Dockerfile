
FROM tomcat:10.1-jdk17-temurin

# Deploy BiteBox as the root web application
COPY BiteBox.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
