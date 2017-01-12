#!/usr/bin/env python

import socket
import sys
import time

IP = "127.0.0.1"
PORT = 8080

REQUEST= """GET /activiti-rest/service/identity/users HTTP/1.1
Host: 127.0.0.1:8080
Accept-Encoding: gzip
User-Agent: okhttp/3.3.0

GET /activiti-rest/service/identity/users HTTP/1.1
Authorization: Basic a2VybWl0Omtlcm1pdA==
Host: 127.0.0.1:8080
Accept-Encoding: gzip
User-Agent: okhttp/3.3.0

"""

responseStart = '{"data":[{"id":"fozzie","firstName":"Fozzie","lastName":"Bear",'

def httpGET():
    print "Try to get users from Activiti endpoint"
    try:
        c = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        # connect to the socket
        c.connect((IP, PORT))
        # create a file-like object to read
        fileobj = c.makefile('r', 0)
        # Ask the server for the file
        fileobj.write(REQUEST)
        response = fileobj.readlines()
        return response[25]
    except Exception:
        print "Unexpected error:", sys.exc_info()[0]
        sys.exit("Not able to get users")

def isServerReady(response):
    print "Successfully received a response"
    return response[:len(responseStart)] == responseStart

def giveServerTimeToSetUp():
    print "Wait 60s until docker container should be running"
    time.sleep(60)

def main():
    giveServerTimeToSetUp()
    while(not isServerReady(httpGET())):
        print "Failed: The Response did not meet the expectation" 
    print "Success: Activti is set up and ready."

if __name__ == '__main__':
      main()