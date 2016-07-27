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
# `docker run -it -p 8080:8080 -v -v /local/path/to/bimgur/client:/usr/local/tomcat/webapps/bimgur bimgur`
#
# -----------------------------------------------------------------------------
# Connecting via browser
# -----------------------------------------------------------------------------
#
# http://localhost:8080/bimgur/index-dev.html
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

ADD server/tomcat/catalina.properties /usr/local/tomcat/conf/
ADD server/tomcat/setenv.sh /usr/local/tomcat/bin/

# -----------------------------------------------------------------------------
# Activiti Explorer
# -----------------------------------------------------------------------------

ADD server/activiti/activiti-explorer-*.war /tmp/
RUN unzip /tmp/activiti-explorer-*.war -d /usr/local/tomcat/webapps/activiti-explorer
ADD server/activiti/db.properties /usr/local/tomcat/webapps/activiti-explorer/WEB-INF/classes/
ADD server/activiti/engine.properties /usr/local/tomcat/webapps/activiti-explorer/WEB-INF/classes/
ADD server/postgres/postgres*.jar /usr/local/tomcat/webapps/activiti-explorer/WEB-INF/lib/
ADD activiti-custom/target/scala-2.11/bimgur-activiti-custom*.jar /usr/local/tomcat/webapps/activiti-explorer/WEB-INF/lib/

# -----------------------------------------------------------------------------
# Activiti REST
# -----------------------------------------------------------------------------

ADD server/activiti/activiti-rest-*.war /tmp/
RUN unzip /tmp/activiti-rest-*.war -d /usr/local/tomcat/webapps/activiti-rest
ADD server/activiti/activiti-rest-web.xml /usr/local/tomcat/webapps/activiti-rest/WEB-INF/web.xml
ADD server/activiti/db.properties /usr/local/tomcat/webapps/activiti-rest/WEB-INF/classes/
ADD server/activiti/engine.properties /usr/local/tomcat/webapps/activiti-rest/WEB-INF/classes/
ADD server/postgres/postgres*.jar /usr/local/tomcat/webapps/activiti-rest/WEB-INF/lib/
ADD activiti-custom/target/scala-2.11/bimgur-activiti-custom*.jar /usr/local/tomcat/webapps/activiti-rest/WEB-INF/lib/

# -----------------------------------------------------------------------------
# Three, two, one -- GO! :-)
# -----------------------------------------------------------------------------

ADD server/start-from-docker.sh /tmp/
CMD ["/tmp/start-from-docker.sh"]