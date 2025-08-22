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