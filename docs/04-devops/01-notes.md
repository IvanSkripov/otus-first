# Основные подходы

# Заметки
- добавлен контейнер с S3
- логи envoy выведены во fluent-bit (TODO: filter=parsing)
- логи Minio выведены во fluent-bit (TODO)

## Команды

- docker logs `docker ps | grep nginx | awk '{ print $1}'` - просмотр логов nginx
- http://158.160.184.84:5602 - заход на OpenSearch
  - opensearch_user=admin
  - opensearch_pass=adm-Password0
- https://rubular.com/ - проверка regexp для fluent_bit
- запуск Gradle `./gradlew :project-build:postgre-repo:generateJooq --stacktrace`

  - запуск докер контейнер с Postgre и контроль работы. Ссылка на docker image - https://hub.docker.com/_/postgres
```
        - docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d -p 5432:5432 postgres:15.4
        -  docker exec -it some-postgres bash
        -  sudo ss -tln
```
  - Клиент для Postgre
    -`sudo apt install postgresql-client`
    -`psql "host=localhost port=5432 dbname=postgres user=postgres"`
       - `\dt`
       - `\d images`
       - `\l`
       - `\?`



