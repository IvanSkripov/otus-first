#/bin/bash

export VERSION=0.0.1
docker network create -d bridge migration_network
docker build -t migration:$VERSION -f ./deploy/dockers/liqbase.Dockerfile .
set -a; source .env; set +a
docker run --name some-postgres --network=migration_network -e POSTGRES_USER=$DB_USER -e POSTGRES_PASSWORD=$DB_PASS -d -p 5432:5432 postgres:15.4
docker run --network=migration_network -e LIQUIBASE_COMMAND_URL=MIGRATION_URL -e LIQUIBASE_COMMAND_USERNAME=$DB_USER -e LIQUIBASE_COMMAND_PASSWORD=$DB_PASS migration:$VERSION
