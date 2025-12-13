FROM liquibase/liquibase:4.31.1

COPY ./project-build/postgre-repo/src/main/resources/db/* /liquibase/
COPY ./project-build/postgre-repo/migration/* /liquibase/

CMD ["liquibase", "--changelog-file=master.yml --url=${DB_URL} --username=${DB_USER} --password=${DB_PASS}", "update"]

