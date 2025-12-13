FROM liquibase/liquibase:4.31.1

COPY ./project-build/postgre-repo/src/main/resources/db/* /liquibase/
COPY ./project-build/postgre-repo/migration/* /liquibase/

CMD ["liquibase", "--changelog-file=master.yml", "update"]

