#!/bin/bash
#
# Combines launching of DB server and tomcat startup
#
# Necessary because these processes MUST be running from the same
# docker container (or the server IP will not be reachable...)
#
service postgresql start && catalina.sh run
