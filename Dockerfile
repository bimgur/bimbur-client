# -----------------------------------------------------------------------------
# Building a new, tagged image
# -----------------------------------------------------------------------------
#
# `docker build --tag=bimgur .`
#
# -----------------------------------------------------------------------------
# Running a tagged image
# -----------------------------------------------------------------------------
#
# `docker run -it -p 8080:8080 -v /local/path/bimgur-server/data:/data bimgur`
#
# -----------------------------------------------------------------------------
# Connecting via browser
# -----------------------------------------------------------------------------
#
# http://localhost:8080/activiti-explorer
#
# Mac OSX: `docker-machine ip` instead of localhost
#
# -----------------------------------------------------------------------------

FROM tomcat:8.5.4-jre8
RUN apt-get update

# -----------------------------------------------------------------------------
# Font Issues (Prevent NPE upon activiti process diagram creation)
# -----------------------------------------------------------------------------

RUN apt-get install -y fontconfig

# -----------------------------------------------------------------------------
# PostgreSQL
# -----------------------------------------------------------------------------

RUN apt-get install -y -q postgresql-9.4

USER postgres
RUN /etc/init.d/postgresql start\
    && /usr/bin/psql --command "CREATE USER bimgur WITH SUPERUSER PASSWORD 'bimgurpw';" \
    && /usr/bin/createdb -O bimgur bimgurdb

USER root
# ensure host can connect to postgres correctly
RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.4/main/pg_hba.conf
RUN echo "listen_addresses='*'" >> /etc/postgresql/9.4/main/postgresql.conf

# -----------------------------------------------------------------------------
# Tomcat
# -----------------------------------------------------------------------------

ADD bimgur-server/tomcat/catalina.properties /usr/local/tomcat/conf/
ADD bimgur-server/tomcat/setenv.sh /usr/local/tomcat/bin/

# -----------------------------------------------------------------------------
# Activiti Explorer
# -----------------------------------------------------------------------------

ADD bimgur-server/activiti/activiti-explorer-*.war /tmp/
RUN unzip -o /tmp/activiti-explorer-*.war -d /usr/local/tomcat/webapps/activiti-explorer
ADD bimgur-server/activiti/db.properties /usr/local/tomcat/webapps/activiti-explorer/WEB-INF/classes/
ADD bimgur-server/activiti/engine.properties /usr/local/tomcat/webapps/activiti-explorer/WEB-INF/classes/
ADD bimgur-server/postgres/postgres*.jar /usr/local/tomcat/webapps/activiti-explorer/WEB-INF/lib/
ADD bimgur-activiti-custom/build/libs/bimgur-activiti-custom-*.jar /usr/local/tomcat/webapps/activiti-explorer/WEB-INF/lib/
ADD bimgur-activiti-custom/build/dependencies/*.jar /usr/local/tomcat/webapps/activiti-rest/WEB-INF/lib/

# -----------------------------------------------------------------------------
# Activiti REST
# -----------------------------------------------------------------------------

ADD bimgur-server/activiti/activiti-rest-*.war /tmp/
RUN unzip -o /tmp/activiti-rest-*.war -d /usr/local/tomcat/webapps/activiti-rest
ADD bimgur-server/activiti/activiti-rest-web.xml /usr/local/tomcat/webapps/activiti-rest/WEB-INF/web.xml
ADD bimgur-server/activiti/db.properties /usr/local/tomcat/webapps/activiti-rest/WEB-INF/classes/
ADD bimgur-server/activiti/engine.properties /usr/local/tomcat/webapps/activiti-rest/WEB-INF/classes/
ADD bimgur-server/postgres/postgres*.jar /usr/local/tomcat/webapps/activiti-rest/WEB-INF/lib/
ADD bimgur-activiti-custom/build/libs/bimgur-activiti-custom-*.jar /usr/local/tomcat/webapps/activiti-rest/WEB-INF/lib/
ADD bimgur-activiti-custom/build/dependencies/*.jar /usr/local/tomcat/webapps/activiti-rest/WEB-INF/lib/

# -----------------------------------------------------------------------------
# Windows compatibility
# -----------------------------------------------------------------------------
RUN apt-get install -y -q dos2unix
RUN dos2unix /tmp/start-from-docker.sh

# -----------------------------------------------------------------------------
# Three, two, one -- GO! :-)
# -----------------------------------------------------------------------------

ADD bimgur-server/start-from-docker.sh /tmp/
CMD ["/tmp/start-from-docker.sh"]