#!/bin/bash
#
# Blocks until tomcat is ready
#
until [ "`curl --silent --show-error --connect-timeout 1 -I http://127.0.0.1:8080/ | grep '200'`" != "" ];
	do
	  echo --- "Waiting until Tomcat is reachable (will retry in 5 seconds)"
	  sleep 5
	done
echo Tomcat is ready!
