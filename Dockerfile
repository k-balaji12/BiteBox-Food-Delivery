
FROM tomcat:10.1-jdk17-temurin

# Configure Tomcat to listen on Render's default web port
RUN sed -i 's/port="8080"/port="10000"/' /usr/local/tomcat/conf/server.xml

# Deploy BiteBox as the root application
COPY BiteBox.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 10000
