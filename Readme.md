#запуск тестов с использованием mysql
docker-compose up -d --force-recreate -e TYPE=mysql
# запуск тестов с использованием postgres
docker-compose up -d --force-recreate -e TYPE=mysql

#остановка
docker-compose down

docker run -e SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/db_shop -e SPRING_DATASOURCE_USERNAME=postgres -e SPRING_DATASOURCE_PASSWORD=asdvbn123 -e SPRING_JPA_DATABASE=POSTGRESQL -p 8080:8080 aqa-shop

#старт 
java -jar ./artifacts/aqa-shop.jar -Dspring.profiles.active=localpg
java -jar ./artifacts/aqa-shop.jar -Dspring.profiles.active=mysql
java -jar ./artifacts/aqa-shop.jar -Dspring.profiles.active=postgres