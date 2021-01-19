call mvn clean package -Dquarkus.package.type=uber-jar -Dmaven.test.skip=true
call docker build -t accounts .
call docker-compose up -d --build
timeout /t 20
call mvn test
call docker-compose down