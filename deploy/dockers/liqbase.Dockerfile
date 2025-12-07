FROM liquibase/liquibase:4.31.1

COPY ../../project-build/postgre-repo/src/main/resources/db/* /liquibase/

CMD ["liquibase", "--changelog-file=data-set-v0.yml", "update"]
