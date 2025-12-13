#/bin/bash

docker stop `docker ps -a | grep -v CONTAINER | awk ' { a = a " "  $1 } END { print a }' `
docker rm `docker ps -a | grep -v CONTAINER | awk ' { a = a " "  $1 } END { print a }' `
