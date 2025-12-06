#/bin/bash

docker stop `docker ps  | grep postgres | awk '{ print $1}'`
docker rm `docker ps -a | grep postgres | awk '{ print $1}'`
sleep 1
docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d -p 5432:5432 postgres:15.4
export JAVA_HOME=~/jdk-21.0.9/
./gradlew :project-build:postgre-repo:update --info -Pjooq.enabled=true