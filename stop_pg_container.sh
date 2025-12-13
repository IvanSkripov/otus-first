#/bin/bash

docker stop `docker ps  | grep postgres | awk '{ print $1}'`
docker rm `docker ps -a | grep postgres | awk '{ print $1}'`